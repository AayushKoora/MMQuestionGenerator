package com.mathminds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Question {

    public int templateId = -1;
    public String type;
    public String templateText;
    public ArrayList<Double> fields;
    public Method solver;


    public Question(int id, String type, String templateText, ArrayList<Double> fields, Method solver) {
        this.templateId = id;
        this.type = type;
        this.templateText = templateText;
        this.fields = fields;
        this.solver = solver;
    }


    public String withFieldsInserted() {
        String output = templateText;

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
