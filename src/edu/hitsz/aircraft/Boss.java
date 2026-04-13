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
        // 50%的概率掉落Bomb道具
        if (Math.random() < 0.2) {
            // 使用工厂模式创建道具
            DropFactory factory = DropFactoryManager.getFactory("Bomb");
            res.add(factory.createDrop(x, y, speedX, speedY + 2));
        }
        else if (Math.random() < 0.4) {
            // 使用工厂模式创建道具
            DropFactory factory = DropFactoryManager.getFactory("Freeze");
            res.add(factory.createDrop(x, y, speedX, speedY + 2));
        }
        else if (Math.random() < 0.6) {
            // 使用工厂模式创建道具
            DropFactory factory = DropFactoryManager.getFactory("AddBullet");
            res.add(factory.createDrop(x, y, speedX, speedY + 2));
        }
        else if (Math.random() < 0.8) {
            // 使用工厂模式创建道具
            DropFactory factory = DropFactoryManager.getFactory("Hp");
            res.add(factory.createDrop(x, y, speedX, speedY + 2));
        }
        else {
            // 使用工厂模式创建道具
            DropFactory factory = DropFactoryManager.getFactory("Bomb");
            res.add(factory.createDrop(x, y, speedX, speedY + 2));
        }
        return res;
    }
}
