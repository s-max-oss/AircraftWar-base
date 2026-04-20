package edu.hitsz.dao;

import java.util.List;

public interface GameRecordDao {
    void addRecord(GameRecord record);
    void removeRecord(int id);
    List<GameRecord> getAllRecords();
    List<GameRecord> getRecordsByInitialDifficulty(int initialDifficulty);
    void clearAllRecords();
}