package com.mathminds;

public class CreateOutput {

    public TestContainer test;

    public CreateOutput(TestContainer test) {
        this.test = test;
    }


    public void pdf() {
        //code to generate a pdf file and save it to the user's device
        System.out.println("Attempting to generate a pdf with object: " + test);
    }


    public String string() {
        System.out.println("Attempting to generate a String with object: " + test);

        String output = "";

        output += "title:" + test.title;
        output += "|qnum:" + test.questionCount;

        for (Question q : test.questions) {
            output += "|";
            output += "question:type=" + q.type + ",id=" + q.templateId + ",text=" + q.withFieldsInserted() + ",answer=" + q.solve();
        }

        return output;
    }
}
