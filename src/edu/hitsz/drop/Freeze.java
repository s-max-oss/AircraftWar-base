package edu.hitsz.drop;

import edu.hitsz.aircraft.HeroAircraft;

public class Freeze extends Drop {

    public Freeze(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        // 无需实现具体功能
        vanish();
    }
}
