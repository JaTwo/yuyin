package com.yuyin;

import com.yuyin.conversion.ConverToMp3;
import com.yuyin.timer.Timer;

import javax.swing.*;

public class App {
    // 程序入口
    public static void main(String[] args) {
        //txt文件转换为MP3
        ConverToMp3.getInstance().cover();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timer frame = new Timer("！何丫加油！");
        frame.pack();
        frame.setVisible(true);
    }
}
