package edu.hitsz.drop;

import edu.hitsz.application.Main;
import edu.hitsz.drop.Drop;
import edu.hitsz.aircraft.HeroAircraft;

public class AddBullet extends Drop {
    // 增加子弹数
    protected int bulletNum = 1;

    public AddBullet(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    /**
     * 激活道具效果
     * @param heroAircraft 英雄机
     */
    public void activate(HeroAircraft heroAircraft) {
        // 给英雄机回复血量
        heroAircraft.increaseBulletNum(bulletNum);
        // 道具使用后消失
        vanish();
    }

    public int getBulletNum() {
        return bulletNum;
    }
}
