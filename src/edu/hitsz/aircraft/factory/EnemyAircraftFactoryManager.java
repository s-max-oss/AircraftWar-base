package edu.hitsz.aircraft.factory;

import java.util.HashMap;
import java.util.Map;

public class EnemyAircraftFactoryManager {
    private static final Map<String, EnemyAircraftFactory> factoryMap = new HashMap<>();
    
    static {
        // 注册所有敌机工厂
        factoryMap.put("MobEnemy", new MobEnemyFactory());
        factoryMap.put("EliteEnemy", new EliteEnemyFactory());
        factoryMap.put("ElitePlusEnemy", new ElitePlusEnemyFactory());
        factoryMap.put("EliteProEnemy", new EliteProEnemyFactory());
        factoryMap.put("Boss", new BossEnemyFactory());
    }
    
    public static EnemyAircraftFactory getFactory(String aircraftType) {
        return factoryMap.get(aircraftType);
    }
}