package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecordPanel extends JPanel {
    private JFrame parentFrame;
    private static int totalEnemiesKilled = 0;
    private static int highestScore = 0;
    private static long longestPlayTime = 0;
    
    public static void updateRecord(int enemiesKilled, int score, long playTime) {
        totalEnemiesKilled += enemiesKilled;
        if (score > highestScore) {
            highestScore = score;
        }
        if (playTime > longestPlayTime) {
            longestPlayTime = playTime;
        }
    }
    
    public RecordPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(null);
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
        
        // 加载背景图片
        Image backgroundImage = ImageManager.BACKGROUND_IMAGE;
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        // 背景标签添加到最底层
        add(backgroundLabel);
        
        // 标题
        JLabel titleLabel = new JLabel("游戏记录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2, 100, 200, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // 确保标题显示在背景之上
        add(titleLabel);
        
        // 记录信息
        JLabel enemiesLabel = createRecordLabel("总击落敌机数: " + totalEnemiesKilled, 150);
        add(enemiesLabel);
        
        JLabel scoreLabel = createRecordLabel("最高得分: " + highestScore, 200);
        add(scoreLabel);
        
        JLabel timeLabel = createRecordLabel("最长游戏时间: " + formatTime(longestPlayTime), 250);
        add(timeLabel);
        
        // 返回按钮
        JButton backButton = createButton("返回主菜单", 150, 350);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
        add(backButton);
        
        // 重新设置组件层次，确保所有组件显示在背景之上
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
    
    private JLabel createRecordLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        label.setForeground(Color.WHITE);
        label.setBounds((Main.WINDOW_WIDTH - 200) / 2, y, 200, 40);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    private JButton createButton(String text, int width, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(100, 149, 237, 200));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        int x = (Main.WINDOW_WIDTH - width) / 2;
        button.setBounds(x, y, width, 40);
        return button;
    }
    
    private String formatTime(long milliseconds) {
        int seconds = (int) (milliseconds / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    private void backToMainMenu() {
        parentFrame.getContentPane().removeAll();
        MainMenuPanel mainMenuPanel = new MainMenuPanel(parentFrame);
        parentFrame.add(mainMenuPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}