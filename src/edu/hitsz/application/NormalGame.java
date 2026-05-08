package edu.hitsz.application;

import javax.swing.JFrame;
import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.factory.EnemyAircraftFactory;
import edu.hitsz.aircraft.factory.EnemyAircraftFactoryManager;

/**
 * 普通难度游戏类
 * - 可以生成Boss敌机
 * - Boss血量不随召唤次数增加
 * - 难度会随时间递进
 */
public class NormalGame extends Game {

    private int bossSpawnCount = 0;
    private boolean bossWarningShown = false;

    public NormalGame(JFrame frame, String userName) {
        super(frame, userName, 2);
        initializeDifficultySettings();
    }

    private void initializeDifficultySettings() {
        baseEnemyPower = 50;
        baseSpeedFactor = 1.1;
        enemyMaxNumber = 12;
        eliteEnemyMaxNumber = 6;
        elitePlusEnemyMaxNumber = 3;
        enemySpawnCycle = 16;
        eliteEnemySpawnCycle = 45;
        elitePlusEnemySpawnCycle = 80;
        backgroundIndex = 1;
    }

    @Override
    protected void spawnEnemyAircrafts() {
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

        elitePlusEnemySpawnCounter++;
        if (elitePlusEnemySpawnCounter >= elitePlusEnemySpawnCycle) {
            elitePlusEnemySpawnCounter = 0;
            if (enemyAircrafts.size() < elitePlusEnemyMaxNumber + eliteEnemyMaxNumber + enemyMaxNumber) {
                int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PLUS_ENEMY_IMAGE.getWidth()));
                int y = 0;
                EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("ElitePlusEnemy");
                enemyAircrafts.add(factory.createAircraft(x, y, difficultyLevel));
            }
        }

        // 生成Boss
        if (canSpawnBoss()) {
            int bossCount = 0;
            for (AbstractAircraft aircraft : enemyAircrafts) {
                if (aircraft instanceof Boss) {
                    bossCount++;
                }
            }
            if (bossCount < bossMaxNumber && score >= lastBossScore + bossScoreThreshold) {
                int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_IMAGE.getWidth()));
                int y = 0;
                Boss boss = new Boss(x, y, 2, 0, getBossHp());
                enemyAircrafts.add(boss);
                lastBossScore = score;
                bossScoreThreshold += 500;
                bossSpawnCount++;
                soundManager.playBossBGM();
                bossWarningShown = false;
            }
        }
    }

    @Override
    protected void updateDifficultyLevel(long gameTime) {
        int newDifficulty = 2;
        
        if (gameTime >= 30 && gameTime < 120) {
            newDifficulty = 3;
            backgroundIndex = 1;
        } else if (gameTime >= 120 && gameTime < 300) {
            newDifficulty = 4;
            backgroundIndex = 2;
        } else if (gameTime >= 300) {
            newDifficulty = 5;
            backgroundIndex = 3;
        }

        if (newDifficulty > difficultyLevel) {
            difficultyLevel = newDifficulty;
            adjustDifficultySettings(newDifficulty);
            System.out.println("难度提升至等级 " + difficultyLevel);
        }
    }

    private void adjustDifficultySettings(int level) {
        switch (level) {
            case 3:
                baseEnemyPower = 50;
                baseSpeedFactor = 1.1;
                enemyMaxNumber = 11;
                eliteEnemyMaxNumber = 6;
                break;
            case 4:
                baseEnemyPower = 60;
                baseSpeedFactor = 1.15;
                enemyMaxNumber = 12;
                eliteEnemyMaxNumber = 7;
                break;
            case 5:
                baseEnemyPower = 70;
                baseSpeedFactor = 1.2;
                enemyMaxNumber = 13;
                eliteEnemyMaxNumber = 8;
                break;
        }
    }

    @Override
    protected void printDifficultyInfo() {
        if (!bossWarningShown && score >= lastBossScore + bossScoreThreshold - 200 && score < lastBossScore + bossScoreThreshold) {
            System.out.println("警告：Boss即将到来！");
            bossWarningShown = true;
        }
    }

    @Override
    protected boolean canSpawnBoss() {
        return true;
    }

    @Override
    protected int getBossHp() {
        // 普通难度Boss血量固定
        return 300;
    }
}