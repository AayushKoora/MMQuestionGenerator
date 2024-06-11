package com.mathminds;

import javax.swing.*;
import java.awt.*;

public class AppInterface {


    int width;
    int height;

    JFrame frame;

    JPanel headerPanel;
    JPanel questionPanel;
    JPanel testPanel;

    JScrollPane questionScrollPane;
    JScrollPane templateScrollPane;



    public AppInterface() {
        width = Toolkit.getDefaultToolkit().getScreenSize().width;
        height = Toolkit.getDefaultToolkit().getScreenSize().height;

        frame = new JFrame("TopLevelDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
