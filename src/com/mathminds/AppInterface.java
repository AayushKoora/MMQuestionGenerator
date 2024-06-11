package com.mathminds;

import javax.swing.*;
import java.awt.*;

public class AppInterface {



    public static void main(String[] args) {
        System.out.println("booting first screen.");

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        JFrame frame = new JFrame("TopLevelDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(width, (int) (height * 0.1)));
        headerPanel.setBackground(Color.red);
        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);


        //question panel
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setPreferredSize(new Dimension(width, (int) (height * 0.55)));
        questionPanel.setBackground(Color.GREEN);

        JScrollPane questionScrollPane = new JScrollPane(questionPanel);
        questionScrollPane.setPreferredSize(new Dimension((int) (width * 0.35), questionPanel.getHeight()));
        questionScrollPane.setBackground(Color.CYAN);

        frame.getContentPane().add(questionPanel, BorderLayout.CENTER);


        //test panel
        JPanel testPanel = new JPanel(new BorderLayout());
        testPanel.setPreferredSize(new Dimension(width, (int) (height * 0.35)));
        testPanel.setBackground(Color.BLUE);
        frame.getContentPane().add(testPanel, BorderLayout.SOUTH);





        //Display the window.
        frame.pack();
        frame.setVisible(true);


    }
}
