package edu.hitsz.application;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static SoundManager instance;

    private Clip bgmClip;
    private Clip bossBgmClip;
    private Clip hitClip;
    private Clip bombClip;
    private Clip powerUpClip;
    private Clip gameOverClip;

    private boolean isBossActive = false;
    private float bgmVolume = 0.7f;
    private float soundEffectVolume = 0.8f;

    private SoundManager() {
        loadSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            synchronized (SoundManager.class) {
                if (instance == null) {
                    instance = new SoundManager();
                }
            }
        }
        return instance;
    }

    private void loadSounds() {
        try {
            bgmClip = loadClip("src/videos/bgm.wav");
            bossBgmClip = loadClip("src/videos/bgm_boss.wav");
            hitClip = loadClip("src/videos/bullet_hit.wav");
            bombClip = loadClip("src/videos/bomb_explosion.wav");
            powerUpClip = loadClip("src/videos/get_supply.wav");
            gameOverClip = loadClip("src/videos/game_over.wav");
        } catch (Exception e) {
        }
    }

    private Clip loadClip(String filePath) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        return clip;
    }

    public void playBGM() {
        stopBGM();
        if (bgmClip != null && !isBossActive) {
            setClipVolume(bgmClip, bgmVolume);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.setFramePosition(0);
        }
    }

    public void playBossBGM() {
        stopBGM();
        isBossActive = true;
        if (bossBgmClip != null) {
            setClipVolume(bossBgmClip, bgmVolume);
            bossBgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBossBGM() {
        if (bossBgmClip != null && bossBgmClip.isRunning()) {
            bossBgmClip.stop();
            bossBgmClip.setFramePosition(0);
        }
        isBossActive = false;
        playBGM();
    }

    public void playHitSound() {
        playSound(hitClip);
    }

    public void playBombSound() {
        playSound(bombClip);
    }

    public void playPowerUpSound() {
        playSound(powerUpClip);
    }

    public void playLevelUpSound() {
        playSound(gameOverClip);
    }

    public void playGameOverSound() {
        stopBGM();
        stopBossBGM();
        playSound(gameOverClip);
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            setClipVolume(clip, soundEffectVolume);
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void setClipVolume(Clip clip, float volume) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            } catch (Exception e) {
            }
        }
    }

    public void setBgmVolume(float volume) {
        this.bgmVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (bgmClip != null && bgmClip.isRunning()) {
            setClipVolume(bgmClip, bgmVolume);
        }
        if (bossBgmClip != null && bossBgmClip.isRunning()) {
            setClipVolume(bossBgmClip, bgmVolume);
        }
    }

    public float getBgmVolume() {
        return bgmVolume;
    }

    public void setSoundEffectVolume(float volume) {
        this.soundEffectVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    public float getSoundEffectVolume() {
        return soundEffectVolume;
    }

    public void close() {
        stopBGM();
        stopBossBGM();
        if (bgmClip != null) bgmClip.close();
        if (bossBgmClip != null) bossBgmClip.close();
        if (hitClip != null) hitClip.close();
        if (bombClip != null) bombClip.close();
        if (powerUpClip != null) powerUpClip.close();
        if (gameOverClip != null) gameOverClip.close();
    }
}