package edu.hitsz.application;

import edu.hitsz.dao.GameRecord;
import edu.hitsz.dao.GameRecordDao;
import edu.hitsz.dao.GameRecordDaoImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel {
    private JFrame parentFrame;
    private int enemiesKilled;
    private int score;
    private long playTime;
    private String userName;
    private int difficulty;
    private int initialDifficulty;
    private GameRecordDao gameRecordDao;

    public GameOverPanel(JFrame frame, int enemiesKilled, int score, long playTime, String userName, int difficulty, int initialDifficulty) {
        this.parentFrame = frame;
        this.enemiesKilled = enemiesKilled;
        this.score = score;
        this.playTime = playTime;
        this.userName = userName;
        this.difficulty = difficulty;
        this.initialDifficulty = initialDifficulty;
        this.gameRecordDao = new GameRecordDaoImpl();

        setLayout(null);
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        Image backgroundImage = ImageManager.BACKGROUND_IMAGE;
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        add(backgroundLabel);

        JLabel titleLabel = new JLabel("游戏结束");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        titleLabel.setForeground(Color.RED);
        titleLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2, 80, 200, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        JLabel enemiesLabel = createDataLabel("击落敌机数: " + enemiesKilled, 150);
        add(enemiesLabel);

        JLabel scoreLabel = createDataLabel("得分: " + score, 200);
        add(scoreLabel);

        JLabel timeLabel = createDataLabel("游戏时间: " + formatTime(playTime), 250);
        add(timeLabel);

        JLabel difficultyLabel = createDataLabel("难度: " + getDifficultyName(difficulty), 300);
        add(difficultyLabel);

        JButton viewRankingButton = createButton("查看排行榜", 150, 350);
        viewRankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRanking();
            }
        });
        add(viewRankingButton);

        JButton restartButton = createButton("重新开始", 150, 400);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        add(restartButton);

        JButton mainMenuButton = createButton("返回主菜单", 150, 450);
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
        add(mainMenuButton);

        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }

    private String getDifficultyName(int difficulty) {
        switch (difficulty) {
            case 1:
                return "普通";
            case 2:
                return "困难";
            case 3:
                return "困难";
            case 4:
                return "噩梦";
            case 5:
                return "噩梦";
            default:
                return "普通";
        }
    }

    private void saveRecord() {
        GameRecord record = new GameRecord();
        record.setName(userName);
        record.setScore(score);
        record.setEnemiesKilled(enemiesKilled);
        record.setGameTime(playTime);
        record.setRecordTime(System.currentTimeMillis());
        record.setDifficulty(difficulty);
        record.setInitialDifficulty(initialDifficulty);
        gameRecordDao.addRecord(record);
    }

    private void showRanking() {
        parentFrame.getContentPane().removeAll();
        RankingPanel rankingPanel = new RankingPanel(parentFrame, this, score, enemiesKilled, playTime, userName, difficulty, initialDifficulty);
        parentFrame.add(rankingPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
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
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
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
        Game game;
        switch (initialDifficulty) {
            case 1:
                game = new SimpleGame(parentFrame, userName);
                break;
            case 2:
                game = new NormalGame(parentFrame, userName);
                break;
            default: // 3, 4, 5
                game = new HardGame(parentFrame, userName);
                break;
        }
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