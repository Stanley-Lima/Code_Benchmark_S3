package pt.uc.fct.s3.benchmark.socket.utils.io;

import javax.swing.*;

public class JtextAreaWriter {
    private static JTextArea area;

    public static void append(String s){
        if(area == null){
            throw new RuntimeException("Area not set!");
        }
        area.append(s);
    }

    public static void setText(String s){
        if(area == null){
            throw new RuntimeException("Area not set!");
        }
        area.setText(s);
    }

    public static void requestFocus(){
        if(area == null){
            throw new RuntimeException("Area not set!");
        }
        area.requestFocus();
    }

    public static void setArea(JTextArea area) {
        JtextAreaWriter.area = area;
    }
}
