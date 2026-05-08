package edu.hitsz.application;


import edu.hitsz.aircraft.*;
import edu.hitsz.drop.*;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    private static final Map<String, BufferedImage> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static BufferedImage BACKGROUND_IMAGE;
    public static BufferedImage BACKGROUND_IMAGE2;
    public static BufferedImage BACKGROUND_IMAGE3;
    public static BufferedImage BACKGROUND_IMAGE4;
    public static BufferedImage BACKGROUND_IMAGE5;
    public static BufferedImage HERO_IMAGE;
    public static BufferedImage HERO_BULLET_IMAGE;
    public static BufferedImage ENEMY_BULLET_IMAGE;
    public static BufferedImage MOB_ENEMY_IMAGE;
    public static BufferedImage ELITE_ENEMY_IMAGE;
    public static BufferedImage ELITE_PLUS_ENEMY_IMAGE;
    public static BufferedImage ELITE_PRO_ENEMY_IMAGE;
    public static BufferedImage BOSS_IMAGE;
    public static BufferedImage HP_DROP_IMAGE;
    public static BufferedImage ADD_BULLET_DROP_IMAGE;
    public static BufferedImage ADD_BULLET_PLUS_DROP_IMAGE;
    public static BufferedImage BOMB_DROP_IMAGE;
    public static BufferedImage FREEZE_DROP_IMAGE;
    static {
        try {
            ClassLoader classLoader = ImageManager.class.getClassLoader();

            BACKGROUND_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/bg.jpg"));
            BACKGROUND_IMAGE2 = ImageIO.read(classLoader.getResourceAsStream("images/bg2.jpg"));
            BACKGROUND_IMAGE3 = ImageIO.read(classLoader.getResourceAsStream("images/bg3.jpg"));
            BACKGROUND_IMAGE4 = ImageIO.read(classLoader.getResourceAsStream("images/bg4.jpg"));
            BACKGROUND_IMAGE5 = ImageIO.read(classLoader.getResourceAsStream("images/bg5.jpg"));

            HERO_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/hero.png"));
            MOB_ENEMY_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/mob.png"));
            ELITE_ENEMY_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/elite.png"));
            ELITE_PLUS_ENEMY_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/elitePlus.png"));
            ELITE_PRO_ENEMY_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/elitePro.png"));
            BOSS_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/boss.png"));
            HERO_BULLET_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/bullet_hero.png"));
            ENEMY_BULLET_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/bullet_enemy.png"));
            HP_DROP_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/prop_blood.png"));
            ADD_BULLET_DROP_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/prop_bullet.png"));
            ADD_BULLET_PLUS_DROP_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/prop_bulletPlus.png"));
            BOMB_DROP_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/prop_bomb.png"));
            FREEZE_DROP_IMAGE = ImageIO.read(classLoader.getResourceAsStream("images/prop_freeze.png"));

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(ElitePlusEnemy.class.getName(), ELITE_PLUS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteProEnemy.class.getName(), ELITE_PRO_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(Hp.class.getName(), HP_DROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(AddBullet.class.getName(), ADD_BULLET_DROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(AddBulletPlus.class.getName(), ADD_BULLET_PLUS_DROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(Bomb.class.getName(), BOMB_DROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(Freeze.class.getName(), FREEZE_DROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(Boss.class.getName(), BOSS_IMAGE);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static BufferedImage get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static BufferedImage get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

}