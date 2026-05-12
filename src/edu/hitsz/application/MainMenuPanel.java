package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainMenuPanel extends JPanel {
    private JFrame parentFrame;
    private JTextField nameField;
    private JComboBox<String> difficultyComboBox;
    private JSlider bgmSlider;
    private JSlider soundEffectSlider;

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

        String[] difficulties = {"简单", "普通", "困难"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        difficultyComboBox.setBounds((Main.WINDOW_WIDTH - 200) / 2 + 10, 245, 190, 30);
        add(difficultyComboBox);

        JLabel bgmVolumeLabel = new JLabel("背景音乐:");
        bgmVolumeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        bgmVolumeLabel.setForeground(Color.WHITE);
        bgmVolumeLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2 - 80, 290, 80, 25);
        add(bgmVolumeLabel);

        bgmSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(SoundManager.getInstance().getBgmVolume() * 100));
        bgmSlider.setBounds((Main.WINDOW_WIDTH - 200) / 2 + 10, 290, 190, 25);
        bgmSlider.setBackground(new Color(0, 0, 0, 100));
        bgmSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SoundManager.getInstance().setBgmVolume(bgmSlider.getValue() / 100f);
            }
        });
        add(bgmSlider);

        JLabel soundEffectLabel = new JLabel("音效音量:");
        soundEffectLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        soundEffectLabel.setForeground(Color.WHITE);
        soundEffectLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2 - 80, 320, 80, 25);
        add(soundEffectLabel);

        soundEffectSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)(SoundManager.getInstance().getSoundEffectVolume() * 100));
        soundEffectSlider.setBounds((Main.WINDOW_WIDTH - 200) / 2 + 10, 320, 190, 25);
        soundEffectSlider.setBackground(new Color(0, 0, 0, 100));
        soundEffectSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SoundManager.getInstance().setSoundEffectVolume(soundEffectSlider.getValue() / 100f);
            }
        });
        add(soundEffectSlider);

        JButton startButton = createButton("开始游戏", 150, 370);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);

        JButton recordButton = createButton("游戏记录", 150, 430);
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRecord();
            }
        });
        add(recordButton);

        JButton rankingButton = createButton("排行榜", 150, 490);
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRanking();
            }
        });
        add(rankingButton);

        JButton exitButton = createButton("退出游戏", 150, 550);
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
        parentFrame.getContentPane().removeAll();

        Game game;
        switch (difficulty) {
            case "简单":
                game = new SimpleGame(parentFrame, userName);
                break;
            case "困难":
                game = new HardGame(parentFrame, userName);
                break;
            default:
                game = new NormalGame(parentFrame, userName);
                break;
        }

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