package com.mathminds;


import com.lowagie.text.Document;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

public class CreateOutput {

    public TestContainer test;

    public CreateOutput(TestContainer test) {
        this.test = test;
    }


    public void pdf() {
        //code to generate a pdf file and save it to the user's device
        System.out.println("Attempting to generate a pdf with object: " + test);

        try {
            File newDir = new File(System.getProperty("user.home") + "/Documents/MathMindsOutput");
            newDir.mkdir();


            Document document = new Document();
            String fileDestination = System.getProperty("user.home") + "/Documents/MathMindsOutput/";
            fileDestination += test.title += ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileDestination));

            document.open();
            String htmlString = "<html><body> This is my Project </body></html>";
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(htmlString));
            document.close();

            System.out.println("PDF created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
