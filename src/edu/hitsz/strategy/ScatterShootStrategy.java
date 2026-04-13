package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import java.util.LinkedList;
import java.util.List;

/**
 * 散弹射击策略
 * 子弹向多个方向分散发射
 */
public class ScatterShootStrategy implements ShootStrategy {
    // 每次射击发射子弹数量
    private int shootNum;
    // 子弹威力
    private int power;
    // 子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction;

    public ScatterShootStrategy(int shootNum, int power, int direction) {
        this.shootNum = shootNum;
        this.power = power;
        this.direction = direction;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedY = aircraft.getSpeedY() + direction * 5;
        BaseBullet bullet;

        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            int offsetX = (i * 2 - shootNum + 1) * 10;
            int speedX = (i - shootNum / 2) * 2;
            
            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(x + offsetX, y, speedX, speedY, power);
            } else if (aircraft instanceof EnemyAircraft) {
                bullet = new EnemyBullet(x + offsetX, y, speedX, speedY, power);
            } else {
                continue;
            }
            res.add(bullet);
        }
        return res;
    }
}
