package dema;

import dema.PictureModule.ImageFrame;

import java.awt.*;

import javax.swing.*;
public class main {
    public static void main (String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImageFrame frame = new ImageFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
