package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.MobEnemy;

public class MobEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY, int difficultyLevel) {
        // 内置普通敌机的属性
        int speedX = 0;
        int speedY = 2 + (int) (Math.random() * 3); // 纵向速度2-4
        int hp = 30;
        return new MobEnemy(locationX, locationY, speedX, speedY, hp);
    }
}