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
        for (Map.Entry<String, String> entry : fields.entrySet()) {

            String type = entry.getKey().split("_")[1].split(":")[0];
            double lowerBound = 0;
            double upperBound = 0;
            if (type.equals("int") || type.equals("double")) {
                lowerBound = Double.parseDouble(entry.getKey().split(":")[1].split("~")[0]);
                upperBound = Double.parseDouble(entry.getKey().split("~")[1].split("_")[0]);
            }

            Random random = new Random();

            String newVal = "";
            switch (type) {
                case "int" -> newVal = "" + random.nextInt((int) lowerBound, (int) upperBound);
                case "double" -> {
                    double tempVal = random.nextDouble(lowerBound, upperBound);
                    double scale = Math.pow(10, AppInterface.doubleFieldsDecimalPlaces);
                    newVal = "" + Math.round(tempVal * scale) / scale;
                }
                case "string" -> {
                    ArrayList<String> potentialOptions = new ArrayList<>();
                    Collections.addAll(potentialOptions, entry.getKey().split(":")[1].split("_")[0].split(","));
                    newVal = potentialOptions.get(random.nextInt(0, potentialOptions.size()));
                }
            }

            fields.put(entry.getKey(), newVal);
        }
    }


    public String withFieldsInserted() {
        String output = templateText;

        for (String field : fieldNames) {
            System.out.println(fields.get(field));
            output = output.replaceAll(field, fields.get(field));
        }

        return output;
    }


    public String solve() {
        Object result;
        try {
            result = solver.invoke(null, fields);
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
