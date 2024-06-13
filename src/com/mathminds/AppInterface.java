package com.mathminds;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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



    public AppInterface() {
        width = Toolkit.getDefaultToolkit().getScreenSize().width;
        height = Toolkit.getDefaultToolkit().getScreenSize().height;

        bgColor = new Color(200, 200, 200);


        frame = new JFrame("TopLevelDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSONParser parser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader("res/questiontemplates.json"));
            JSONArray arr = (JSONArray) obj.get("templates");
            Iterator<JSONObject> iterator = arr.iterator();

            templateQuestions = new ArrayList<>();
            while (iterator.hasNext()) {
                JSONObject templateVal = iterator.next();
                String type = "" + templateVal.get("type");
                String id = "" + templateVal.get("id");
                String asText = "" + templateVal.get("astext");
                String keyMethod = "" + templateVal.get("keymethod");

                ArrayList<String> fields = new ArrayList<>();
                JSONArray fieldArr = (JSONArray) templateVal.get("fields");
                fields.addAll(fieldArr);

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
        TestContainer test = new TestContainer(testTitle, Math.min(templateQuestions.size(), 20), true, templateQuestions);
        test.populateQuestions();
        test.randomizeQuestionFields();
        return test;
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
