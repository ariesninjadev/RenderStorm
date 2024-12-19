package com.ariesninja.renderstorm;

import javax.swing.*;
import java.awt.*;

public class TextWindow extends JFrame {
    private JTextArea textArea;

    public TextWindow() {
        setTitle("Renderstorm Debug");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void updateText(String text) {
        textArea.setText(text);
    }
}