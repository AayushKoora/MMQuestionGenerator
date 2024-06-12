package com.mathminds;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class AppInterface {


    int width;
    int height;

    JFrame frame;

    JPanel headerPanel;
    JPanel questionPanel;
    JPanel testPanel;

    JScrollPane questionScrollPane;
    JScrollPane templateScrollPane;

    ArrayList<TemplateQuestion> templateQuestions;



    public AppInterface() {
        width = Toolkit.getDefaultToolkit().getScreenSize().width;
        height = Toolkit.getDefaultToolkit().getScreenSize().height;

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

                templateQuestions.add(new TemplateQuestion(type, id, asText, fields, keyMethod));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(width, (int) (height * 0.1)));
        headerPanel.setBackground(Color.red);
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
    }

    private void createQuestionPanel() {
        questionPanel = new JPanel(new BorderLayout());
        questionPanel.setPreferredSize(new Dimension(width, (int) (height * 0.55)));
        questionPanel.setBackground(Color.GREEN);

        questionScrollPane = new JScrollPane(questionPanel);
        questionScrollPane.setPreferredSize(new Dimension((int) (width * 0.35), questionPanel.getHeight()));
        questionScrollPane.setBackground(Color.CYAN);

        templateScrollPane = new JScrollPane(questionPanel);
        templateScrollPane.setPreferredSize(new Dimension((int) (width * 0.35), questionPanel.getHeight()));
        templateScrollPane.setBackground(Color.MAGENTA);

        frame.getContentPane().add(questionPanel, BorderLayout.CENTER);
    }

    private void createTestPanel() {
        testPanel = new JPanel(new BorderLayout());
        testPanel.setPreferredSize(new Dimension(width, (int) (height * 0.35)));
        testPanel.setBackground(Color.BLUE);
        frame.getContentPane().add(testPanel, BorderLayout.SOUTH);
    }



    public static void main(String[] args) {
        System.out.println("booting first screen.");
        AppInterface screen = new AppInterface();
        screen.createHeaderPanel();
        screen.createQuestionPanel();
        screen.createTestPanel();

        //display window
        screen.frame.pack();
        screen.frame.setVisible(true);
    }
}
