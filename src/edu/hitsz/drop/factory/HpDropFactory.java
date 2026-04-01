package edu.hitsz.drop.factory;

import edu.hitsz.drop.Drop;
import edu.hitsz.drop.Hp;

public class HpDropFactory implements DropFactory {
    @Override
    public Drop createDrop(int locationX, int locationY, int speedX, int speedY) {
        return new Hp(locationX, locationY, speedX, speedY);
    }
}