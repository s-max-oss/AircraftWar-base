package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel {
    private JFrame parentFrame;
    private int enemiesKilled;
    private int score;
    private long playTime;
    
    public GameOverPanel(JFrame frame, int enemiesKilled, int score, long playTime) {
        this.parentFrame = frame;
        this.enemiesKilled = enemiesKilled;
        this.score = score;
        this.playTime = playTime;
        
        // 更新游戏记录
        RecordPanel.updateRecord(enemiesKilled, score, playTime);
        
        setLayout(null);
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
        
        // 加载背景图片
        Image backgroundImage = ImageManager.BACKGROUND_IMAGE;
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        add(backgroundLabel);
        
        // 标题
        JLabel titleLabel = new JLabel("游戏结束");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        titleLabel.setForeground(Color.RED);
        titleLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2, 100, 200, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);
        
        // 游戏数据
        JLabel enemiesLabel = createDataLabel("击落敌机数: " + enemiesKilled, 180);
        add(enemiesLabel);
        
        JLabel scoreLabel = createDataLabel("得分: " + score, 230);
        add(scoreLabel);
        
        JLabel timeLabel = createDataLabel("游戏时间: " + formatTime(playTime), 280);
        add(timeLabel);
        
        // 重新开始按钮
        JButton restartButton = createButton("重新开始", 150, 350);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        add(restartButton);
        
        // 返回主菜单按钮
        JButton mainMenuButton = createButton("返回主菜单", 150, 410);
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
        add(mainMenuButton);
        
        // 重新设置组件层次，确保所有组件显示在背景之上
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
    
    private JLabel createDataLabel(String text, int y) {
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
        button.setBackground(new Color(220, 20, 60, 200));
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
    
    private void restartGame() {
        parentFrame.getContentPane().removeAll();
        Game game = new Game(parentFrame);
        parentFrame.add(game);
        parentFrame.revalidate();
        parentFrame.repaint();
        game.action();
    }
    
    private void backToMainMenu() {
        parentFrame.getContentPane().removeAll();
        MainMenuPanel mainMenuPanel = new MainMenuPanel(parentFrame);
        parentFrame.add(mainMenuPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}