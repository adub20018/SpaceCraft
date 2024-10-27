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
    private int tractorQuantityLevel;
    private int refineQualityLevel;
    private int autoRefineLevel;
    private int scannerLevel;

    private Spaceship spaceship;
    public UpgradesManager(Spaceship spaceship) {
        // get stats
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");
        preferences.putInteger("asteroidsBalance", 0);
        preferences.putInteger("clickLevel", 1);
        preferences.putInteger("idleLevel", 0);
        preferences.putInteger("navigatorLevel",0);
        preferences.putInteger("harvestTimeLevel",0);
        preferences.putInteger("asteroidScanner", 0);
        preferences.putBoolean("isScanner",false);
        preferences.putInteger("harvestCount", 1);
        preferences.putInteger("tractorQuantityLevel", 1);
        preferences.flush();
        asteroidsBalance = preferences.getInteger("asteroidsBalance", 0);
        clickLevel = preferences.getInteger("clickLevel", 1);
        idleLevel = preferences.getInteger("idleLevel", 0);
        navigatorLevel = preferences.getInteger("navigatorLevel",0);
        harvestTimeLevel = preferences.getInteger("harvestTimeLevel",0);
        asteroidScanner = preferences.getInteger("asteroidScanner", 0);
        isScanner = preferences.getBoolean("isScanner",false);
        tractorQuantityLevel = preferences.getInteger("tractorQuantityLevel", 1);
        refineQualityLevel = preferences.getInteger("refineQualityLevel", 1);
        autoRefineLevel = preferences.getInteger("autoRefineLevel", 0);
        scannerLevel = preferences.getInteger("scannerLevel", 0);
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
        spaceship.setTractorQuantityLevel(tractorQuantityLevel);
        spaceship.setHarvestCount(tractorQuantityLevel);
        spaceship.setRefineQualityLevel(refineQualityLevel);
        spaceship.setAutoRefineLevel(autoRefineLevel);
        spaceship.setScannerLevel(scannerLevel);
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
        int newTractorQuantityLevel = tractorQuantityLevel+=1;
        spaceship.setTractorQuantityLevel(newTractorQuantityLevel);
        spaceship.setHarvestCount(spaceship.getHarvestCount()+1);
        preferences.putInteger("tractorQuantityLevel", newTractorQuantityLevel );
        preferences.putInteger("harvestCount", spaceship.getHarvestCount());
        preferences.flush();
    }

    public void upgradeScanner() {
        int newScannerLevel = scannerLevel+=1;
        spaceship.setScannerLevel(newScannerLevel);
        preferences.putInteger("scannerLevel", newScannerLevel);
        System.out.println("Scanner upgraded!!!");
        spaceship.isScanner = true;
        preferences.putBoolean("isScanner", true);
        preferences.flush();
    }

    public void upgradeRefineryQuality() {
        int newRefineQualityLevel = refineQualityLevel+=1;
        spaceship.setRefineQualityLevel(newRefineQualityLevel);
        preferences.putInteger("refineQualityLevel", newRefineQualityLevel);
        preferences.flush();
    }

    public void upgradeToAutoRefine() {
        int newAutoRefineLevel = autoRefineLevel+=1;
        spaceship.setRefineQualityLevel(newAutoRefineLevel);
        preferences.putInteger("autoRefineLevel", newAutoRefineLevel);
        preferences.flush();
    }

    // upgrades
    public int getClickLevel() {
        return clickLevel;
    }
    public int getIdleChargeLevel() {
        return idleLevel;
    }
    public int getNavigatorLevel() {
        return navigatorLevel;
    }
    public int getHarvestTimeLevel() {
        return harvestTimeLevel;
    }

    // laboratory
    public int getTractorQuantityLevel() {
        return tractorQuantityLevel;
    }
    public int getAutoRefineLevel() {
        return autoRefineLevel;
    }
    public int getRefineQualityLevel() {
        return refineQualityLevel;
    }
    public int getScannerLevel() {
        return scannerLevel;
    }
}
