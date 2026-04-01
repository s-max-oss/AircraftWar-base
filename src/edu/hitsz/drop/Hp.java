package edu.hitsz.drop;

import edu.hitsz.aircraft.HeroAircraft;
public class Hp extends Drop {

    // 回复血量值
    private int restoreHp = 30;

    public Hp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    
    /**
     * 激活道具效果
     * @param heroAircraft 英雄机
     */
    public void activate(HeroAircraft heroAircraft) {
        // 给英雄机回复血量
        heroAircraft.increaseHp(restoreHp);
        // 道具使用后消失
        vanish();
    }
    
    public int getRestoreHp() {
        return restoreHp;
    }
}
