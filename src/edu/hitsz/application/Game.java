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
    
    // 父窗口
    private JFrame parentFrame;
    // 游戏开始时间
    private long gameStartTime;
    // 敌机击杀数
    private int enemiesKilled;

    //屏幕中出现的敌机最大数量
    private int enemyMaxNumber = 10;
    //屏幕中出现的精英敌机最大数量
    private int eliteEnemyMaxNumber = 5;
    //屏幕中出现的高级精英敌机最大数量
    private int elitePlusEnemyMaxNumber = 2;
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
    
    // 难度等级
    private int difficultyLevel = 1;
    // 基础攻击力
    private int baseEnemyPower = 40;
    // 基础移动速度系数
    private double baseSpeedFactor = 1.0;
    // 背景图片索引
    private int backgroundIndex = 0;

    public Game(JFrame frame) {
        this.parentFrame = frame;
        // 记录游戏开始时间
        this.gameStartTime = System.currentTimeMillis();
        // 初始化敌机击杀数
        this.enemiesKilled = 0;
        
        // 使用单例模式获取英雄机实例
        heroAircraft = HeroAircraft.getInstance();
        // 重置英雄机状态
        heroAircraft.reset();

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
                // 计算游戏时间（秒）
                long gameTime = (System.currentTimeMillis() - gameStartTime) / 1000;
                
                // 根据游戏时间调整难度等级
                updateDifficultyLevel(gameTime);

                enemySpawnCounter++;
                if (enemySpawnCounter >=enemySpawnCycle) {
                    enemySpawnCounter = 0;
                    // 产生普通敌机
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        // 随机生成敌机位置
                        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
                        int y = 0;
                        // 使用工厂模式创建普通敌机
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("MobEnemy");
                        enemyAircrafts.add(factory.createAircraft(x, y, difficultyLevel));
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
                        // 随机生成敌机位置
                        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth()));
                        int y = 0;
                        // 使用工厂模式创建精英敌机
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("EliteEnemy");
                        enemyAircrafts.add(factory.createAircraft(x, y, difficultyLevel));
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
                        // 随机生成敌机位置
                        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PLUS_ENEMY_IMAGE.getWidth()));
                        int y = 0;
                        // 使用工厂模式创建高级精英敌机
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("ElitePlusEnemy");
                        enemyAircrafts.add(factory.createAircraft(x, y, difficultyLevel));
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
                        // 随机生成敌机位置
                        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PRO_ENEMY_IMAGE.getWidth()));
                        int y = 0;
                        // 使用工厂模式创建专业精英敌机
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("EliteProEnemy");
                        enemyAircrafts.add(factory.createAircraft(x, y, difficultyLevel));
                    }
                }

                // 生成Boss敌机（30秒后才开始生成）
                if (gameTime >= 30) {
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
                            // 随机生成敌机位置
                        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PRO_ENEMY_IMAGE.getWidth()));
                        int y = 0;
                        // 使用工厂模式创建Boss敌机
                        EnemyAircraftFactory factory = EnemyAircraftFactoryManager.getFactory("Boss");
                        enemyAircrafts.add(factory.createAircraft(x, y, difficultyLevel));
                        }
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
                List<BaseBullet> bullets = enemyAircraft.shoot();
                // 根据难度等级调整子弹威力
                for (BaseBullet bullet : bullets) {
                    // 调整子弹威力（根据难度等级增加）
                    int originalPower = bullet.getPower();
                    int newPower = originalPower + (difficultyLevel - 1) * 10;
                    bullet.setPower(newPower);
                }
                enemyBullets.addAll(bullets);
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
                        // 增加敌机击杀数
                        enemiesKilled++;
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
            
            // 计算游戏时间
            long gameTime = System.currentTimeMillis() - gameStartTime;
            
            // 显示游戏结束面板
            parentFrame.getContentPane().removeAll();
            GameOverPanel gameOverPanel = new GameOverPanel(parentFrame, enemiesKilled, score, gameTime);
            parentFrame.add(gameOverPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
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
    
    /**
     * 根据游戏时间更新难度等级
     */
    private void updateDifficultyLevel(long gameTime) {
        int newLevel = 1;
        int newBackgroundIndex = 0;
        
        // 根据游戏时间确定难度等级
        if (gameTime < 30) {
            // 30秒以内，难度等级1
            newLevel = 1;
            newBackgroundIndex = 0;
        } else if (gameTime < 120) {
            // 30秒到2分钟，难度等级2
            newLevel = 2;
            newBackgroundIndex = 1;
        } else if (gameTime < 300) {
            // 2分钟到5分钟，难度等级3
            newLevel = 3;
            newBackgroundIndex = 2;
        } else if (gameTime < 540) {
            // 5分钟到9分钟，难度等级4
            newLevel = 4;
            newBackgroundIndex = 3;
        } else {
            // 超过9分钟，难度等级5
            newLevel = 5;
            newBackgroundIndex = 4;
        }
        
        // 如果难度等级发生变化
        if (newLevel != difficultyLevel) {
            difficultyLevel = newLevel;
            
            // 根据难度等级调整属性
            switch (difficultyLevel) {
                case 1:
                    // 难度等级1：基础设置
                    baseEnemyPower = 40;
                    baseSpeedFactor = 1.0;
                    break;
                case 2:
                    // 难度等级2：攻击力+10，速度+10%
                    baseEnemyPower = 50;
                    baseSpeedFactor = 1.1;
                    break;
                case 3:
                    // 难度等级3：最大数量+1，攻击力+10
                    baseEnemyPower = 60;
                    baseSpeedFactor = 1.1;
                    // 增加敌机最大数量
                    enemyMaxNumber++;
                    eliteEnemyMaxNumber++;
                    elitePlusEnemyMaxNumber++;
                    break;
                case 4:
                    // 难度等级4：最大数量+1，攻击力+10
                    baseEnemyPower = 70;
                    baseSpeedFactor = 1.1;
                    // 增加敌机最大数量
                    enemyMaxNumber++;
                    eliteEnemyMaxNumber++;
                    elitePlusEnemyMaxNumber++;
                    break;
                case 5:
                    // 难度等级5：最大数量+1，攻击力+10，速度+25%
                    baseEnemyPower = 80;
                    baseSpeedFactor = 1.25;
                    // 增加敌机最大数量
                    enemyMaxNumber++;
                    eliteEnemyMaxNumber++;
                    elitePlusEnemyMaxNumber++;
                    break;
            }
        }
        
        // 如果背景索引发生变化
        if (newBackgroundIndex != backgroundIndex) {
            backgroundIndex = newBackgroundIndex;
        }
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
        Image backgroundImage = getBackgroundImage();
        g.drawImage(backgroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(backgroundImage, 0, this.backGroundTop, null);
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
        
        // 绘制难度等级
        paintDifficultyLevel(g);

    }
    
    /**
     * 根据背景索引获取背景图片
     */
    private Image getBackgroundImage() {
        switch (backgroundIndex) {
            case 1:
                return ImageManager.BACKGROUND_IMAGE;
            case 2:
                return ImageManager.BACKGROUND_IMAGE;
            case 3:
                return ImageManager.BACKGROUND_IMAGE;
            case 4:
                return ImageManager.BACKGROUND_IMAGE;
            default:
                return ImageManager.BACKGROUND_IMAGE;
        }
    }
    
    /**
     * 绘制难度等级
     */
    private void paintDifficultyLevel(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("宋体", Font.BOLD, 15));
        g.drawString("难度等级: " + difficultyLevel, 10, 60);
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
