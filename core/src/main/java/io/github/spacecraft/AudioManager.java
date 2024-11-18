package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;

public class AudioManager {
    private static AudioManager instance;
    private Music themeMusic;
    private Sound asteroidDestroyedSound;
    private Sound tractorBeamSound;
    private Sound spaceshipClickSound;
    private Sound chipFailSound;
    private Sound chipSuccessSound;

    public boolean soundEffectsMuted;

    private Music music;
    private final List<Sound> soundEffects = new ArrayList<>();

    public AudioManager() {
        themeMusic = Gdx.audio.newMusic(Gdx.files.internal("spacecraft_theme.wav"));
        asteroidDestroyedSound = Gdx.audio.newSound(Gdx.files.internal("asteroid_destroyed.wav"));
        tractorBeamSound = Gdx.audio.newSound(Gdx.files.internal("tractor_beam.wav"));
        spaceshipClickSound = Gdx.audio.newSound(Gdx.files.internal("spaceship_click.wav"));
        chipFailSound = Gdx.audio.newSound(Gdx.files.internal("chip_fail.wav"));
        chipSuccessSound = Gdx.audio.newSound(Gdx.files.internal("chip_success.wav"));
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void addMusic(Music music) {
        music = music;
    }
    public void addSoundEffect(Sound sound) {
        soundEffects.add(sound);
    }

    public void muteMusic() {
        // mute music
        stopThemeMusic();
    }

    public void muteSoundEffects() {
        // mute sounds
        soundEffectsMuted = true;
        asteroidDestroyedSound.stop();
        tractorBeamSound.stop();
        spaceshipClickSound.stop();
    }
    public void unmuteSoundEffects() {
        soundEffectsMuted = false;
    }

    public void playThemeMusic() {
        themeMusic.setLooping(true);
        themeMusic.play();
    }
    public void stopThemeMusic() {
        themeMusic.stop();
    }

    public void playAsteroidDestroyedSound() {
        asteroidDestroyedSound.play();
    }

    public void playTractorBeamSound() {
        tractorBeamSound.play();
        tractorBeamSound.loop();
    }
    public void stopTractorBeamSound() {
        tractorBeamSound.stop();
    }

    public void playSpaceshipClickSound() {
        spaceshipClickSound.play();
    }

    public void playChipSuccessSound() {
        chipSuccessSound.play();
    }
    public void playChipFailSound() {
        chipFailSound.play();
    }
}
