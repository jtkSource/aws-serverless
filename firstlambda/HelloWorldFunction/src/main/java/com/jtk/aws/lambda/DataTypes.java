package com.jtk.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataTypes {

    private Double instanceVariable = Math.random();
    private static Double staticVariable = Math.random();
    public DataTypes(){
        System.out.println("Inside Constructor");
    }
    static {
        System.out.println("Static block Constructor");
    }
    public void coldStartBasics(){
        Double localVariable = Math.random();
        System.out.println(
                " Instance: " + instanceVariable +
                "\n Static Variable: " + staticVariable +
                "\n Local Variable: " + localVariable +
                "\n");
    }

    public int getNumber(float number){
        return (int) number;
    }

    public List<Integer> getScores(List<String> names){
        Map<String, Integer> studentScores = new HashMap<>();
        studentScores.put("John",90);
        studentScores.put("Bob",80);
        studentScores.put("Ahmed",100);
        return studentScores.entrySet()
                .stream()
                .filter(entry-> names.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void saveEmployeeData(Map<String, Integer> employeeData){
        System.out.println(employeeData);
    }

    public ClinicalData getVitals(Patient patient){
        System.out.println(patient.getName());
        System.out.println(patient.getSsn());
        ClinicalData clinicalData = new ClinicalData();
        clinicalData.setBp("80/120");
        clinicalData.setHeartRate("80bpm");
        return clinicalData;
    }

    public void getOutput(InputStream input, OutputStream outputStream, Context runtimeContext) throws IOException, InterruptedException {
        Thread.sleep(4000); //timeout the lambda
        System.out.println(System.getenv("restapiurl"));
        System.out.println(runtimeContext.getAwsRequestId());
        System.out.println(runtimeContext.getFunctionName());
        System.out.println(runtimeContext.getRemainingTimeInMillis());
        System.out.println(runtimeContext.getMemoryLimitInMB());
        int data;
        while ((data = input.read()) != -1){
            outputStream.write(Character.toLowerCase(data));
        }
    }
}
