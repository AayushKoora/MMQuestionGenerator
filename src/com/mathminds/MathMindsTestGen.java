package com.mathminds;

import javax.swing.*;
import java.awt.*;

public class MathMindsTestGen {

    public static void main(String[] args) {
        System.out.println("booting first screen.");

        //create the frame
        JFrame appFrame = new JFrame("MathMinds Test Gen");
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        appFrame.setSize(width, height);
        appFrame.setVisible(true);



    }
}
