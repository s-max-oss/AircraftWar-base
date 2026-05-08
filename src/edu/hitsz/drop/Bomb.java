package edu.hitsz.drop;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class Bomb extends Drop {

    public Bomb(int locationX, int locationY, int speedX, int speedY) {
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
                } else if (enemy instanceof EliteProEnemy) {
                    enemy.decreaseHp(100);
                } else {
                    enemy.vanish();
                    int score = 0;
                    if (enemy instanceof EliteEnemy) {
                        score = 30;
                    } else if (enemy instanceof ElitePlusEnemy) {
                        score = 50;
                    } else if (enemy instanceof MobEnemy) {
                        score = 10;
                    }
                    game.addScore(score);
                }
            }
        }
        
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.isValid()) {
                bullet.vanish();
            }
        }
        
        vanish();
    }
}