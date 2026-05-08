package edu.hitsz.drop;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class Freeze extends Drop {

    public Freeze(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        vanish();
    }
    
    @Override
    public void activate(HeroAircraft heroAircraft, edu.hitsz.application.Game game) {
        List<AbstractAircraft> enemyAircrafts = game.getEnemyAircrafts();
        List<BaseBullet> enemyBullets = game.getEnemyBullets();
        
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy.isValid()) {
                if (enemy instanceof Boss) {
                    continue;
                }
                
                int originalSpeedX = enemy.getSpeedX();
                int originalSpeedY = enemy.getSpeedY();
                
                if (enemy instanceof MobEnemy) {
                    enemy.setSpeedX(0);
                    enemy.setSpeedY(0);
                    enemy.setFreezeType(AbstractAircraft.FreezeType.PERMANENT);
                } else if (enemy instanceof EliteEnemy) {
                    enemy.setSpeedX(0);
                    enemy.setSpeedY(0);
                    enemy.setFreezeEndTime(System.currentTimeMillis() + 4000);
                    enemy.setFreezeType(AbstractAircraft.FreezeType.TEMPORARY_4S);
                    enemy.setOriginalSpeed(originalSpeedX, originalSpeedY);
                } else if (enemy instanceof ElitePlusEnemy) {
                    enemy.setSpeedX(0);
                    enemy.setSpeedY(0);
                    enemy.setFreezeEndTime(System.currentTimeMillis() + 3000);
                    enemy.setFreezeType(AbstractAircraft.FreezeType.TEMPORARY_3S);
                    enemy.setOriginalSpeed(originalSpeedX, originalSpeedY);
                } else if (enemy instanceof EliteProEnemy) {
                    enemy.setSpeedX(originalSpeedX / 2);
                    enemy.setSpeedY(originalSpeedY / 2);
                    enemy.setFreezeEndTime(System.currentTimeMillis() + 5000);
                    enemy.setFreezeType(AbstractAircraft.FreezeType.SLOW_5S);
                    enemy.setOriginalSpeed(originalSpeedX, originalSpeedY);
                }
            }
        }
        
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.isValid()) {
                int originalSpeedX = bullet.getSpeedX();
                int originalSpeedY = bullet.getSpeedY();
                bullet.setSpeedX(0);
                bullet.setSpeedY(0);
                bullet.setFreezeEndTime(System.currentTimeMillis() + 5000);
                bullet.setOriginalSpeed(originalSpeedX, originalSpeedY);
            }
        }
        
        vanish();
    }
    
    public enum FreezeType {
        NONE, PERMANENT, TEMPORARY_3S, TEMPORARY_4S, TEMPORARY_5S, SLOW_5S
    }
}