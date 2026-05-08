package edu.hitsz.application;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private Clip bgmClip;
    private Clip bossBgmClip;
    private Clip hitClip;
    private Clip bombClip;
    private Clip powerUpClip;
    private Clip gameOverClip;

    private boolean isBossActive = false;

    public SoundManager() {
        loadSounds();
    }

    private void loadSounds() {
        try {
            bgmClip = loadClip("src/sounds/bgm.wav");
            bossBgmClip = loadClip("src/sounds/boss_bgm.wav");
            hitClip = loadClip("src/sounds/hit.wav");
            bombClip = loadClip("src/sounds/bomb.wav");
            powerUpClip = loadClip("src/sounds/powerup.wav");
            gameOverClip = loadClip("src/sounds/gameover.wav");
        } catch (Exception e) {
            System.out.println("音效文件加载失败: " + e.getMessage());
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

    public void playGameOverSound() {
        stopBGM();
        stopBossBGM();
        playSound(gameOverClip);
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
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