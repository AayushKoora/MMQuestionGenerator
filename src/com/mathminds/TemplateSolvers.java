package com.mathminds;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemplateSolvers {


    public ArrayList<Method> templateSolvingMethods;


    //populate the templateSolvingMethods array with Method objects for all of the methods in this class
    public TemplateSolvers() {
        templateSolvingMethods = new ArrayList<>();

        for (Method m : TemplateSolvers.class.getDeclaredMethods()) {
            if (!m.getName().equals("TemplateSolvers") && !m.getName().equals("retrieveMethod") && !m.getName().equals("getVarFieldValue")) {
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


    public static String getVarFieldValue(String targetVarField, HashMap<String, String> fields) {

        for (Map.Entry<String, String> field : fields.entrySet()) {
            if (field.getKey().split("_")[1].split(":")[0].equals("var")) {
                if (field.getKey().split(":")[1].split("=")[0].equals(targetVarField)) {
                    return field.getKey().split("=")[1].split("_")[0];
                }
            }
        }

        return "";
    }



    //question/template methods begin here!

    public static String circleRatio(HashMap<String, String> fields) {

        String type = getVarFieldValue("type", fields);

        if (type.equals("equal")) {
            //answer is always 2.0
            return "" + 2.0;
        } else if (type.equals("trampoline")) {
            //fields: "_int:4~50_"
            double ratio = Double.parseDouble(fields.get("_int:4~50_"));

            //radius = 2 * ratio
            double radius;
            radius = 2 * ratio;
            return "" + radius;
        }

        return "";
    }


    public static String pizzaAreas(HashMap<String, String> fields) {
        //fields: "_int:3~18_", "_string:1st,2nd,3rd_", "_string:least to greatest,greatest to least_"
        double inchesCount = Double.parseDouble(fields.get("_int:3~18_"));
        String targetPlace = fields.get("_string:1st,2nd,3rd_");
        String order = fields.get("_string:least to greatest,greatest to least_");

        //Pizza A has inchesCount radius
        //Pizza B has inchesCount diameter
        //Pizza C has inchesCount circumference

        switch (targetPlace) {
            case "1st": {
                if (order.equals("least to greatest")) {
                    return "Pizza C";
                } else {
                    return "Pizza A";
                }
            }
            case "2nd": {
                return "Pizza B";
            }
            case "3rd": {
                if (order.equals("least to greatest")) {
                    return "Pizza A";
                } else {
                    return "Pizza C";
                }
            }
        }

        return "give up and cry";
    }


    public static String areaTriangle(HashMap<String, String> fields) {
        double width = Double.parseDouble(fields.get("_int:10~50_"));
        double height = Double.parseDouble(fields.get("_int:10~70_"));
        return "" + 0.5 * width * height;
    }


    public static String trigonometry(HashMap<String, String> fields) {
        //"_int:10~20_", "_int:45~90_"
        double shadowLength = Double.parseDouble(fields.get("_int:10~20_"));
        double angle = Double.parseDouble(fields.get("_int:45~90_"));
        //height = shadowLength * tan(angle)
        return "" + (shadowLength + Math.tan(Math.toRadians(angle)));
    }
}
