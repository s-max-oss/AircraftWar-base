package edu.hitsz.application;

import javax.swing.JFrame;
import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.factory.EnemyAircraftFactory;
import edu.hitsz.aircraft.factory.EnemyAircraftFactoryManager;

/**
 * 简单难度游戏类
 * - 无法生成Boss敌机
 * - 敌机攻击力较低
 * - 敌机数量较少
 */
public class SimpleGame extends Game {

    public SimpleGame(JFrame frame, String userName) {
        super(frame, userName, 1);
        initializeDifficultySettings();
    }

    private void initializeDifficultySettings() {
        baseEnemyPower = 35;
        baseSpeedFactor = 0.9;
        enemyMaxNumber = 10;
        eliteEnemyMaxNumber = 4;
        elitePlusEnemyMaxNumber = 2;
        enemySpawnCycle = 20;
        eliteEnemySpawnCycle = 60;
        elitePlusEnemySpawnCycle = 100;
        backgroundIndex = 0;
    }

    @Override
    protected void spawnEnemyAircrafts() {
        // 简单难度只生成普通敌机和精英敌机
        if (enemyAircrafts.size() < enemyMaxNumber) {
            int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
            int y = 0;
            EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("MobEnemy");
            enemyAircrafts.add(factory.createAircraft(x, y, difficultyLevel));
        }

        eliteEnemySpawnCounter++;
        if (eliteEnemySpawnCounter >= eliteEnemySpawnCycle) {
            eliteEnemySpawnCounter = 0;
            if (enemyAircrafts.size() < eliteEnemyMaxNumber + enemyMaxNumber) {
                int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth()));
                int y = 0;
                EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("EliteEnemy");
                enemyAircrafts.add(factory.createAircraft(x, y, difficultyLevel));
            }
        }
    }

    @Override
    protected void updateDifficultyLevel(long gameTime) {
        // 简单难度不提升难度等级
        difficultyLevel = 1;
        backgroundIndex = 0;
    }

    @Override
    protected void printDifficultyInfo() {
        // 简单难度不输出额外信息
    }

    @Override
    protected boolean canSpawnBoss() {
        // 简单难度不允许生成Boss
        return false;
    }

    @Override
    protected int getBossHp() {
        return 0;
    }
}