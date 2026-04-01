package edu.hitsz.drop.factory;

import java.util.HashMap;
import java.util.Map;

public class DropFactoryManager {
    private static final Map<String, DropFactory> factoryMap = new HashMap<>();
    
    static {
        // 注册所有道具工厂
        factoryMap.put("Hp", new HpDropFactory());
        factoryMap.put("AddBullet", new AddBulletDropFactory());
        factoryMap.put("AddBulletPlus", new AddBulletPlusDropFactory());
        factoryMap.put("Bomb", new BombDropFactory());
        factoryMap.put("Freeze", new FreezeDropFactory());
    }
    
    public static DropFactory getFactory(String dropType) {
        return factoryMap.get(dropType);
    }
}