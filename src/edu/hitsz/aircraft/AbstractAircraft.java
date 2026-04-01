package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.drop.Drop;
import java.util.List;
import java.util.LinkedList;

/**
 * 所有种类飞机的抽象父类
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {

    //最大生命值
    protected int maxHp;
    protected int hp;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public void increaseHp(int increase){
        hp += increase;
        if(hp > maxHp){
            hp = maxHp;
        }
    }

    public int getHp() {
        return hp;
    }


    /**
     * 飞机射击方法
     * @return
     *  可射击对象需实现，返回子弹列表
     *  非可射击对象空实现，返回空列表
     */
    public abstract List<BaseBullet> shoot();
    
    /**
     * 飞机掉落道具方法
     * @return
     *  可掉落道具的飞机需实现，返回掉落物列表
     *  不可掉落道具的飞机空实现，返回空列表
     */
    public List<Drop> drop() {
        return new LinkedList<>();
    }

}


