package com.mathminds;

import java.util.ArrayList;

public class TemplateQuestion {

    public String type;
    public int id;
    public String asText;
    public ArrayList<String> fields;
    public String keyMethod;

    public static ArrayList<String> allValidTypes = new ArrayList<>();

    public TemplateQuestion(String type, int id, String asText, ArrayList<String> fields, String keyMethod) {
        this.type = type;
        this.id = id;
        this.asText = asText;
        this.fields = fields;
        this.keyMethod = keyMethod;

        if (allValidTypes.contains(type)) {
            allValidTypes.add(type);
        }
    }
}
