package edu.hitsz.bullet;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 子弹基类
 * @author hitsz
 */
public abstract class BaseBullet extends AbstractFlyingObject {

    private int power = 0;
    
    // 冰冻相关字段
    protected long freezeEndTime = 0;
    protected int originalSpeedX = 0;
    protected int originalSpeedY = 0;

    public BaseBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public int getPower() {
        return power;
    }
    
    public void setPower(int power) {
        this.power = power;
    }
    
    public int getSpeedX() {
        return speedX;
    }
    
    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }
    
    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
    
    public void setFreezeEndTime(long time) {
        this.freezeEndTime = time;
    }
    
    public void setOriginalSpeed(int speedX, int speedY) {
        this.originalSpeedX = speedX;
        this.originalSpeedY = speedY;
    }
    
    public void updateFreezeState() {
        if (System.currentTimeMillis() >= freezeEndTime && freezeEndTime > 0) {
            this.speedX = originalSpeedX;
            this.speedY = originalSpeedY;
            freezeEndTime = 0;
        }
    }
}
