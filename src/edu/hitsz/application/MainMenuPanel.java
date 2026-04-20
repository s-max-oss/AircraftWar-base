package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private JFrame parentFrame;
    private JTextField nameField;
    private JComboBox<String> difficultyComboBox;

    public MainMenuPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(null);
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        Image backgroundImage = ImageManager.BACKGROUND_IMAGE;
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        add(backgroundLabel);

        JLabel titleLabel = new JLabel("飞机大战");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2, 120, 200, 60);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        JLabel nameLabel = new JLabel("用户名:");
        nameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2 - 80, 200, 80, 30);
        add(nameLabel);

        nameField = new JTextField("玩家");
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        nameField.setBounds((Main.WINDOW_WIDTH - 200) / 2 + 10, 200, 190, 30);
        add(nameField);

        JLabel difficultyLabel = new JLabel("难度:");
        difficultyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2 - 80, 245, 80, 30);
        add(difficultyLabel);

        String[] difficulties = {"普通", "困难", "噩梦"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        difficultyComboBox.setBounds((Main.WINDOW_WIDTH - 200) / 2 + 10, 245, 190, 30);
        add(difficultyComboBox);

        JButton startButton = createButton("开始游戏", 150, 310);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);

        JButton recordButton = createButton("游戏记录", 150, 370);
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRecord();
            }
        });
        add(recordButton);

        JButton rankingButton = createButton("排行榜", 150, 430);
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRanking();
            }
        });
        add(rankingButton);

        JButton exitButton = createButton("退出游戏", 150, 490);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exitButton);

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
        String userName = nameField.getText().trim();
        if (userName.isEmpty()) {
            userName = "玩家";
        }
        String difficulty = (String) difficultyComboBox.getSelectedItem();
        int initialDifficulty = 1;
        if ("困难".equals(difficulty)) {
            initialDifficulty = 3;
        } else if ("噩梦".equals(difficulty)) {
            initialDifficulty = 5;
        }
        parentFrame.getContentPane().removeAll();
        Game game = new Game(parentFrame, userName, initialDifficulty);
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

    private void showRanking() {
        parentFrame.getContentPane().removeAll();
        RankingPanel rankingPanel = new RankingPanel(parentFrame);
        parentFrame.add(rankingPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}