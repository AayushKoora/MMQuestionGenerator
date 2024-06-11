package com.mathminds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Question {

    public int templateId = -1;
    public String type;
    public String templateText;
    public ArrayList<String> fieldNames;
    public HashMap<String, Double> fields;
    public Method solver;


    public Question(int id, String type, String templateText, ArrayList<String> fieldNames, HashMap<String, Double> fields, Method solver) {
        this.templateId = id;
        this.type = type;
        this.templateText = templateText;
        this.fieldNames = fieldNames;
        this.fields = fields;
        this.solver = solver;
    }


    public void randomizeFields() {
        for (Map.Entry<String, Double> entry : fields.entrySet()) {

            String type = entry.getKey().split("_")[1].split(":")[0];
            double lowerBound = Double.parseDouble(entry.getKey().split(":")[1].split("-")[0]);
            double upperBound = Double.parseDouble(entry.getKey().split("-")[1].split("_")[0]);

            Random random = new Random();
            double newVal = switch (type) {
                case "int" -> random.nextInt((int) lowerBound, (int) upperBound);
                case "double" -> random.nextDouble(lowerBound, upperBound);
                default -> -1.0;
            };

            fields.put(entry.getKey(), newVal);
        }
    }


    public String withFieldsInserted() {
        String output = templateText;

        for (String field : fieldNames) {
            output = output.replace(field, ""  + fields.get(field));
        }

        return output;
    }


    public double solve() {
        try {
            return (double) solver.invoke(null, fields);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Error attempting to solve question.");
            System.out.println("templateId: " + templateId);
            System.out.println("type: " + type);
            System.out.println("templateText: " + templateText);
            System.out.println("fields: " + fields);

            System.out.println("ERROR MESSAGE: " + e.getMessage());
            System.out.println("ERROR CAUSE: " + e.getCause());
            System.out.println("ERROR STACK TRACE: " + e.getStackTrace());

            return -1;
        }

    }
}
