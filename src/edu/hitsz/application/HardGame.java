package edu.hitsz.application;

import javax.swing.JFrame;
import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.factory.EnemyAircraftFactory;
import edu.hitsz.aircraft.factory.EnemyAircraftFactoryManager;

/**
 * 困难难度游戏类
 * - 可以生成Boss敌机
 * - Boss血量随召唤次数增加
 * - 难度会随时间递进
 * - 敌机攻击力和数量都较高
 */
public class HardGame extends Game {

    private int bossSpawnCount = 0;
    private boolean bossWarningShown = false;

    public HardGame(JFrame frame, String userName) {
        super(frame, userName, 3);
        initializeDifficultySettings();
    }

    private void initializeDifficultySettings() {
        baseEnemyPower = 70;
        baseSpeedFactor = 1.3;
        enemyMaxNumber = 15;
        eliteEnemyMaxNumber = 8;
        elitePlusEnemyMaxNumber = 4;
        enemySpawnCycle = 12;
        eliteEnemySpawnCycle = 35;
        elitePlusEnemySpawnCycle = 60;
        eliteProEnemySpawnCycle = 100;
        bossScoreThreshold = 600;
        backgroundIndex = 2;
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

        eliteProEnemySpawnCounter++;
        if (eliteProEnemySpawnCounter >= eliteProEnemySpawnCycle) {
            eliteProEnemySpawnCounter = 0;
            if (enemyAircrafts.size() < eliteProEnemyMaxNumber + elitePlusEnemyMaxNumber + eliteEnemyMaxNumber + enemyMaxNumber) {
                int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PRO_ENEMY_IMAGE.getWidth()));
                int y = 0;
                EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("EliteProEnemy");
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
                Boss boss = new Boss(x, y, 3, 0, getBossHp());
                enemyAircrafts.add(boss);
                lastBossScore = score;
                bossScoreThreshold += 400;
                bossSpawnCount++;
                soundManager.playBossBGM();
                bossWarningShown = false;
            }
        }
    }

    @Override
    protected void updateDifficultyLevel(long gameTime) {
        int newDifficulty = 3;
        
        if (gameTime >= 20 && gameTime < 90) {
            newDifficulty = 4;
            backgroundIndex = 2;
        } else if (gameTime >= 90 && gameTime < 240) {
            newDifficulty = 5;
            backgroundIndex = 4;
        }

        if (newDifficulty > difficultyLevel) {
            difficultyLevel = newDifficulty;
            adjustDifficultySettings(newDifficulty);
            System.out.println("难度提升至等级 " + difficultyLevel);
        }
    }

    private void adjustDifficultySettings(int level) {
        switch (level) {
            case 4:
                baseEnemyPower = 70;
                baseSpeedFactor = 1.2;
                enemyMaxNumber = 14;
                eliteEnemyMaxNumber = 8;
                break;
            case 5:
                baseEnemyPower = 90;
                baseSpeedFactor = 1.3;
                enemyMaxNumber = 16;
                eliteEnemyMaxNumber = 10;
                break;
        }
    }

    @Override
    protected void printDifficultyInfo() {
        if (!bossWarningShown && score >= lastBossScore + bossScoreThreshold - 200 && score < lastBossScore + bossScoreThreshold) {
            System.out.println("警告：Boss即将到来！当前Boss血量: " + getBossHp());
            bossWarningShown = true;
        }
    }

    @Override
    protected boolean canSpawnBoss() {
        return true;
    }

    @Override
    protected int getBossHp() {
        // 困难难度Boss血量随召唤次数增加
        return 400 + bossSpawnCount * 100;
    }
}