package edu.hitsz.drop.factory;

import edu.hitsz.drop.Drop;
import edu.hitsz.drop.Freeze;

public class FreezeDropFactory implements DropFactory {
    @Override
    public Drop createDrop(int locationX, int locationY, int speedX, int speedY) {
        return new Freeze(locationX, locationY, speedX, speedY);
    }
}