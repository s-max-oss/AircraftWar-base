package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

public class PlayerLevelManager {
    
    private static PlayerLevelManager instance;
    private HeroAircraft hero;
    
    private int level = 1;
    private int exp = 0;
    private int[] expThresholds = {0, 200, 500, 1000, 1800, 3000, 5000, 8000, 12000, 18000};
    
    private int[] hpBonuses = {100, 20, 20, 20, 30, 30, 30, 40, 40, 50};
    private int[] powerBonuses = {30, 5, 5, 5, 10, 10, 10, 15, 15, 20};
    
    private static final double SHOOT_SPEED_INTERVAL = 5;
    private static final double SHOOT_SPEED_BONUS = 0.1;
    private static final double MAX_SHOOT_SPEED_BONUS = 0.5;
    
    private PlayerLevelManager() {
        this.hero = HeroAircraft.getInstance();
    }
    
    public static PlayerLevelManager getInstance() {
        if (instance == null) {
            synchronized (PlayerLevelManager.class) {
                if (instance == null) {
                    instance = new PlayerLevelManager();
                }
            }
        }
        return instance;
    }
    
    public void addExp(int amount) {
        exp += amount;
        checkLevelUp();
    }
    
    private void checkLevelUp() {
        while (level < expThresholds.length && exp >= expThresholds[level]) {
            levelUp();
        }
    }
    
    private void levelUp() {
        level++;
        applyLevelBonus();
        System.out.println("========================================");
        System.out.println("          恭喜升级！");
        System.out.println("          当前等级: " + level);
        System.out.println("          生命上限+ " + hpBonuses[level - 1]);
        System.out.println("          攻击力+ " + powerBonuses[level - 1]);
        if (level >= SHOOT_SPEED_INTERVAL) {
            double speedBonus = getShootSpeedBonus();
            if (speedBonus > 0) {
                System.out.println("          攻速+ " + (int)(speedBonus * 100) + "%");
            }
        }
        System.out.println("========================================");
        SoundManager.getInstance().playLevelUpSound();
    }
    
    private void applyLevelBonus() {
        int index = level - 1;
        hero.increaseMaxHp(hpBonuses[index]);
        hero.setPowerTo(30 + powerBonuses[index]);
    }
    
    public double getShootSpeedBonus() {
        if (level < SHOOT_SPEED_INTERVAL) {
            return 0.0;
        }
        int bonusLevels = (level - (int)SHOOT_SPEED_INTERVAL) / (int)SHOOT_SPEED_INTERVAL + 1;
        double bonus = bonusLevels * SHOOT_SPEED_BONUS;
        return Math.min(bonus, MAX_SHOOT_SPEED_BONUS);
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getExp() {
        return exp;
    }
    
    public int getExpToNextLevel() {
        if (level >= expThresholds.length) {
            return 0;
        }
        return expThresholds[level] - exp;
    }
    
    public int getCurrentThreshold() {
        if (level >= expThresholds.length) {
            return expThresholds[expThresholds.length - 1];
        }
        return expThresholds[level];
    }
    
    public void reset() {
        level = 1;
        exp = 0;
    }
    
    public double getExpProgress() {
        if (level >= expThresholds.length) {
            return 1.0;
        }
        int prevThreshold = level > 1 ? expThresholds[level - 1] : 0;
        int currentThreshold = expThresholds[level];
        return (double)(exp - prevThreshold) / (currentThreshold - prevThreshold);
    }
}