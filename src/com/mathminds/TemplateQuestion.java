package com.mathminds;

import java.util.ArrayList;

public class TemplateQuestion {

    public String type;
    public String id;
    public String asText;
    public ArrayList<String> fields;
    public String keyMethod;

    public TemplateQuestion(String type, String id, String asText, ArrayList<String> fields, String keyMethod) {
        this.type = type;
        this.id = id;
        this.asText = asText;
        this.fields = fields;
        this.keyMethod = keyMethod;
    }
}
