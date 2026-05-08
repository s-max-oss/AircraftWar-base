package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.drop.Drop;
import edu.hitsz.drop.factory.DropFactory;
import edu.hitsz.drop.factory.DropFactoryManager;
import edu.hitsz.strategy.RingShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class Boss extends EnemyAircraft{
    //每次射击发射子弹数量
    private int shootNum = 3;

    //子弹威力
    private int power = 40;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = 1;

    public Boss(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 初始化射击策略为环形射击，发射20颗子弹形成环形（密度提高150%）
        this.shootStrategy = new RingShootStrategy(20, power);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    /**
     * 高级精英敌机掉落掉落物
     * @return 掉落物List
     */
    public List<Drop> drop() {
        List <Drop> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY();
        int speedX = 0;
        int speedY = this.getSpeedY();
        double rand = Math.random();
        if (rand < 0.2) {
            res.add(DropFactoryManager.getFactory("Bomb").createDrop(x, y, speedX, speedY + 2));
        } else if (rand < 0.4) {
            res.add(DropFactoryManager.getFactory("Freeze").createDrop(x, y, speedX, speedY + 2));
        } else if (rand < 0.6) {
            res.add(DropFactoryManager.getFactory("AddBullet").createDrop(x, y, speedX, speedY + 2));
        } else if (rand < 0.8) {
            res.add(DropFactoryManager.getFactory("Hp").createDrop(x, y, speedX, speedY + 2));
        } else {
            res.add(DropFactoryManager.getFactory("AddBulletPlus").createDrop(x, y, speedX, speedY + 2));
        }
        return res;
    }
}
