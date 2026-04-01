package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.factory.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    //调度器, 用于定时任务调度
    private final Timer timer;
    //时间间隔(ms)，控制刷新频率
    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractFlyingObject> drops;

    //屏幕中出现的敌机最大数量
    private final int enemyMaxNumber = 10;
    //屏幕中出现的精英敌机最大数量
    private final int eliteEnemyMaxNumber = 5;
    //屏幕中出现的高级精英敌机最大数量
    private final int elitePlusEnemyMaxNumber = 2;
    //屏幕中出现的专业精英敌机最大数量
    private final int eliteProEnemyMaxNumber = 1;
    //屏幕中出现的boss敌机最大数量
    private final int bossMaxNumber = 1;

    //敌机生成周期
    protected double enemySpawnCycle  =  20;
    private int enemySpawnCounter = 0;
    //精英敌机生成周期
    protected double eliteEnemySpawnCycle  =  60;
    private int eliteEnemySpawnCounter = 0;
    //高级精英敌机生成周期
    protected double elitePlusEnemySpawnCycle  =  100;
    //高级精英敌机生成计数器
    private int elitePlusEnemySpawnCounter = 0;
    //专业精英敌机生成周期
    protected double eliteProEnemySpawnCycle  =  150;
    //专业精英敌机生成计数器
    private int eliteProEnemySpawnCounter = 0;
    //Boss生成周期
    protected double bossSpawnCycle  =  300;
    //Boss生成计数器
    private int bossSpawnCounter = 0;

    //英雄机和敌机射击周期
    private double shootHeroCycle = 10;
    private int shootHeroCounter = 0;

    private double shootEnemyCycle = 50;
    private int shootEnemyCounter = 0;

    //当前玩家分数
    private int score = 0;

    //游戏结束标志
    private boolean gameOverFlag = false;
    
    //敌机冻结标志
    private boolean enemyFrozen = false;
    //冻结持续时间（帧数）
    private int freezeDuration = 30;
    //当前冻结剩余时间
    private int freezeTimer = 0;

    public Game() {
        // 使用单例模式获取英雄机实例
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        drops = new LinkedList<>();

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        this.timer = new Timer("game-action-timer", true);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                enemySpawnCounter++;
                if (enemySpawnCounter >=enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    // 产生普通敌机
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("MobEnemy");
                        enemyAircrafts.add(factory.createAircraft(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                                0 // 在屏幕最上方生成
                        ));
                    }
                }

                // 生成精英敌机
                eliteEnemySpawnCounter++;
                if (eliteEnemySpawnCounter >= eliteEnemySpawnCycle) {
                    eliteEnemySpawnCounter = 0;
                    // 统计当前精英敌机数量
                    int eliteEnemyCount = 0;
                    for (AbstractAircraft aircraft : enemyAircrafts) {
                        if (aircraft instanceof EliteEnemy) {
                            eliteEnemyCount++;
                        }
                    }
                    // 产生精英敌机
                    if (eliteEnemyCount < eliteEnemyMaxNumber) {
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("EliteEnemy");
                        enemyAircrafts.add(factory.createAircraft(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                                0 // 在屏幕最上方生成
                        ));
                    }
                }
                // 生成高级精英敌机
                elitePlusEnemySpawnCounter++;
                if (elitePlusEnemySpawnCounter >= elitePlusEnemySpawnCycle) { 
                    elitePlusEnemySpawnCounter = 0;
                    int elitePlusEnemyCount = 0;
                    for (AbstractAircraft aircraft : enemyAircrafts) {
                        if (aircraft instanceof ElitePlusEnemy) {
                            elitePlusEnemyCount++;
                        }
                    }
                    // 产生高级精英敌机
                    if (elitePlusEnemyCount < elitePlusEnemyMaxNumber) {
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("ElitePlusEnemy");
                        enemyAircrafts.add(factory.createAircraft(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PLUS_ENEMY_IMAGE.getWidth())),
                                0 // 在屏幕最上方生成
                        ));
                    }
                }

                // 生成专业精英敌机
                eliteProEnemySpawnCounter++;
                if (eliteProEnemySpawnCounter >= eliteProEnemySpawnCycle) {
                    eliteProEnemySpawnCounter = 0;
                    int eliteProEnemyCount = 0;
                    for (AbstractAircraft aircraft : enemyAircrafts) {
                        if (aircraft instanceof EliteProEnemy) {
                            eliteProEnemyCount++;
                        }
                    }
                    // 产生专业精英敌机
                    if (eliteProEnemyCount < eliteProEnemyMaxNumber) {
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("EliteProEnemy");
                        enemyAircrafts.add(factory.createAircraft(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PRO_ENEMY_IMAGE.getWidth())),
                                0 // 在屏幕最上方生成
                        ));
                    }
                }

                bossSpawnCounter++;
                if (bossSpawnCounter >= bossSpawnCycle) {
                    bossSpawnCounter = 0;
                    int bossSpawnCount = 0;
                    for (AbstractAircraft aircraft : enemyAircrafts){
                        if (aircraft instanceof Boss){
                            bossSpawnCount++;
                        }
                    }
                    // 产生Boss敌机
                    if (bossSpawnCount < bossMaxNumber) {
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("Boss");
                        enemyAircrafts.add(factory.createAircraft(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PRO_ENEMY_IMAGE.getWidth())),
                                0 // 在屏幕最上方生成
                        ));
                    }
                }

                // 飞机发射子弹
                heroShootAction();
                enemyShootAction();
                // 子弹移动
                bulletsMoveAction();
                // 飞机移动
                aircraftsMoveAction();
                // 道具移动
                dropsMoveAction();
                // 撞击检测
                crashCheckAction();
                // 后处理
                postProcessAction();
                // 重绘界面
                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        timer.schedule(task,0,timeInterval);

    }

    //***********************
    //      Action 各部分
    //***********************

    private void heroShootAction() {
        shootHeroCounter++;
        if (shootHeroCounter >= shootHeroCycle) {
            shootHeroCounter = 0;
            //英雄机射击
            heroBullets.addAll(heroAircraft.shoot());
        }
    }

    private void enemyShootAction(){
        shootEnemyCounter ++;
        if (shootEnemyCounter >= shootEnemyCycle){
            shootEnemyCounter = 0;
            //敌机射击
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        // 处理冻结逻辑
        if (enemyFrozen) {
            freezeTimer--;
            if (freezeTimer <= 0) {
                enemyFrozen = false;
            }
            // 冻结时敌机不移动
            return;
        }
        
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }
    
    /**
     * 道具移动
     */
    private void dropsMoveAction() {
        for (AbstractFlyingObject drop : drops) {
            drop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        //敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 撞击到英雄机子弹
                // 英雄机损失一定生命值
                bullet.vanish();
                heroAircraft.decreaseHp(bullet.getPower());
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // 获得分数
                        score += 10;
                        // 产生道具补给
                        drops.addAll(enemyAircraft.drop());
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (AbstractFlyingObject drop : drops) {
            if (drop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(drop)) {
                // 英雄机碰到道具
                if (drop instanceof edu.hitsz.drop.Hp) {
                    // 激活Hp道具效果
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
                }
                if (drop instanceof edu.hitsz.drop.Freeze){
                    ((edu.hitsz.drop.Freeze) drop).activate(heroAircraft, this);
                }
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        drops.removeIf(AbstractFlyingObject::notValid);
    }

    /**
     * 检查游戏是否结束，若结束：关闭线程池
     */
    private void checkResultAction(){
        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.cancel(); // 取消定时器并终止所有调度任务
            gameOverFlag = true;
            System.out.println("Game Over!");
        }
    };
    
    /**
     * 清除所有敌机和敌机子弹（炸弹道具效果）
     */
    public void clearEnemiesAndBullets() {
        // 清除所有敌机
        enemyAircrafts.clear();
        // 清除所有敌机子弹
        enemyBullets.clear();
    }
    
    /**
     * 冻结敌机（冻结道具效果）
     */
    public void freezeEnemies() {
        enemyFrozen = true;
        freezeTimer = freezeDuration;
    }

    //***********************
    //      Paint 各部分
    //***********************
    /**
     * 重写 paint方法
     * 通过重复调用paint方法，实现游戏动画
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        // 绘制道具
        paintImageWithPositionRevised(g, drops);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

}
