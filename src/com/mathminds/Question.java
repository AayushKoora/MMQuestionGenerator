package com.mathminds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Question {

    public int templateId;
    public String type;
    public String templateText;
    public ArrayList<String> fieldNames;
    public HashMap<String, String> fields;
    public Method solver;


    public Question(int id, String type, String templateText, ArrayList<String> fieldNames, HashMap<String, String> fields, Method solver) {
        this.templateId = id;
        this.type = type;
        this.templateText = templateText;
        this.fieldNames = fieldNames;
        this.fields = fields;
        this.solver = solver;
    }


    public void randomizeFields() {
        for (String fieldName : fieldNames) {
            String type = fieldName.split("_")[1].split(":")[0];
            double lowerBound = 0;
            double upperBound = 0;
            if (type.equals("int") || type.equals("double")) {
                lowerBound = Double.parseDouble(fieldName.split(":")[1].split("~")[0]);
                upperBound = Double.parseDouble(fieldName.split("~")[1].split("_")[0]);
            }

            Random random = new Random();

            String newVal = "";
            switch (type) {
                case "int" -> newVal = "" + random.nextInt((int) lowerBound, (int) upperBound + 1);
                case "double" -> {
                    double tempVal = random.nextDouble(lowerBound, upperBound);
                    double scale = Math.pow(10, AppInterface.doubleFieldsDecimalPlaces);
                    newVal = "" + Math.round(tempVal * scale) / scale;
                }
                case "sequence" -> {
                    //_sequence:pos=1&initial=3~7&scale=2~7_
                    int pos = Integer.parseInt(fieldName.split("=")[1].split("&")[0]);
                    int initial;
                    if (pos == 1) {
                        int lowerBoundInitial = Integer.parseInt(fieldName.split("initial=")[1].split("~")[0]);
                        int upperBoundInitial = Integer.parseInt(fieldName.split("initial=")[1].split("&")[0].split("~")[1]);

                        initial = random.nextInt(lowerBoundInitial, upperBoundInitial + 1);
                    } else {
                        initial = Integer.parseInt(fields.get(fieldNames.get(0)));
                    }
                    int scale;
                    int lowerBoundScale = Integer.parseInt(fieldName.split("scale=")[1].split("~")[0]);
                    int upperBoundScale = Integer.parseInt(fieldName.split("scale=")[1].split("_")[0].split("~")[1]);
                    scale = random.nextInt(lowerBoundScale, upperBoundScale + 1);
                    if (pos == 1) {
                        newVal = "" + initial;
                    } else {
                        newVal = "" + (initial + scale * (pos - 1));
                    }
                }
                case "string" -> {
                    ArrayList<String> potentialOptions = new ArrayList<>();
                    Collections.addAll(potentialOptions, fieldName.split(":")[1].split("_")[0].split(","));
                    newVal = potentialOptions.get(random.nextInt(0, potentialOptions.size()));
                }
                case "var" -> {
                    newVal = fieldName.split("=")[1].split("_")[0];
                }
            }

            fields.put(fieldName, newVal);
        }




    }


    public String withFieldsInserted() {
        String output = templateText;

        for (String field : fieldNames) {
            output = output.replaceAll(field, fields.get(field));
        }

        return output;
    }


    public String solve() {
        Object result;
        try {
            result = solver.invoke(null, fields, fieldNames);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Error attempting to solve question.");
            System.out.println("templateId: " + templateId);
            System.out.println("type: " + type);
            System.out.println("templateText: " + templateText);
            System.out.println("fields: " + fields);

            System.out.println("ERROR MESSAGE: " + e.getMessage());
            System.out.println("ERROR CAUSE: " + e.getCause());
            System.out.println("ERROR STACK TRACE: " + Arrays.toString(e.getStackTrace()));

            return "";
        }
        return (String) result;
    }


    public String genAltAnswer() {
        //randomizes fields and solves, then returns fields to previous state
        HashMap<String, String> oldFields = new HashMap<>(fields);

        this.randomizeFields();
        String altAnswer = solve();
        fields.clear();

        fields.putAll(oldFields);

        return altAnswer;
    }
}
