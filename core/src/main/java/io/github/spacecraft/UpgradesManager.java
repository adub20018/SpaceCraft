package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UpgradesManager {
    private Preferences preferences;
    private int asteroidsBalance;
    private int clickLevel;
    private int asteroidScanner;

    private Spaceship spaceship;
    public UpgradesManager(Spaceship spaceship) {
        // get stats
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");

        asteroidsBalance = preferences.getInteger("asteroidsBalance", 0);
        clickLevel = preferences.getInteger("clickLevel", 0);
        asteroidScanner = preferences.getInteger("asteroidScanner", 0);

        this.spaceship = spaceship;

        init();
    }

    public void init() {

    }

    public void upgradeClickLevel() {
        // check asteroids balance

        // subtract cost from balance

        int value = clickLevel += 2;
        System.out.println("Click Level: " + value);
        // calculate level increase formula

        // call spaceship.setClickLevel
        spaceship.setClickLevel(value);
        System.out.println("Upgrading Click Level !!!!!!!");

        preferences.putInteger("clickLevel", value);
        preferences.flush();

        // then spaceship.getClickLevel will be passed to asteroidManager
    }

    public void upgradeIdleCharge() {
    }

    public void upgradeNavigation() {
    }

    public void upgradeHarvestTime() {
    }


    // Laboratory section
    public void upgradeTractorQuantity() {
    }

    public void upgradeScanner() {
    }

    public void upgradeRefineryQuality() {
    }

    public void upgradeToAutoRefine() {
    }

    // upgrade scanner -> call spaceship.setUpgradeScanner

    // do the upgrade logic -> call spaceship.setUpgrade

    // call set click level (called in spaceship constructor)
    // on upgrade- update spaceship

}
