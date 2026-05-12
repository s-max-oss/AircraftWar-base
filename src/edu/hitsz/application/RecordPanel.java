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

public class RecordPanel extends JPanel {
    private JFrame parentFrame;
    private GameRecordDao gameRecordDao;
    private JTable recordTable;
    private DefaultTableModel tableModel;

    public RecordPanel(JFrame frame) {
        this.parentFrame = frame;
        this.gameRecordDao = new GameRecordDaoImpl();

        setLayout(null);
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        Image backgroundImage = ImageManager.BACKGROUND_IMAGE;
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        add(backgroundLabel);

        JLabel titleLabel = new JLabel("游戏记录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds((Main.WINDOW_WIDTH - 200) / 2, 50, 200, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        String[] columnNames = {"序号", "用户名", "得分", "击落敌机数", "游戏时间", "难度", "记录时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recordTable = new JTable(tableModel);
        recordTable.setBackground(new Color(255, 255, 255, 200));
        recordTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        recordTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        recordTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        recordTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        recordTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        recordTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        recordTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        recordTable.getColumnModel().getColumn(6).setPreferredWidth(140);

        // 为所有列设置居中渲染器
        for (int i = 0; i < recordTable.getColumnCount(); i++) {
            recordTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(recordTable);
        int tableWidth = 620;
        int tableHeight = 300;
        scrollPane.setBounds((Main.WINDOW_WIDTH - tableWidth) / 2, 120, tableWidth, tableHeight);
        scrollPane.setBackground(new Color(255, 255, 255, 200));
        add(scrollPane);

        loadRecords();

        JButton deleteButton = createButton("删除选中记录", 150, 440);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecord();
            }
        });
        add(deleteButton);

        JButton clearButton = createButton("清空所有记录", 150, 490);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllRecords();
            }
        });
        add(clearButton);

        JButton backButton = createButton("返回主菜单", 150, 540);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
        add(backButton);

        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }

    private void loadRecords() {
        tableModel.setRowCount(0);
        List<GameRecord> records = gameRecordDao.getAllRecords();
        for (GameRecord record : records) {
            Object[] rowData = {
                record.getId(),
                record.getName(),
                record.getScore(),
                record.getEnemiesKilled(),
                formatTime(record.getGameTime()),
                record.getDifficultyName(),
                formatRecordTime(record.getRecordTime())
            };
            tableModel.addRow(rowData);
        }
    }

    private void deleteSelectedRecord() {
        int selectedRow = recordTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            gameRecordDao.removeRecord(id);
            loadRecords();
            JOptionPane.showMessageDialog(this, "记录已删除", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "请先选择要删除的记录", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearAllRecords() {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要清空所有游戏记录吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            gameRecordDao.clearAllRecords();
            loadRecords();
            JOptionPane.showMessageDialog(this, "所有记录已清空", "提示", JOptionPane.INFORMATION_MESSAGE);
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
        SoundManager.getInstance().stopBGM();
        SoundManager.getInstance().stopBossBGM();
        parentFrame.getContentPane().removeAll();
        MainMenuPanel mainMenuPanel = new MainMenuPanel(parentFrame);
        parentFrame.add(mainMenuPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}