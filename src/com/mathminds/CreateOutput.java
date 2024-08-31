package com.mathminds;


import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CreateOutput {

    public TestContainer test;

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

        Random random = new Random();

        for (int questionCounter = 1; questionCounter <= test.questions.size(); questionCounter++) {
            Question q = test.questions.get(questionCounter - 1);
            if (q.isTieBreaker) {
                continue;
            }

            output += "<h2>#" + questionCounter + ": </h2>";

            output += "<h3>" + q.withFieldsInserted() + "</h3>";

            //generate alternate answer choices as well as the real answer choice

            output += "<p>" + htmlGenerateOptionString(q) + "</p>";
            output += "<h1><br> </h1>";

            //spacing
            output += "<h1> <br> <br> <br> <br> </h1>";
        }

        output += "</body></html>";

        System.out.println(output);
        return output;
    }


    public void textFile() {
        System.out.println("Attempting to generate text file");
        String targetDestination = System.getProperty("user.home") + "/Documents/MathMindsOutput/";
        targetDestination += test.title + ".txt";
        try {
            File textfile = new File(targetDestination);
            if (textfile.createNewFile()) {
                System.out.println("File created: " + textfile.getName());
                FileWriter myWriter = new FileWriter(targetDestination);
                myWriter.write(string());
                myWriter.close();
            } else {
                System.out.println("Filename already in use.");
            }
        } catch (Exception e) {
            System.out.println("Error attempting to generate text file.");
            e.printStackTrace();
        }
    }


    public void notecardFile() {
        System.out.println("Attempting to generate Google Slide.");

        try {
            //make the directory to store output files if it does not already exist
            File newDir = new File(System.getProperty("user.home") + "/Documents/MathMindsOutput");
            System.out.println("Did create new directory: " + newDir.mkdir());

            //(72/25.4) * size in millimeters
            float width = 127f * 72f / 25.4f;
            float height = 76.2f * 72f / 25.4f;
            float margin = 2 * 72f / 25.4f;
            Rectangle rect = new Rectangle(width, height);

            //create pdf file
            Document doc = new Document(rect, margin, margin, margin, margin);
            String fileDestination = System.getProperty("user.home") + "/Documents/MathMindsOutput/";
            fileDestination += test.title + ".pdf";
            PdfWriter.getInstance(doc, new FileOutputStream(fileDestination));

            //write to pdf file
            doc.open();
            HTMLWorker titleHTMLWorker = new HTMLWorker(doc);
            String titlePageString = "<h4>MathMinds Test</h4>";
            titlePageString += "<br>";
            titlePageString += "<h1>" + test.title + "</h1>";
            titleHTMLWorker.parse(new StringReader(titlePageString));

            int questionCounter = 1;
            for (Question q : test.questions) {
                if (q.isTieBreaker) {
                    continue;
                }
                doc.newPage();
                String asHtml = questionAsNotecardHTML(q, questionCounter);
                HTMLWorker htmlWorker = new HTMLWorker(doc);
                htmlWorker.parse(new StringReader(asHtml));
                questionCounter++;
            }

            doc.close();

            System.out.println("PDF created successfully.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public String questionAsNotecardHTML(Question question, int questionNum) {
        String output = "";
        output += "<h1>#" + questionNum + ": </h1>";
        output += "<h3>" + question.withFieldsInserted() + "<h3>";
        output += "<p>" + htmlGenerateOptionString(question) + "</p>";

        return output;
    }


    public String htmlGenerateOptionString(Question q) {
        String optionsString = "";
        Random random = new Random();

        //determine total answer choices
        int totalAnswerChoices = 0;
        try {
            for (String fieldName : q.fieldNames) {
                if (fieldName.contains("_var:multiplechoice=")) {
                    totalAnswerChoices = Integer.parseInt(fieldName.split("_var:multiplechoice=")[1].split("_")[0]);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Encountered error when attempting to read total answer choices field variables");
        }


        String answer = q.solve();

        if (totalAnswerChoices > 0) { //if question is configured for multiple answer choices
            int realAnswer = random.nextInt(1, totalAnswerChoices + 1);
            ArrayList<String> answerChoices = new ArrayList<>();

            for (int i = 1; i <= totalAnswerChoices; i++) {
                if (i == realAnswer && !answerChoices.contains(answer)) {
                    answerChoices.add(answer);
                } else {
                    boolean hasGeneratedUniqueAnswer = false;
                    while (!hasGeneratedUniqueAnswer) {
                        String newAltAnswer = q.genAltAnswer();
                        if (!answerChoices.contains(newAltAnswer)) {
                            answerChoices.add(newAltAnswer);
                            hasGeneratedUniqueAnswer = true;
                        }
                    }
                }
            }

            for (int optionCount = 0; optionCount < answerChoices.size(); optionCount++) {
                String answerChoice = "" + answerChoices.get(optionCount);
                optionsString += "<br>";
                optionsString += ("" + (char) ('A' + optionCount));
                optionsString += ": " + answerChoice;
            }
        } else {
            optionsString += "<br>___________";
        }

        return optionsString;
    }


    public String string() {
        System.out.println("Attempting to generate a String with object: " + test);

        String output = "";

        output += "title:" + test.title;
        output += "&qnum:" + test.questionCount;

        for (Question q : test.questions) {
            String answer = q.solve();
            output += "question:text=" + q.withFieldsInserted() + "_tiebreaker=" + q.isTieBreaker + "_answer=" + answer + "_altanswers=";
            boolean multipleChoice = false;
            int multipleChoiceCount = 0;
            for (String fieldName : q.fieldNames) { //do multiple choice
                if (fieldName.contains("_var:multiplechoice=")) {
                    multipleChoice = true;
                    multipleChoiceCount = Integer.parseInt(q.fields.get(fieldName));
                }
            }
            if (multipleChoice) {
                Set<String> altAnswers = new HashSet<>();
                while (altAnswers.size() < multipleChoiceCount - 1) {
                    System.out.println(q.withFieldsInserted());
                    String possibleAnswer = q.genAltAnswer();
                    if (!answer.equals(possibleAnswer)) {
                        altAnswers.add(possibleAnswer);
                    } else {
                        System.out.println(possibleAnswer + " " + answer);
                    }
                }
                for (String option : altAnswers) {
                    output += option + "&";
                }
                output = output.substring(0, output.length() - 1);
            } else {
                output += "fillintheblank";
            }

        }

        return output;
    }
}
