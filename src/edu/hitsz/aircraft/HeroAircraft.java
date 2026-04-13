package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.drop.Drop;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.ShootStrategy;
import edu.hitsz.strategy.RingShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    // 单例实例（使用volatile修饰，确保可见性和防止指令重排序）
    private static volatile HeroAircraft instance;

    //每次射击发射子弹数量
    private int shootNum = 1;
    //原始子弹数量
    private int originalShootNum = 1;

    //子弹威力
    private int power = 30;
    //原始子弹威力
    private int originalPower = 30;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = -1;
    
    //火力道具激活时间
    private long powerUpTime = 0;
    //火力道具持续时间（毫秒）
    private static final long POWER_UP_DURATION = 10000;

    // 私有构造方法
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 初始化射击策略为散弹射击
        this.shootStrategy = new ScatterShootStrategy(shootNum, power, direction);
    }

    // 获取单例实例的方法（使用DCL双重检查锁定）
    public static HeroAircraft getInstance() {
        // 第一次检查，避免不必要的同步
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                // 第二次检查，确保只创建一个实例
                if (instance == null) {
                    instance = new HeroAircraft(
                            Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0, 0, 100
                    );
                }
            }
        }
        return instance;
    }

    // 重置英雄机（游戏重新开始时使用）
    public static synchronized void resetInstance() {
        instance = null;
    }
    
    /**
     * 重置英雄机状态
     */
    public void reset() {
        // 重置位置
        locationX = Main.WINDOW_WIDTH / 2;
        locationY = Main.WINDOW_HEIGHT - 100;
        // 重置生命值
        hp = maxHp;
        // 重置子弹数量和威力
        shootNum = 1;
        power = 30;
        // 更新射击策略
        this.shootStrategy = new ScatterShootStrategy(shootNum, power, direction);
        // 重置有效状态
        isValid = true;
    }

    public void increasePower(int increase) {
        // 记录原始值（如果还没有记录）
        if (powerUpTime == 0) {
            originalShootNum = shootNum;
            originalPower = power;
        }
        // 增加子弹威力
        power += increase;
        // 更新射击策略
        this.shootStrategy = new ScatterShootStrategy(shootNum, power, direction);
        // 重置激活时间（无论是否已经有火力道具效果）
        powerUpTime = System.currentTimeMillis();
    }
    
    public void setPowerTo(int aim) {
        power = aim;
    }
    
    /**
     * 增加最大生命值
     * @param increase 增加的最大生命值
     */
    public void increaseMaxHp(int increase){
        maxHp += increase;
        // 同时增加当前生命值
        hp += increase;
    }
    
    /**
     * 增加生命值
     * @param increase 增加的生命值
     */
    public void increaseHp(int increase){
        hp += increase;
        // 生命值不能超过最大值
        if(hp > maxHp){
            hp = maxHp;
        }
    }
    
    /**
     * 增加子弹数量
     * @param increase 增加的子弹数量
     */
    public void increaseBulletNum(int increase) {
        // 记录原始值（如果还没有记录）
        if (powerUpTime == 0) {
            originalShootNum = shootNum;
            originalPower = power;
        }
        // 增加子弹数量
        shootNum += increase;
        // 更新射击策略
        this.shootStrategy = new ScatterShootStrategy(shootNum, power, direction);
        // 重置激活时间（无论是否已经有火力道具效果）
        powerUpTime = System.currentTimeMillis();
    }
    
    // 原始射击策略
    private ShootStrategy originalShootStrategy;
    // 环形射击策略激活时间
    private long ringShootTime = 0;
    // 环形射击策略持续时间（毫秒）
    private static final long RING_SHOOT_DURATION = 10000;
    
    /**
     * 检查火力道具是否过期
     * 如果过期，恢复原始的子弹数量和威力
     */
    public void checkPowerUpExpiration() {
        if (powerUpTime > 0 && System.currentTimeMillis() - powerUpTime >= POWER_UP_DURATION) {
            // 恢复原始值
            shootNum = originalShootNum;
            power = originalPower;
            // 更新射击策略
            this.shootStrategy = new ScatterShootStrategy(shootNum, power, direction);
            // 重置激活时间
            powerUpTime = 0;
        }
        
        // 检查环形射击策略是否过期
        if (ringShootTime > 0 && System.currentTimeMillis() - ringShootTime >= RING_SHOOT_DURATION) {
            // 恢复原始射击策略
            if (originalShootStrategy != null) {
                this.shootStrategy = originalShootStrategy;
            } else {
                this.shootStrategy = new ScatterShootStrategy(shootNum, power, direction);
            }
            // 重置环形射击激活时间
            ringShootTime = 0;
        }
    }
    
    /**
     * 激活环形射击策略
     */
    public void activateRingShoot() {
        // 保存原始射击策略
        originalShootStrategy = this.shootStrategy;
        // 设置环形射击策略
        this.shootStrategy = new RingShootStrategy(8, power);
        // 设置激活时间
        ringShootTime = System.currentTimeMillis();
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 英雄机掉落掉落物
     * @return 掉落物List
     */
    public List<Drop> drop() {
        return new LinkedList<>();
    }
}
