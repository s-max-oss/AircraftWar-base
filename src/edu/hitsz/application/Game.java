package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.factory.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.drop.Drop;
import edu.hitsz.observer.EnemyObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * 游戏抽象模板类，使用模板方法模式
 * @author hitsz
 */
public abstract class Game extends JPanel {

    protected int backGroundTop = 0;

    protected final Timer timer;
    protected final int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractFlyingObject> drops;
    
    protected JFrame parentFrame;
    protected long gameStartTime;
    protected int enemiesKilled;

    protected int enemyMaxNumber = 10;
    protected int eliteEnemyMaxNumber = 5;
    protected int elitePlusEnemyMaxNumber = 2;
    protected final int eliteProEnemyMaxNumber = 1;
    protected final int bossMaxNumber = 1;

    protected double enemySpawnCycle  =  20;
    protected int enemySpawnCounter = 0;
    protected double eliteEnemySpawnCycle  =  60;
    protected int eliteEnemySpawnCounter = 0;
    protected double elitePlusEnemySpawnCycle  =  100;
    protected int elitePlusEnemySpawnCounter = 0;
    protected double eliteProEnemySpawnCycle  =  150;
    protected int eliteProEnemySpawnCounter = 0;
    protected double bossSpawnCycle  =  300;
    protected int bossSpawnCounter = 0;

    protected double shootHeroCycle = 10;
    protected int shootHeroCounter = 0;

    protected double shootEnemyCycle = 50;
    protected int shootEnemyCounter = 0;

    protected int score = 0;
    protected boolean gameOverFlag = false;
    
    protected boolean enemyFrozen = false;
    protected int freezeDuration = 60;
    protected int freezeTimer = 0;
    
    protected int bossScoreThreshold = 1000;
    protected int lastBossScore = 0;
    protected int bossHpIncrease = 0;
    
    protected int maxHpScoreThreshold = 1500;
    protected int lastMaxHpScore = 0;
    protected int maxHpIncrease = 20;
    
    protected int difficultyLevel = 1;
    protected int initialDifficulty = 1;
    protected int baseEnemyPower = 40;
    protected double baseSpeedFactor = 1.0;
    protected int backgroundIndex = 0;
    protected String userName;
    protected SoundManager soundManager;

    public Game(JFrame frame, String userName, int initialDifficulty) {
        this.parentFrame = frame;
        this.userName = userName;
        this.gameStartTime = System.currentTimeMillis();
        this.enemiesKilled = 0;
        this.difficultyLevel = initialDifficulty;
        this.initialDifficulty = initialDifficulty;
        
        heroAircraft = HeroAircraft.getInstance();
        heroAircraft.reset();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        drops = new LinkedList<>();

        new HeroController(this, heroAircraft);
        soundManager = new SoundManager();
        this.timer = new Timer("game-action-timer", true);
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        soundManager.playBGM();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long gameTime = (System.currentTimeMillis() - gameStartTime) / 1000;
                
                updateDifficultyLevel(gameTime);
                printDifficultyInfo();

                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    spawnEnemyAircrafts();
                }

                heroShootAction();
                enemyShootAction();
                bulletsMoveAction();
                aircraftsMoveAction();
                dropsMoveAction();
                crashCheckAction();
                postProcessAction();
                heroAircraft.checkPowerUpExpiration();
                updateFreezeState();
                
