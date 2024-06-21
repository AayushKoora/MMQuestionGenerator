package com.mathminds;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class AppInterface {


    int width;
    int height;

    Color bgColor;

    JFrame frame;
    JPanel appPanel;
    JButton genTest;
    JTextField testNameField;

    ArrayList<TemplateQuestion> templateQuestions;


    public static final int maxQuestionCount = 20;
    public static final boolean allQuestionsUniqueType = true;
    public static final int doubleFieldsDecimalPlaces = 2;



    public AppInterface() {
        width = Toolkit.getDefaultToolkit().getScreenSize().width;
        height = Toolkit.getDefaultToolkit().getScreenSize().height;

        bgColor = new Color(200, 200, 200);


        frame = new JFrame("TopLevelDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSONParser parser = new JSONParser();

        try {
            JSONObject obj;
            try {
                obj = (JSONObject) parser.parse(new FileReader("src/res/questiontemplates.json"));
            } catch (FileNotFoundException e) {
                //this is not being ran in a ide environment, try to find the file in the current directory instead.
                try {
                    obj = (JSONObject) parser.parse(new FileReader("questiontemplates.json"));
                } catch (FileNotFoundException e2) {
                    //file is not where it is supposed to be, tell the user they messed up and crash the app.
                    System.out.println("*** File questiontemplates.json not found in current directory or in (ide) res/questiontemplates.json");
                    System.out.println("*** Error 1: " + e);
                    System.out.println("*** Error 2: " + e2);
                    System.out.println("Exiting...");
                    System.exit(0);
                    obj = null; //just to clear up ide errors
                }
            }

            JSONArray arr = (JSONArray) obj.get("templates");
            Iterator<JSONObject> iterator = arr.iterator();

            templateQuestions = new ArrayList<>();
            while (iterator.hasNext()) {
                JSONObject templateVal = iterator.next();
                String type = "" + templateVal.get("type");
                String id = "" + templateVal.get("id");
                String asText = "" + templateVal.get("astext");
                String keyMethod = "" + templateVal.get("keymethod");

                JSONArray fieldArr = (JSONArray) templateVal.get("fields");
                ArrayList<String> fields = new ArrayList<>();
                for (Object field : fieldArr) {
                    fields.add((String) field);
                }

                templateQuestions.add(new TemplateQuestion(type, Integer.parseInt(id), asText, fields, keyMethod));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }



    private void buildInterface() {
        appPanel = new JPanel(new GridBagLayout());
        appPanel.setBackground(bgColor);
        frame.getContentPane().add(appPanel, BorderLayout.CENTER);

        testNameField = new JTextField(20);
        testNameField.setToolTipText("Set resulting test name");
        appPanel.add(testNameField);

        genTest = new JButton();
        genTest.addActionListener(e -> {
            //button that happens when you generate a test
            TestContainer test = genTest(testNameField.getText());
            CreateOutput createOutput = new CreateOutput(test);
            createOutput.pdf();
            System.out.println(createOutput.string());
        });
        genTest.setText("Generate Test");
        appPanel.add(genTest);
    }




    private TestContainer genTest(String testTitle) {
        System.out.println("Generating test with title: " + testTitle);
        TestContainer test = new TestContainer(testTitle, Math.min(uniqueTypeCount(templateQuestions), maxQuestionCount), allQuestionsUniqueType, templateQuestions);
        test.populateQuestions();
        test.randomizeQuestionFields();
        return test;
    }


    private int uniqueTypeCount(ArrayList<TemplateQuestion> templateQuestions) {
        ArrayList<String> containedTypes = new ArrayList<>();
        for (TemplateQuestion q : templateQuestions) {
            if (!containedTypes.contains(q.type)) {
                containedTypes.add(q.type);
            }
        }
        return containedTypes.size();
    }



    public static void main(String[] args) {
        System.out.println("booting first screen.");
        AppInterface screen = new AppInterface();
        screen.buildInterface();

        //display window
        screen.frame.pack();
        screen.frame.setVisible(true);
    }
}
