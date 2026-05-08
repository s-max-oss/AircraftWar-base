package edu.hitsz.application;

import edu.hitsz.dao.GameRecord;
import edu.hitsz.dao.GameRecordDao;
import edu.hitsz.dao.GameRecordDaoImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RankingPanel extends JPanel {
    private JFrame parentFrame;
    private GameRecordDao gameRecordDao;
    private JTabbedPane tabbedPane;
    private GameOverPanel gameOverPanel;
    
    // 当前游戏数据
    private int currentScore;
    private int currentEnemiesKilled;
    private long currentPlayTime;
    private String currentUserName;
    private int currentDifficulty;
    private int currentInitialDifficulty;
    private boolean isFromGameOver;

    public RankingPanel(JFrame frame) {
        this.parentFrame = frame;
        this.gameOverPanel = null;
        this.gameRecordDao = new GameRecordDaoImpl();
        this.isFromGameOver = false;
        initUI();
    }

    public RankingPanel(JFrame frame, GameOverPanel gameOverPanel, int score, int enemiesKilled, long playTime, String userName, int difficulty, int initialDifficulty) {
        this.parentFrame = frame;
        this.gameOverPanel = gameOverPanel;
        this.gameRecordDao = new GameRecordDaoImpl();
        this.currentScore = score;
        this.currentEnemiesKilled = enemiesKilled;
        this.currentPlayTime = playTime;
        this.currentUserName = userName;
        this.currentDifficulty = difficulty;
        this.currentInitialDifficulty = initialDifficulty;
        this.isFromGameOver = true;
        
        showNameInputDialog();
        initUI();
    }
    
    private void initUI() {
        setLayout(null);
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        Image backgroundImage = ImageManager.BACKGROUND_IMAGE;
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        add(backgroundLabel);

        JLabel titleLabel = new JLabel("排行榜");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2, 50, 200, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        tabbedPane = new JTabbedPane();
        int tabWidth = 550;
        int tabHeight = 350;
        tabbedPane.setBounds((Main.WINDOW_WIDTH - tabWidth) / 2, 120, tabWidth, tabHeight);
        add(tabbedPane);

        refreshRanking();

        JButton backButton;
        if (isFromGameOver) {
            backButton = createButton("返回游戏结束", 150, 500);
        } else {
            backButton = createButton("返回主菜单", 150, 500);
        }
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isFromGameOver) {
                    backToGameOver();
                } else {
                    backToMainMenu();
                }
            }
        });
        add(backButton);

        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }

    private void showNameInputDialog() {
        JTextField nameField = new JTextField(currentUserName);
        int option = JOptionPane.showConfirmDialog(this, 
            new Object[]{"请输入您的姓名:", nameField},
            "保存游戏记录",
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                currentUserName = name;
            }
            saveCurrentRecord();
        }
    }

    private void saveCurrentRecord() {
        GameRecord record = new GameRecord();
        record.setName(currentUserName);
        record.setScore(currentScore);
        record.setEnemiesKilled(currentEnemiesKilled);
        record.setGameTime(currentPlayTime);
        record.setRecordTime(System.currentTimeMillis());
        record.setDifficulty(currentDifficulty);
        record.setInitialDifficulty(currentInitialDifficulty);
        gameRecordDao.addRecord(record);
    }

    private void refreshRanking() {
        tabbedPane.removeAll();
        
        JComponent normalPanel = createRankingPanel(1, "普通");
        tabbedPane.addTab("普通", normalPanel);

        JComponent hardPanel = createRankingPanel(3, "困难");
        tabbedPane.addTab("困难", hardPanel);

        JComponent nightmarePanel = createRankingPanel(5, "噩梦");
        tabbedPane.addTab("噩梦", nightmarePanel);
    }

    private JComponent createRankingPanel(int initialDifficulty, String difficultyName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"排名", "用户名", "得分", "击落敌机数", "游戏时间", "记录时间"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(tableModel);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setEnabled(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnNames.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);

        List<GameRecord> records = gameRecordDao.getRecordsByInitialDifficulty(initialDifficulty);

        if (records.isEmpty()) {
            tableModel.addRow(new Object[]{"-", "暂无记录", "-", "-", "-", "-"});
        } else {
            int rank = 1;
            for (GameRecord record : records) {
                tableModel.addRow(new Object[]{
                    rank++,
                    record.getName(),
                    record.getScore(),
                    record.getEnemiesKilled(),
                    formatTime(record.getGameTime()),
                    formatRecordTime(record.getRecordTime())
                });
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
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

    private String formatRecordTime(long milliseconds) {
        java.util.Date date = new java.util.Date(milliseconds);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    private void backToMainMenu() {
        parentFrame.getContentPane().removeAll();
        MainMenuPanel mainMenuPanel = new MainMenuPanel(parentFrame);
        parentFrame.add(mainMenuPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void backToGameOver() {
        parentFrame.getContentPane().removeAll();
        GameOverPanel panel = new GameOverPanel(parentFrame, currentEnemiesKilled, currentScore, currentPlayTime, currentUserName, currentDifficulty, currentInitialDifficulty);
        parentFrame.add(panel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}