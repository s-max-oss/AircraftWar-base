package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteProEnemy;

public class EliteProEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY) {
        // 内置专业精英敌机的属性
        int speedX = (int) (Math.random() * 4); // 横向速度（0-3之间的随机整数）
        int speedY = 3 + (int) (Math.random() * 3); // 纵向速度3-5
        int hp = 120;
        return new EliteProEnemy(locationX, locationY, speedX, speedY, hp);
    }
}