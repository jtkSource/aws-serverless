package com.jtk.aws.lambda.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtk.aws.lambda.api.model.Order;

public class CreateOrderLambda {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());

    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent)
            throws JsonProcessingException {
        Order order = objectMapper.readValue(apiGatewayProxyRequestEvent.getBody(), Order.class);
        Table orderTable = dynamoDB.getTable(System.getenv("ORDERS_TABLE"));
        Item item = new Item()
                .withPrimaryKey("id",order.id)
                .withString("itemName", order.itemName)
                .withInt("quantity",order.quantity);
        orderTable.putItem(item);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Order Created OrderID: "+order.id);
    }
}
