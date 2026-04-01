package edu.hitsz.drop.factory;

import edu.hitsz.drop.Drop;
import edu.hitsz.drop.Bomb;

public class BombDropFactory implements DropFactory {
    @Override
    public Drop createDrop(int locationX, int locationY, int speedX, int speedY) {
        return new Bomb(locationX, locationY, speedX, speedY);
    }
}