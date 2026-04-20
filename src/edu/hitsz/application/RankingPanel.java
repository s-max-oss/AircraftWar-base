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

    public RankingPanel(JFrame frame) {
        this.parentFrame = frame;
        this.gameRecordDao = new GameRecordDaoImpl();

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

        // 添加普通难度排行榜
        JComponent normalPanel = createRankingPanel(1, "普通");
        tabbedPane.addTab("普通", normalPanel);

        // 添加困难难度排行榜
        JComponent hardPanel = createRankingPanel(3, "困难");
        tabbedPane.addTab("困难", hardPanel);

        // 添加噩梦难度排行榜
        JComponent nightmarePanel = createRankingPanel(5, "噩梦");
        tabbedPane.addTab("噩梦", nightmarePanel);

        JButton backButton = createButton("返回主菜单", 150, 500);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
        add(backButton);

        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }

    private JComponent createRankingPanel(int initialDifficulty, String difficultyName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        String[] columnNames = {"排名", "用户名", "得分", "击落敌机数", "游戏时间", "记录时间"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setBackground(new Color(255, 255, 255, 200));
        table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);

        // 为所有列设置居中渲染器
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    return label;
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 加载排行榜数据
        loadRankingData(tableModel, initialDifficulty);

        return panel;
    }

    private void loadRankingData(DefaultTableModel tableModel, int initialDifficulty) {
        tableModel.setRowCount(0);
        List<GameRecord> records = gameRecordDao.getRecordsByInitialDifficulty(initialDifficulty);
        for (int i = 0; i < records.size(); i++) {
            GameRecord record = records.get(i);
            Object[] rowData = {
                i + 1,
                record.getName(),
                record.getScore(),
                record.getEnemiesKilled(),
                formatTime(record.getGameTime()),
                formatRecordTime(record.getRecordTime())
            };
            tableModel.addRow(rowData);
        }

        // 如果没有记录，显示提示信息
        if (records.isEmpty()) {
            Object[] rowData = {"1", "暂无记录", "0", "0", "00:00", "-"};
            tableModel.addRow(rowData);
        }
    }

    private JButton createButton(String text, int width, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
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

    private String formatRecordTime(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date(timestamp));
    }

    private void backToMainMenu() {
        parentFrame.getContentPane().removeAll();
        MainMenuPanel mainMenuPanel = new MainMenuPanel(parentFrame);
        parentFrame.add(mainMenuPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}