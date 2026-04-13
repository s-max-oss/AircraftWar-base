package edu.hitsz.drop;

import edu.hitsz.aircraft.HeroAircraft;

public class AddBulletPlus extends Drop {

    // 增加子弹数
    protected int power = 10;

    public AddBulletPlus(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        // 给英雄机增加子弹威力
        heroAircraft.increasePower(power);
        // 激活环形射击策略
        heroAircraft.activateRingShoot();
        // 道具使用后消失
        vanish();
    }

    public int getPower() {
        return power;
    }
}
