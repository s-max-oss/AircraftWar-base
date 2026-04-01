package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.drop.Drop;
import java.util.List;
import java.util.LinkedList;

/**
 * 所有敌机的抽象父类
 * @author hitsz
 */
public abstract class EnemyAircraft extends AbstractAircraft {

    public EnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 敌机通用的移动逻辑可以在这里实现
    }

    @Override
    public abstract List<BaseBullet> shoot();

    /**
     * 敌机掉落道具方法
     * @return 掉落物列表
     */
    public List<Drop> drop() {
        return new LinkedList<>();
    }

}
