package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.Boss;

public class BossEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY, int difficultyLevel) {
        // 内置Boss敌机的属性
        int speedX = (int) (Math.random() * 4); // 横向速度（0-3之间的随机整数）
        int speedY = 0; // 纵向速度0
        int hp = 300;
        return new Boss(locationX, locationY, speedX, speedY, hp);
    }
}