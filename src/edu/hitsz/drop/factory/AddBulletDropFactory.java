package edu.hitsz.drop.factory;

import edu.hitsz.drop.Drop;
import edu.hitsz.drop.AddBullet;

public class AddBulletDropFactory implements DropFactory {
    @Override
    public Drop createDrop(int locationX, int locationY, int speedX, int speedY) {
        return new AddBullet(locationX, locationY, speedX, speedY);
    }
}