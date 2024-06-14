package com.mathminds;


import com.lowagie.text.Document;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

public class CreateOutput {

    public TestContainer test;

    public static final int totalAnswerChoices = 5;

    public CreateOutput(TestContainer test) {
        this.test = test;
    }


    public void pdf() {
        //code to generate a pdf file and save it to the user's device
        System.out.println("Attempting to generate a pdf with object: " + test);

        try {
            //make the directory to store output files if it does not already exist
            File newDir = new File(System.getProperty("user.home") + "/Documents/MathMindsOutput");
            System.out.println("Did create new directory: " + newDir.mkdir());

            //create the pdf file
            Document document = new Document();
            String fileDestination = System.getProperty("user.home") + "/Documents/MathMindsOutput/";
            fileDestination += test.title + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileDestination));

            //write to the pdf file
            document.open();
            String htmlString = this.html();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(htmlString));
            document.close();

            System.out.println("PDF created successfully.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }



    public String html() {
        String output = "<html><body>";

        output += "<h1>" + test.title + "</h1>";

        output += "<h1> <br> <br> <br> <br> </h1>";

        output += "<p>Following are " + test.questions.size() + " questions. Enjoy.</p>";

        output += "<h1> <br> <br> <br> <br> </h1>";

        Random random = new Random();

        for (int questionCounter = 1; questionCounter <= test.questions.size(); questionCounter++) {
            Question q = test.questions.get(questionCounter - 1);
            output += "<h2>#" + questionCounter + ": " + q.type + "</h2>";

            output += "<h3>" + q.withFieldsInserted() + "</h3>";

            //generate alternate answer choices as well as the real answer choice

            String optionsString = "";


            int realAnswer = random.nextInt(1, totalAnswerChoices + 1);
            double answer = q.solve();
            ArrayList<Double> answerChoices = new ArrayList<>();

            for (int i = 1; i <= totalAnswerChoices; i++) {

                if (i == realAnswer && !answerChoices.contains(answer)) {
                    answerChoices.add(answer);
                } else {
                    boolean hasGeneratedUniqueAnswer = false;
                    while (!hasGeneratedUniqueAnswer) {
                        double newAltAnswer = q.genAltAnswer();
                        if (!answerChoices.contains(newAltAnswer)) {
                            answerChoices.add(newAltAnswer);
                            hasGeneratedUniqueAnswer = true;
                        }
                    }
                }
            }

            for (int optionCount = 0; optionCount < answerChoices.size(); optionCount++) {
                String answerChoice = "" + answerChoices.get(optionCount);
                optionsString += ("" + (char) ('A' + optionCount));
                optionsString += ": " + answerChoice + "<br>";
            }

            output += "<p>" + optionsString + "</p>";
        }

        output += "</body></html>";
        return output;
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
