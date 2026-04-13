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
 * 环形射击策略
 * 子弹以自身为中心环形散开
 */
public class RingShootStrategy implements ShootStrategy {
    // 每次射击发射子弹数量
    private int shootNum;
    // 子弹威力
    private int power;

    public RingShootStrategy(int shootNum, int power) {
        this.shootNum = shootNum;
        this.power = power;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        // 获取飞机的宽度和高度，用于调整子弹生成位置
        int width = aircraft.getWidth();
        int height = aircraft.getHeight();
        BaseBullet bullet;

        // 计算每个子弹的角度（360度环形）
        double angleStep = 2 * Math.PI / shootNum;
        int bulletSpeed = 5;

        for (int i = 0; i < shootNum; i++) {
            // 计算子弹的速度向量
            double angle = i * angleStep;
            int speedX = (int) (bulletSpeed * Math.cos(angle));
            int speedY = (int) (bulletSpeed * Math.sin(angle));
            
            // 调整子弹生成位置，使其从飞机外部生成
            int bulletX = x + (int) (Math.cos(angle) * width / 2);
            int bulletY = y + (int) (Math.sin(angle) * height / 2);

            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(bulletX, bulletY, speedX, speedY, power);
            } else if (aircraft instanceof EnemyAircraft) {
                bullet = new EnemyBullet(bulletX, bulletY, speedX, speedY, power);
            } else {
                continue;
            }
            res.add(bullet);
        }
        return res;
    }
}
