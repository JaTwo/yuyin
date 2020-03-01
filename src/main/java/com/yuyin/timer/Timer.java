package com.yuyin.timer;

import com.yuyin.conf.ConfigEntry;
import com.yuyin.conversion.ConverToMp3;
import com.yuyin.music.MusicPlayer;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import java.awt.HeadlessException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
/**
 * 计时器
 */
public class Timer extends JFrame {

    private static int helpCount = 1;
    private static final long serialVersionUID = 1L;
    private static final String INITIAL_LABEL_TEXT = "00:00:00 000";
    // 计数线程
    private CountingThread thread = new CountingThread();
    // 记录程序开始时间
    private long programStart = System.currentTimeMillis();
    // 程序一开始就是暂停的
    private long pauseStart = programStart;
    // 程序暂停的总时间
    private long pauseCount = 0;
    private JLabel label = new JLabel(INITIAL_LABEL_TEXT);
    private JButton refershPauseButton = new JButton("刷新题库");
    private JButton startPauseButton = new JButton("开始");
    private JButton resetButton = new JButton("清零");
    private JButton preButton = new JButton("English");
    private JButton nextButton = new JButton("专业课");
    private JButton helpButton = new JButton("提示");
    private String helpTip = "";

    private MusicPlayer musicPlayer = MusicPlayer.getInstance();

    private ActionListener refershButtonListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
//            ConverToMp3.getInstance().cover();
            helpCount = 1;
            JOptionPane.showMessageDialog(null, "【请不要想着走后门】" , "提示【请好好学习哦】", JOptionPane.ERROR_MESSAGE);
        }
    };

    private ActionListener startPauseButtonListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (thread.stopped) {
                pauseCount += (System.currentTimeMillis() - pauseStart);
                thread.stopped = false;
                startPauseButton.setText("暂停");
            } else {
                pauseStart = System.currentTimeMillis();
                thread.stopped = true;
                startPauseButton.setText("继续");
            }
        }
    };

    private ActionListener preButtonListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String fileName = musicPlayer.start(false);
            helpTip = ConfigEntry.HELP_TIP.get(fileName);
        }
    };

    private ActionListener nextButtonListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String fileName = musicPlayer.start(true);
            helpTip = ConfigEntry.HELP_TIP.get(fileName);
        }
    };
    private ActionListener helpButtonListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if ( helpCount == 3) {
                JOptionPane.showMessageDialog(null, "【" + helpTip + "】" + "！你已使用了三次提示，还剩两次", "提示【请好好学习哦】", JOptionPane.WARNING_MESSAGE);
                helpCount++;
            } else if ( helpCount > 5) {
                JOptionPane.showMessageDialog(null, "！你的提示机会已使用完！", "提示【请好好学习哦】", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "【" + helpTip + "】", "提示【请好好学习哦】", JOptionPane.INFORMATION_MESSAGE);
                helpCount++;
            }
        }
    };

    private ActionListener resetButtonListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            pauseStart = programStart;
            pauseCount = 0;
            thread.stopped = true;
            label.setText(INITIAL_LABEL_TEXT);
            startPauseButton.setText("开始");
        }
    };

    public Timer(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(500, 300);
        setResizable(false);
        setupBorder();
        setupLabel();
        setupButtonsPanel();
        refershPauseButton.addActionListener(refershButtonListener);
        preButton.addActionListener(preButtonListener);
        startPauseButton.addActionListener(startPauseButtonListener);
        resetButton.addActionListener(resetButtonListener);
        nextButton.addActionListener(nextButtonListener);
        helpButton.addActionListener(helpButtonListener);
        thread.start(); // 计数线程一直就运行着
    }
    // 为窗体面板添加边框
    private void setupBorder() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
    }
    // 配置按钮
    private void setupButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(refershPauseButton);
        panel.add(preButton);
        panel.add(startPauseButton);
        panel.add(resetButton);
        panel.add(nextButton);
        panel.add(helpButton);
        add(panel, BorderLayout.SOUTH);
    }
    // 配置标签
    private void setupLabel() {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 80));
        this.add(label, BorderLayout.CENTER);
    }

    private class CountingThread extends Thread {
        public boolean stopped = true;
        private CountingThread() {
            setDaemon(true);
        }
        @Override
        public void run() {
            while (true) {
                if (!stopped) {
                    long elapsed = System.currentTimeMillis() - programStart - pauseCount;
                    label.setText(format(elapsed));
                }
                try {
                    sleep(1); // 1毫秒更新一次显示
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        // 将毫秒数格式化
        private String format(long elapsed) {
            int hour, minute, second, milli;
            milli = (int) (elapsed % 1000);
            elapsed = elapsed / 1000;
            second = (int) (elapsed % 60);
            elapsed = elapsed / 60;
            minute = (int) (elapsed % 60);
            elapsed = elapsed / 60;
            hour = (int) (elapsed % 60);
            return String.format("%02d:%02d:%02d %03d", hour, minute, second, milli);
        }
    }
}
