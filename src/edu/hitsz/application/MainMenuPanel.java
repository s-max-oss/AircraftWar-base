package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private JFrame parentFrame;
    
    public MainMenuPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(null);
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
        
        // 加载背景图片
        Image backgroundImage = ImageManager.BACKGROUND_IMAGE;
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        add(backgroundLabel);
        
        // 标题
        JLabel titleLabel = new JLabel("飞机大战");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2, 150, 200, 60);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);
        
        // 开始游戏按钮
        JButton startButton = createButton("开始游戏", 150, 280);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);
        
        // 游戏记录按钮
        JButton recordButton = createButton("游戏记录", 150, 360);
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRecord();
            }
        });
        add(recordButton);
        
        // 退出游戏按钮
        JButton exitButton = createButton("退出游戏", 150, 440);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exitButton);
        
        // 重新设置组件层次，确保所有组件显示在背景之上
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
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
    
    private void startGame() {
        parentFrame.getContentPane().removeAll();
        Game game = new Game(parentFrame);
        parentFrame.add(game);
        parentFrame.revalidate();
        parentFrame.repaint();
        game.action();
    }
    
    private void showRecord() {
        parentFrame.getContentPane().removeAll();
        RecordPanel recordPanel = new RecordPanel(parentFrame);
        parentFrame.add(recordPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}