package com.jtk.aws.lambda.s3sns.errorhandling;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    public void handler(SNSEvent snsEvent){
        snsEvent.getRecords()
                .forEach(snsRecord -> logger.info("Dead Letter Queue Event " + snsRecord.toString()));
    }
}
