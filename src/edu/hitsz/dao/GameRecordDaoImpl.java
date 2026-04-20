package edu.hitsz.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameRecordDaoImpl implements GameRecordDao {
    private static final String FILE_NAME = "game_records.dat";
    private List<GameRecord> records;

    public GameRecordDaoImpl() {
        this.records = loadRecords();
    }

    private List<GameRecord> loadRecords() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<GameRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new ArrayList<>(records));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRecord(GameRecord record) {
        int newId = records.isEmpty() ? 1 : records.stream()
                .mapToInt(GameRecord::getId)
                .max()
                .orElse(0) + 1;
        record.setId(newId);
        records.add(record);
        saveRecords();
    }

    @Override
    public void removeRecord(int id) {
        records.removeIf(record -> record.getId() == id);
        reorderRecords();
        saveRecords();
    }

    private void reorderRecords() {
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setId(i + 1);
        }
    }

    @Override
    public List<GameRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    @Override
    public List<GameRecord> getRecordsByInitialDifficulty(int initialDifficulty) {
        List<GameRecord> filteredRecords = new ArrayList<>();
        for (GameRecord record : records) {
            if (record.getInitialDifficulty() == initialDifficulty) {
                filteredRecords.add(record);
            }
        }
        // 按分数降序排序
        filteredRecords.sort((r1, r2) -> Integer.compare(r2.getScore(), r1.getScore()));
        return filteredRecords;
    }

    @Override
    public void clearAllRecords() {
        records.clear();
        saveRecords();
    }
}