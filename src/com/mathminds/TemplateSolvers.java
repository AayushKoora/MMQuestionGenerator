package com.mathminds;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class TemplateSolvers {


    public ArrayList<Method> templateSolvingMethods;


    //populate the templateSolvingMethods array with Method objects for all of the methods in this class
    public TemplateSolvers() {
        templateSolvingMethods = new ArrayList<>();

        for (Method m : TemplateSolvers.class.getDeclaredMethods()) {
            if (!m.getName().equals("TemplateSolvers") && !m.getName().equals("retrieveMethod")) {
                templateSolvingMethods.add(m);
            }
        }
    }


    public Method retrieveMethod(String target) throws IllegalArgumentException {
        for (Method m : templateSolvingMethods) {
            if (m.getName().equals(target)) {
                return m;
            }
        }
        throw new IllegalArgumentException();
    }



    //question/template methods begin here!



    public static double placeholderSolve(HashMap<String, String> fields) {
        System.out.println("placeholdersolve running");
        //fields: "_int:1~5_", "_int:6~10_"
        double field1 = Double.parseDouble(fields.get("_int:1~5_"));
        double field2 = Double.parseDouble(fields.get("_int:6~10_"));
        return field1 + field2;
    }


    public static double otherPlaceholderSolve(HashMap<String, String> fields) {
        System.out.println("otherPlaceholderSolve running");
        //fields: "_int:1~5_", "_int:6~10_", "_double:-55.6~50.9_"
        double field1 = Double.parseDouble(fields.get("_int:1~5_"));
        double field2 = Double.parseDouble(fields.get("_int:6~10_"));
        double field3 = Double.parseDouble(fields.get("_double:-55.6~50.9_"));
        return field1 * field2 + field3;
    }
}
