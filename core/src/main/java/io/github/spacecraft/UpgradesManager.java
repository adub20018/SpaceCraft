package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UpgradesManager {
    private Preferences preferences;
    private int asteroidsBalance;
    private int clickLevel;
    private int asteroidScanner;
    private int idleLevel;
    private int navigatorLevel;
    private int harvestTimeLevel;
    private boolean isScanner;

    private Spaceship spaceship;
    public UpgradesManager(Spaceship spaceship) {
        // get stats
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");

        asteroidsBalance = preferences.getInteger("asteroidsBalance", 0);
        clickLevel = preferences.getInteger("clickLevel", 1);
        idleLevel = preferences.getInteger("idleLevel", 0);
        navigatorLevel = preferences.getInteger("navigatorLevel",0);
        harvestTimeLevel = preferences.getInteger("harvestTimeLevel",0);
        asteroidScanner = preferences.getInteger("asteroidScanner", 0);
        isScanner = preferences.getBoolean("isScanner",false);
        preferences.getInteger("harvestCount", 1);

        this.spaceship = spaceship;

        init();
    }

    public void init() {
        spaceship.setClickLevel(clickLevel);
        spaceship.setIdleLevel(idleLevel);
        spaceship.setNavigatorLevel(navigatorLevel);
        spaceship.setHarvestTimeLevel(harvestTimeLevel);
        spaceship.isScanner = isScanner;
    }


    // *******************
    // Upgrades section
    // *******************

    public void upgradeClickLevel() {
        // check asteroids balance

        // subtract cost from balance

        int value = clickLevel+=1;
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
        int value = idleLevel+=1;
        System.out.println("Idle Level: " + value);
        // calculate level increase formula
        // call spaceship.setClickLevel
        spaceship.setIdleLevel(value);
        System.out.println("Upgrading Idle Level !!!!!!!");
        preferences.putInteger("IdleLevel", value);
        preferences.flush();
    }

    public void upgradeNavigation() {
        int value = navigatorLevel+=1;
        System.out.println("Navigator Level: " + value);
        // calculate level increase formula
        // call spaceship.setClickLevel
        spaceship.setNavigatorLevel(value);
        System.out.println("Upgrading Navigator Level !!!!!!!");
        preferences.putInteger("NavigatorLevel", value);
        preferences.flush();
    }

    public void upgradeHarvestTime() {
        int value = harvestTimeLevel+=1;
        System.out.println("Harvest Time Level: " + value);
        // calculate level increase formula
        // call spaceship.setClickLevel
        spaceship.setHarvestTimeLevel(value);
        System.out.println("Upgrading harvest time Level !!!!!!!");
        preferences.putInteger("harvestTimeLevel", value);
        preferences.flush();
    }

    // *******************
    // Laboratory section
    // *******************
    public void upgradeTractorQuantity() {
        spaceship.setHarvestCount(spaceship.getHarvestCount()+1);
        preferences.putInteger("harvestCount", spaceship.getHarvestCount());
        preferences.flush();
    }

    public void upgradeScanner() {
        System.out.println("Scanner upgraded!!!");
        spaceship.isScanner = true;
        preferences.putBoolean("isScanner", true);
        preferences.flush();
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
