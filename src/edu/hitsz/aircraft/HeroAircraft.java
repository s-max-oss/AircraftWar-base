package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.drop.Drop;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

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

    //子弹威力
    private int power = 30;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = -1;

    // 私有构造方法
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
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
        // 重置有效状态
        isValid = true;
    }

    public void increasePower(int increase){
        power += increase;
    }
    public void setPowerTo(int aim){
        power = aim;
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
    public void increaseBulletNum(int increase){
        shootNum += increase;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
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
