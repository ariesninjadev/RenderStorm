package com.ariesninja.renderstorm;

import javax.swing.*;
import java.awt.*;

public class TextWindow extends JFrame {

    private JTextArea textArea;
    private String text;

    public TextWindow() {
        setTitle("Renderstorm Debug");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void add(String text) {
        this.text += text + "\n";
    }

    public void post() {
        textArea.setText(this.text);
        this.text = "";
    }

    public void blank() {
        this.text += "\n";
    }

}