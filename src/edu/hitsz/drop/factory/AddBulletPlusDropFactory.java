package edu.hitsz.drop.factory;

import edu.hitsz.drop.Drop;
import edu.hitsz.drop.AddBulletPlus;

public class AddBulletPlusDropFactory implements DropFactory {
    @Override
    public Drop createDrop(int locationX, int locationY, int speedX, int speedY) {
        return new AddBulletPlus(locationX, locationY, speedX, speedY);
    }
}