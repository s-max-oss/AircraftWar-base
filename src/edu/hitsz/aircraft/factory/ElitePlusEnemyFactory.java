package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.ElitePlusEnemy;

public class ElitePlusEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY) {
        // 内置高级精英敌机的属性
        int speedX = (int) (Math.random() * 4); // 横向速度（0-3之间的随机整数）
        int speedY = 4 + (int) (Math.random() * 3); // 纵向速度4-6
        int hp = 100;
        return new ElitePlusEnemy(locationX, locationY, speedX, speedY, hp);
    }
}