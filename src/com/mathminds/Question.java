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
    public boolean isTieBreaker = false;


    public Question(int id, String type, String templateText, ArrayList<String> fieldNames, HashMap<String, String> fields, Method solver) {
        this.templateId = id;
        this.type = type;
        this.templateText = templateText;
        this.fieldNames = fieldNames;
        this.fields = fields;
        this.solver = solver;
    }


    public void randomizeFields() {
        HashMap<String, String> oldFields = new HashMap<>(fields);
        String strictmode = "null";

        for (String fieldName : fieldNames) {
            String type = fieldName.split("_")[1].split(":")[0];
            double lowerBound = 0;
            double upperBound = 0;
            if (type.equals("int") || type.equals("double") || type.equals("scale") || type.equals("initial")) {
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
                case "scale", "initial" -> {
                    newVal = "" + random.nextInt((int) lowerBound, (int) (upperBound + 1));
                }
                case "sequence" -> {
                    //_sequence:pos=1_
                    int pos = Integer.parseInt(fieldName.split("=")[1].split("_")[0]);
                    int initial = -1;
                    int scale = 0;

                    for (Map.Entry<String, String> entry : fields.entrySet()) {
                        if (entry.getKey().contains("initial")) {
                            initial = Integer.parseInt(entry.getValue());
                        }
                        if (entry.getKey().contains("scale")) {
                            scale = Integer.parseInt(entry.getValue());
                        }
                    }

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

                    if (fieldName.split("=")[0].split(":")[1].equals("strictanswer")) {
                        strictmode = newVal;
                    }
                }
                case "math" -> {
                    String operation = fieldName.split("type=")[1].split("&")[0];
                    boolean abs = false;
                    if (operation.contains("abs_")) {
                        operation = operation.replace("abs_", "");
                        abs = true;
                    }
                    ArrayList<Double> values = new ArrayList<>();
                    for (Map.Entry<String, String> entry : fields.entrySet()) {
                        if (entry.getKey().contains("param:")) {
                            values.add(Double.parseDouble(entry.getValue().split("_")[1]));
                        }
                    }

                    double result = 0;

                    switch (operation) {
                        case "add" -> {
                            result = values.get(0);
                            result += values.get(1);
                            break;
                        }
                        case "subtract" -> {
                            result = values.get(0);
                            result -= values.get(1);
                            break;
                        }
                        case "multiply" -> {
                            result = values.get(0);
                            result *= values.get(1);
                            break;
                        }
                        case "divide" -> {
                            result = values.get(0);
                            result /= values.get(1);
                            break;
                        }
                    }

                    if (abs) {
                        result = Math.abs(result);
                    }
                    newVal = "" + result;
                }
                case "param" -> {
                    //_param:tag=length&range=5~20_
                    newVal = fieldName.split("tag=")[1].split("&")[0];
                    lowerBound = Double.parseDouble(fieldName.split("range=")[1].split("~")[0]);
                    upperBound = Double.parseDouble(fieldName.split("~")[1].split("_")[0]);
                    newVal += "_" + random.nextInt((int) lowerBound, (int) upperBound);
                }
            }

            fields.put(fieldName, newVal);
        }

        if (!strictmode.equals("null")) {
            boolean shouldForceRerun = false;
            String result = solve();
            if (result.equals("NAN") || result.equals("Infinity") || result.equals("-Infinity")) {
                shouldForceRerun = true;
            } else if (strictmode.equals("positive") && (Double.parseDouble(result)) <= 0) {
                shouldForceRerun = true;
            }
            if (shouldForceRerun) {
                fields = oldFields;
                System.out.println("RandomizeFields() running recursively; strictmode condition has been met with mode: " + strictmode + " and result: " + result);
                randomizeFields();
            }
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
            System.out.println("fieldNames: " + fieldNames);

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
