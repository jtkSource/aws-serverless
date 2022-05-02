package com.jtk.aws.lambda;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtk.aws.lambda.s3sns.PatientCheckoutEvent;

public class BillManagementLambda {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void handler(SNSEvent snsEvent){
        snsEvent.getRecords()
                .forEach(snsRecord -> {
                    try {
                        PatientCheckoutEvent pce = objectMapper.readValue(snsRecord.getSNS().getMessage(),
                                PatientCheckoutEvent.class);
                        System.out.println(pce);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
