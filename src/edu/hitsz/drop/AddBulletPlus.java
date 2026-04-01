package edu.hitsz.drop;

import edu.hitsz.aircraft.HeroAircraft;

public class AddBulletPlus extends Drop {

    // 增加子弹数
    protected int bulletNum = 2;

    public AddBulletPlus(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        // 无需实现具体功能
        vanish();
    }

    public int getBulletNum() {
        return bulletNum;
    }
}
