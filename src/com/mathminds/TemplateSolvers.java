package com.mathminds;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TemplateSolvers {


    public ArrayList<Method> templateSolvingMethods;


    //populate the templateSolvingMethods array with Method objects for all of the methods in this class
    public TemplateSolvers() {
        templateSolvingMethods = new ArrayList<>();

        for (Method m : TemplateSolvers.class.getDeclaredMethods()) {
            if (!m.getName().equals("TemplateSolvers") && !m.getName().equals("retrieveMethod") && !m.getName().equals("getVarFieldValue") && !m.getName().equals("round")) {
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


    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round((Double) value * scale) / scale;
    }



    //question/template methods begin here!

    public static String none(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        return "Solution is not calculated. In fact, you shouldn't be seeing this!";
    }


    public static String circleRatio(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        String type = getVarFieldValue("type", fields);

        if (type.equals("equal")) {
            //answer is always 2.0
            return "" + 2.0;
        } else if (type.equals("trampoline")) {
            //fields: "_int:4~50_"
            double ratio = Double.parseDouble(fields.get(fieldNames.get(0)));

            //radius = 2 * ratio
            double radius;
            radius = 2 * ratio;
            return "" + radius;
        }

        return "NO APPLICABLE TYPE VAR IN SOLVER";
    }


    public static String pizzaAreas(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        //fields: "_int:3~18_", "_string:1st,2nd,3rd_", "_string:least to greatest,greatest to least_"
        double inchesCountRadius = Double.parseDouble(fields.get(fieldNames.get(0)));
        double inchesCountDiameter = Double.parseDouble(fields.get(fieldNames.get(1)));
        double inchesCountCircumference = Double.parseDouble(fields.get(fieldNames.get(2)));
        String targetPlace = fields.get(fieldNames.get(3));
        String order = fields.get(fieldNames.get(4));

        ArrayList<Double> inches = new ArrayList<>();

        inches.add(inchesCountRadius);
        inches.add(inchesCountDiameter / 2);
        inches.add(inchesCountCircumference / Math.PI / 2.0);

        Collections.sort(inches);
        if (order.equals("greatest to least")) {
            Collections.reverse(inches);
        }

        double target = 0;
        switch (targetPlace) {
            case "1st": {
                target = inches.get(0);
            }
            case "2nd": {
                target = inches.get(1);
            }
            case "3rd": {
                target = inches.get(2);
            }
        }

        if (target == inchesCountRadius) {
            return "Pizza A";
        }
        if (target == inchesCountDiameter / 2) {
            return "Pizza B";
        }
        if (target == inchesCountCircumference / Math.PI / 2.0) {
            return "Pizza C";
        }

        return "NO APPLICABLE TYPE VAR IN SOLVER";
    }


    public static String areaTriangle(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        double width = Double.parseDouble(fields.get(fieldNames.get(0)));
        double height = Double.parseDouble(fields.get(fieldNames.get(1)));
        return "" + 0.5 * width * height;
    }


    public static String trigonometry(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        //"_int:10~20_", "_int:45~90_"
        double shadowLength = Double.parseDouble(fields.get(fieldNames.get(0)));
        double angle = 45;
        if (fieldNames.size() > 1) {
            angle = Double.parseDouble(fields.get(fieldNames.get(1)));
        }
        //height = shadowLength * tan(angle)
        return "" + round(shadowLength + Math.tan(Math.toRadians(angle)), AppInterface.doubleFieldsDecimalPlaces);
    }

/*
    public static String equalLegsTri(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        //"_int:10~20_", "_int:45~90_"
        double hypoLength = Double.parseDouble(fields.get("_int:10~20_"));
        //hypo = leg * sqrt(2)
        return hypoLength + " / √2";
    }
 */

    public static String runDist(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        //"_int:4~10_", "_int:2~5_"
        double dist = Double.parseDouble(fields.get(fieldNames.get(0)));
        double weeks = Double.parseDouble(fields.get(fieldNames.get(1)));
        return "" + (weeks * 7 * dist);
    }


    public static String bankNegative(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        double val1 = Double.parseDouble(fields.get(fieldNames.get(0)));
        double val2 = Double.parseDouble(fields.get(fieldNames.get(1)));
        double val3 = Double.parseDouble(fields.get(fieldNames.get(2)));

        return "" + (val1 - val2 + val3);
    }


    public static String moneyCompared(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        double multFactor = Double.parseDouble(fields.get(fieldNames.get(0)));
        double extraBal = Double.parseDouble(fields.get(fieldNames.get(1)));
        double totalBal = Double.parseDouble(fields.get(fieldNames.get(2)));

        //multFactor * x + extraBal = totalBal
        return "" + round((totalBal - extraBal) / multFactor, 2);
    }


    public static String multiply(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        double val1 = Double.parseDouble(fields.get(fieldNames.get(0)));
        double val2 = Double.parseDouble(fields.get(fieldNames.get(1)));

        return "" + (val1 * val2);
    }


    public static String linEq(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        //"_int:20~80_", "_double:1.25~2.75_", "_double:0.1~0.99_", "_double:80.0~120.0_", "_var:type=x_"
        double totalCount = Double.parseDouble(fields.get(fieldNames.get(0)));
        double xCost = Double.parseDouble(fields.get(fieldNames.get(1)));
        double yCost = Double.parseDouble(fields.get(fieldNames.get(2)));
        double totalCost = Double.parseDouble(fields.get(fieldNames.get(3)));
        String type = fields.get(fieldNames.get(4));

        if (type.equals("y")) {
            //xCost * x + yCost * y = totalCost
            //x + y = totalCount

            //xCost * (totalCount - y) + yCost * y = totalCost
            //xCost * totalCount - xCost * y + yCost * y = totalCost
            //totalCost - xCost * totalCount = y * (-xCost + yCost)
            //( totalCost - xCost * totalCount ) / ( -PencilCost + yCost )
            return "" + Math.floor( (totalCost - xCost * totalCount) / (-1 * xCost + yCost) );
        } else if (type.equals("x")) {
            //xCost * x + yCost * y = totalCost
            //x + y = totalCount

            //y = totalCount - x;
            //xCost * x + yCost * totalCount - yCost * x = totalCost
            //xCost * x - yCost * x = totalCost - yCost * totalCount
            //x * (xCost - yCost) = totalCost - yCost * totalCount
            //x = (totalCost - yCost * totalCount) / (xCost - yCost)
            return "" + Math.floor( (totalCost - yCost * totalCount) / (xCost - yCost));
        } else {
            return "NO APPLICABLE TYPE VAR IN SOLVER";
        }

    }


    public static String arithSequence(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        int val1 = Integer.parseInt(fields.get(fieldNames.get(2)));
        int val2 = Integer.parseInt(fields.get(fieldNames.get(3)));
        int val3 = Integer.parseInt(fields.get(fieldNames.get(4)));

        int scale = val2 - val1;

        int totalDays = Integer.parseInt(fields.get(fieldNames.get(5)));

        double sum = 0;

        for (int i = 1; i < totalDays; i++) {
            sum += (val1 + scale * (i - 1));
        }

        return "" + sum;
    }


    public static String plotDimensions(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        double side1 = Double.parseDouble(fields.get(fieldNames.get(0)).split("_")[1]);
        double side2 = Double.parseDouble(fields.get(fieldNames.get(1)).split("_")[1]);
        return "" + side1 + " | " + side2;
    }


    public static String sphere(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        //"_int:20~80_", "_double:1.25~2.75_", "_double:0.1~0.99_", "_double:80.0~120.0_", "_var:type=x_"
        double num = Double.parseDouble(fields.get(fieldNames.get(0)));
        String type = fields.get(fieldNames.get(1));

        if (type.equals("surfacearea")) {
            return "" + round(4 * Math.pow(num, 2), AppInterface.doubleFieldsDecimalPlaces) + "π";
        } else if (type.equals("volume")) {
            return "4/3 * " + round(Math.pow(num/2, 3), AppInterface.doubleFieldsDecimalPlaces) + "π";
        } else {
            return "NO APPLICABLE TYPE VAR IN SOLVER";
        }
    }


    public static String cube(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        //"_int:20~80_", "_double:1.25~2.75_", "_double:0.1~0.99_", "_double:80.0~120.0_", "_var:type=x_"
        double num = Double.parseDouble(fields.get(fieldNames.get(0)));
        String type = fields.get(fieldNames.get(1));


        if (type.equals("side")) {
            return "" + round(Math.cbrt(num), AppInterface.doubleFieldsDecimalPlaces);
        } else if (type.equals("surfacearea")) {
            return "" + round(6 * Math.pow(num, 2), AppInterface.doubleFieldsDecimalPlaces);
        } else {
            return "NO APPLICABLE TYPE VAR IN SOLVER";
        }
    }


    public static String cylinder(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        //"_int:20~80_", "_double:1.25~2.75_", "_double:0.1~0.99_", "_double:80.0~120.0_", "_var:type=x_"
        String isDiameter = fields.get(fieldNames.get(0));
        double num1 = Double.parseDouble(fields.get(fieldNames.get(1)));
        double height = Double.parseDouble(fields.get(fieldNames.get(2)));
        String type = fields.get(fieldNames.get(3));
        double radius = (isDiameter.equals("diameter")) ? num1 / 2 : num1;

        if (type.equals("volume")) { //pi * r^2 * h
            return round(Math.pow(radius, 2) * height, AppInterface.doubleFieldsDecimalPlaces) + "π";
        } else if (type.equals("latsurfarea")) { //2pi * r * h
            return round(2 * radius * height, AppInterface.doubleFieldsDecimalPlaces) + "π";
        } else {
            return "NO APPLICABLE TYPE VAR IN SOLVER";
        }
    }


    public static String pyramid(HashMap<String, String> fields, ArrayList<String> fieldNames) {

        //"_int:20~80_", "_double:1.25~2.75_", "_double:0.1~0.99_", "_double:80.0~120.0_", "_var:type=x_"
        double num1 = Double.parseDouble(fields.get(fieldNames.get(0)));
        double num2 = Double.parseDouble(fields.get(fieldNames.get(1)));
        String type = fields.get(fieldNames.get(2));

        System.out.println("type: " + type);


        if (type.equals("volumetripyramid")) {
            return "" + round((1.0/3.0) * num1 * num2, AppInterface.doubleFieldsDecimalPlaces);
        } else if (type.equals("surfareasquarepyramid")) {
            double B = Math.pow(num1, 2);
            double p = num1 * 4;
            return "" + round(B + 12 * p * num2, AppInterface.doubleFieldsDecimalPlaces);
        } else {
            return "NO APPLICABLE TYPE VAR IN SOLVER";
        }
    }


    public static String complexNumbers(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        String type = fields.get(fieldNames.get(0));

        if (type.equals("simplify")) {
            double num1 = Double.parseDouble(fields.get(fieldNames.get(1)));
            double num2 = Double.parseDouble(fields.get(fieldNames.get(2)));
            double num3 = Double.parseDouble(fields.get(fieldNames.get(3)));
            double num4 = Double.parseDouble(fields.get(fieldNames.get(4)));
            double frontNum = num1 * num3 - num2 * num4;
            double backNum = num2 * num3 - num1 * num4;
            return frontNum + " + " + backNum + "i";
        }

        double num = Double.parseDouble(fields.get(fieldNames.get(1)));

        if (num % 4 == 0) {
            return "1";
        }
        if (num % 3 == 0) {
            return "-i";
        }
        if (num % 2 == 0) {
            return "-1";
        }
        return "i";
    }


    public static String conferenceTickets(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        double num1 = Double.parseDouble(fields.get(fieldNames.get(0)));
        double num2 = Double.parseDouble(fields.get(fieldNames.get(1)));
        double num3 = Double.parseDouble(fields.get(fieldNames.get(2)));

        return "" + (num1 * num2 + num2 * num3);
    }


    public static String submarineShark(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        double initialDepth = Double.parseDouble(fields.get(fieldNames.get(0)));
        double lowerRate = Double.parseDouble(fields.get(fieldNames.get(1)));
        double lowerTime = Double.parseDouble(fields.get(fieldNames.get(2)));
        double riseRate = Double.parseDouble(fields.get(fieldNames.get(3)));
        double riseTime = Double.parseDouble(fields.get(fieldNames.get(4)));

        return "" + (initialDepth - (lowerRate * lowerTime) + (riseRate * riseTime));
    }


    public static String sumCalcTriSides(HashMap<String, String> fields, ArrayList<String> fieldNames) {
        String type = fields.get(fieldNames.get(0));
        double initialLength = Double.parseDouble(fields.get(fieldNames.get(1)));

        if (type.equals("30")) {
            return "" + ((initialLength * 2) + (initialLength * Math.sqrt(3)));
        }
        if (type.equals("60")) {
            return "" + ((initialLength / Math.sqrt(3)) * 3);
        }
        return "" + (initialLength/2 + (initialLength/2 * Math.sqrt(3)));
    }
}