                repaint();
                checkResultAction();
            }
        };
        timer.schedule(task, 0, timeInterval);
    }

    /**
     * 模板方法：生成敌机（由子类实现）
     */
    protected abstract void spawnEnemyAircrafts();

    /**
     * 模板方法：更新难度等级（由子类实现）
     */
    protected abstract void updateDifficultyLevel(long gameTime);

    /**
     * 模板方法：打印难度信息（由子类实现）
     */
    protected abstract void printDifficultyInfo();

    /**
     * 是否允许生成Boss（由子类实现）
     */
    protected abstract boolean canSpawnBoss();

    /**
     * 获取Boss血量（由子类实现）
     */
    protected abstract int getBossHp();

    protected void heroShootAction() {
        shootHeroCounter++;
        if (shootHeroCounter >= shootHeroCycle) {
            shootHeroCounter = 0;
            List<BaseBullet> bullets = heroAircraft.shoot();
            heroBullets.addAll(bullets);
        }
    }

    protected void enemyShootAction() {
        shootEnemyCounter++;
        if (shootEnemyCounter >= shootEnemyCycle) {
            shootEnemyCounter = 0;
            for (AbstractAircraft enemy : enemyAircrafts) {
                List<BaseBullet> bullets = enemy.shoot();
                for (BaseBullet bullet : bullets) {
                    bullet.setPower(bullet.getPower() + baseEnemyPower - 40);
                }
                enemyBullets.addAll(bullets);
            }
        }
    }

    protected void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    protected void aircraftsMoveAction() {
        for (AbstractAircraft enemy : enemyAircrafts) {
            enemy.forward();
        }
    }

    protected void dropsMoveAction() {
        for (AbstractFlyingObject drop : drops) {
            drop.forward();
        }
    }

    protected void crashCheckAction() {
        List<BaseBullet> heroBulletsCopy = new LinkedList<>(heroBullets);
        for (BaseBullet bullet : heroBulletsCopy) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    soundManager.playHitSound();
                    if (enemyAircraft.notValid()) {
                        // 根据敌机类型给予不同分数
                        if (enemyAircraft instanceof Boss) {
                            score += 500;
                            soundManager.stopBossBGM();
                        } else if (enemyAircraft instanceof EliteProEnemy) {
                            score += 100;
                        } else if (enemyAircraft instanceof ElitePlusEnemy) {
                            score += 50;
                        } else if (enemyAircraft instanceof EliteEnemy) {
                            score += 30;
                        } else {
                            score += 10;
                        }
                        enemiesKilled++;
                        List<Drop> newDrops = enemyAircraft.drop();
                        registerDropObservers(newDrops);
                        drops.addAll(newDrops);
                    }
                }
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        List<BaseBullet> enemyBulletsCopy = new LinkedList<>(enemyBullets);
        for (BaseBullet bullet : enemyBulletsCopy) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        for (AbstractFlyingObject drop : drops) {
            if (drop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(drop)) {
                soundManager.playPowerUpSound();
                if (drop instanceof edu.hitsz.drop.Hp) {
                    ((edu.hitsz.drop.Hp) drop).activate(heroAircraft);
                }
                if (drop instanceof edu.hitsz.drop.AddBullet){
                    ((edu.hitsz.drop.AddBullet) drop).activate(heroAircraft);
                }
                if (drop instanceof edu.hitsz.drop.AddBulletPlus){
                    ((edu.hitsz.drop.AddBulletPlus) drop).activate(heroAircraft);
                }
                if (drop instanceof edu.hitsz.drop.Bomb){
                    ((edu.hitsz.drop.Bomb) drop).activate(heroAircraft, this);
                    soundManager.playBombSound();
                }
                if (drop instanceof edu.hitsz.drop.Freeze){
                    ((edu.hitsz.drop.Freeze) drop).activate(heroAircraft, this);
                }
            }
        }
    }

    protected void registerDropObservers(List<Drop> newDrops) {
        for (Drop drop : newDrops) {
            for (AbstractAircraft enemy : enemyAircrafts) {
                if (enemy.isValid()) {
                    drop.addObserver(new EnemyObserver(enemy));
                }
            }
            for (BaseBullet bullet : enemyBullets) {
                if (bullet.isValid()) {
                    drop.addObserver(new EnemyObserver(bullet));
                }
            }
        }
    }

    protected void postProcessAction() {
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        drops.removeIf(AbstractFlyingObject::notValid);
    }

    protected void updateFreezeState() {
        if (enemyFrozen && freezeTimer > 0) {
            freezeTimer--;
            if (freezeTimer == 0) {
                enemyFrozen = false;
            }
        }
        
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy.isValid()) {
                enemy.updateFreezeState();
            }
        }
        
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.isValid()) {
                bullet.updateFreezeState();
            }
        }
    }

    protected void checkResultAction() {
        if (heroAircraft.getHp() <= 0) {
            timer.cancel();
            gameOverFlag = true;
            System.out.println("Game Over!");
            
            soundManager.playGameOverSound();
            
            long gameTime = System.currentTimeMillis() - gameStartTime;
            
            parentFrame.getContentPane().removeAll();
            GameOverPanel gameOverPanel = new GameOverPanel(parentFrame, enemiesKilled, score, gameTime, userName, difficultyLevel, initialDifficulty);
            parentFrame.add(gameOverPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }

    public void clearEnemyBullets() {
        enemyBullets.clear();
    }

    public void freezeEnemies() {
        enemyFrozen = true;
        freezeTimer = freezeDuration;
        for (AbstractAircraft aircraft : enemyAircrafts) {
            if (aircraft instanceof Boss) {
                aircraft.setSpeedX(0);
                aircraft.setSpeedY(0);
            } else {
                aircraft.setSpeedX(0);
                aircraft.setSpeedY(0);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        BufferedImage bg = getBackgroundImage();
        g.drawImage(bg, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(bg, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        for (AbstractFlyingObject drop : drops) {
            drop.paint(g);
        }
        for (BaseBullet bullet : heroBullets) {
            bullet.paint(g);
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.paint(g);
        }
        for (AbstractAircraft enemy : enemyAircrafts) {
            enemy.paint(g);
        }
        heroAircraft.paint(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("微软雅黑", Font.BOLD, 18));
        g.drawString("分数: " + score, 10, 25);
        g.drawString("击杀: " + enemiesKilled, 10, 50);
        g.drawString("血量: " + heroAircraft.getHp(), 10, 75);
        g.drawString("难度: " + getDifficultyName(difficultyLevel), Main.WINDOW_WIDTH - 120, 25);
    }

    protected BufferedImage getBackgroundImage() {
        switch (backgroundIndex) {
            case 1: return ImageManager.BACKGROUND_IMAGE2;
            case 2: return ImageManager.BACKGROUND_IMAGE3;
            case 3: return ImageManager.BACKGROUND_IMAGE4;
            case 4: return ImageManager.BACKGROUND_IMAGE5;
            default: return ImageManager.BACKGROUND_IMAGE;
        }
    }

    protected String getDifficultyName(int difficulty) {
        switch (difficulty) {
            case 1: return "简单";
            case 2: return "普通";
            case 3: return "困难";
            case 4: return "试炼";
            case 5: return "炼狱";
            default: return "简单";
        }
    }

    public int getScore() {
        return score;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public long getGameTime() {
        return System.currentTimeMillis() - gameStartTime;
    }

    public String getUserName() {
        return userName;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getInitialDifficulty() {
        return initialDifficulty;
    }
    
    public List<AbstractAircraft> getEnemyAircrafts() {
        return enemyAircrafts;
    }
    
    public List<BaseBullet> getEnemyBullets() {
        return enemyBullets;
    }
    
    public void addScore(int score) {
        this.score += score;
    }
}