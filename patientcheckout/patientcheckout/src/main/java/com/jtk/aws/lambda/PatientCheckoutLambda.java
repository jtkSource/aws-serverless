package com.jtk.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtk.aws.lambda.s3sns.PatientCheckoutEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class PatientCheckoutLambda {

    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
    private final String patient_checkout_topic = System.getenv("PATIENT_CHECKOUT_TOPIC");

    public void handler(S3Event s3Event, Context context){
        LambdaLogger logger = context.getLogger();

        s3Event.getRecords().forEach(record->{
            S3ObjectInputStream s3InputStream = s3
                    .getObject(record.getS3().getBucket().getName(),
                            record.getS3().getObject().getKey())
                    .getObjectContent();
            try {
                List<PatientCheckoutEvent> patientCheckoutEvents =
                        Arrays.asList(objectMapper.readValue(s3InputStream, PatientCheckoutEvent[].class));
                logger.log(patientCheckoutEvents.toString());
                s3InputStream.close();
                logger.log("Message published to SNS...");
                publishMessageToSNS(patientCheckoutEvents);
            } catch (Exception e) {
                logger.log("Exception" + e.toString());
                throw new RuntimeException("Error in processing s3 event", e);
            }
        });

    }

    private void publishMessageToSNS(List<PatientCheckoutEvent> patientCheckoutEvents) {
        patientCheckoutEvents.forEach(patientCheckoutEvent -> {
            try {
                sns.publish(patient_checkout_topic,
                        objectMapper.writeValueAsString(patientCheckoutEvent));
            } catch (JsonProcessingException e) {

            }
        });
    }
}
