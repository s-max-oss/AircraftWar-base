package edu.hitsz.aircraft.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;

public class EliteEnemyFactory implements EnemyAircraftFactory {
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY, int difficultyLevel) {
        // 内置精英敌机的属性
        int speedX = (int) (Math.random() * 3); // 横向速度（0-2之间的随机整数）
        int speedY = 3 + (int) (Math.random() * 3); // 纵向速度3-5
        int hp = 60;
        return new EliteEnemy(locationX, locationY, speedX, speedY, hp);
    }
}