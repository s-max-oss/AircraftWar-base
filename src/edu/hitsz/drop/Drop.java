package edu.hitsz.drop;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.aircraft.HeroAircraft;

/**
 * 所有掉落物的抽象父类
 * @author hitsz
 */
public abstract class Drop extends AbstractFlyingObject {

    public Drop(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    /**
     * 激活道具效果
     * @param heroAircraft 英雄机
     */
    public abstract void activate(HeroAircraft heroAircraft);
    
    /**
     * 激活道具效果（带游戏对象）
     * @param heroAircraft 英雄机
     * @param game 游戏对象
     */
    public void activate(HeroAircraft heroAircraft, edu.hitsz.application.Game game) {
        // 默认实现，调用无参数版本
        activate(heroAircraft);
    }

}
