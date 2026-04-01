package edu.hitsz.drop.factory;

import edu.hitsz.drop.Drop;

public interface DropFactory {
    Drop createDrop(int locationX, int locationY, int speedX, int speedY);
}