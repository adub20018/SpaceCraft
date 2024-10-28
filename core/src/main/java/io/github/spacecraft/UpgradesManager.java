package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import io.github.spacecraft.Costs;

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
    public int gravititeBalance, tritaniumBalance, cubaneBalance;
    private boolean poppedUp;

    private Spaceship spaceship;
    public UpgradesManager(Spaceship spaceship) {
        // get stats
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");
        asteroidsBalance = preferences.getInteger("asteroidsBalance", 0);
        gravititeBalance = preferences.getInteger("gravititeBalance", 0);
        tritaniumBalance = preferences.getInteger("tritaniumBalance", 0);
        cubaneBalance    = preferences.getInteger("cubaneBalance", 0);
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
        //preferences.getInteger("harvestCount", 1);
        poppedUp = false;
        this.spaceship = spaceship;
        System.out.println("INTIAL TRITANIUM BALANCE: " + tritaniumBalance);
        init();
    }

    public void init() {
        spaceship.setAsteroidBalance(1000);
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
        spaceship.setTritaniumBalance(tritaniumBalance);
        spaceship.setGravititeBalance(gravititeBalance);
        spaceship.setCubaneBalance(cubaneBalance);
        System.out.println("IDLE LEVEL: "+spaceship.tractorIdleLevel);
    }


    // *******************
    // Upgrades section
    // *******************

    public void upgradeClickLevel() {
        // check asteroids balance
        gravititeBalance = spaceship.getGravititeBalance();
        // subtract cost from balance
        if(gravititeBalance>=Costs.getClickLevelCost(clickLevel)){
            int newGravititeBalance = gravititeBalance -= Costs.getClickLevelCost(clickLevel);
            int value = clickLevel+=1;
            System.out.println("Click Level: " + value);
            // calculate level increase formula
            // call spaceship.setClickLevel
            spaceship.setClickLevel(value);
            spaceship.setGravititeBalance(newGravititeBalance);
            System.out.println("Upgrading Click Level !!!!!!!");
            preferences.putInteger("clickLevel", value);
            preferences.flush();
            //spaceship.updateValues();
        }
    }

    public void upgradeIdleCharge() {
        tritaniumBalance = spaceship.getTritaniumBalance();

        if(tritaniumBalance>=Costs.getIdleLevelCost(idleLevel)) {
            int newTritaniumBalance = tritaniumBalance -= Costs.getIdleLevelCost(idleLevel);
            int value = idleLevel+=1;
            System.out.println("Idle Level: " + value);
            // calculate level increase formula
            // call spaceship.setClickLevel
            spaceship.setIdleLevel(value);
            spaceship.setTritaniumBalance(newTritaniumBalance);
            System.out.println("Upgrading Idle Level !!!!!!!");
            preferences.putInteger("idleLevel", value);
            preferences.flush();
        }
    }

    public void upgradeNavigation() {
        cubaneBalance = spaceship.getCubaneBalance();

        if(cubaneBalance>=Costs.getNavigatorLevelCost(navigatorLevel)) {
            int newCubaneBalance = cubaneBalance -= Costs.getNavigatorLevelCost(navigatorLevel);
            int value = navigatorLevel+=1;
            System.out.println("Navigator Level: " + value);
            // calculate level increase formula
            // call spaceship.setClickLevel
            spaceship.setNavigatorLevel(value);
            spaceship.setCubaneBalance(newCubaneBalance);
            System.out.println("Upgrading Navigator Level !!!!!!!");
            preferences.putInteger("navigatorLevel", value);
            preferences.flush();
        }
    }

    public void upgradeHarvestTime() {
        tritaniumBalance = spaceship.getTritaniumBalance();
        if(tritaniumBalance>=Costs.getHarvestTimeLevelCost(harvestTimeLevel)) {
            int newTritaniumBalance = tritaniumBalance -= Costs.getHarvestTimeLevelCost(harvestTimeLevel);
            int value = harvestTimeLevel += 1;
            System.out.println("Harvest Time Level: " + value);
            // calculate level increase formula
            // call spaceship.setClickLevel
            spaceship.setHarvestTimeLevel(value);
            spaceship.setTritaniumBalance(newTritaniumBalance);
            System.out.println("Upgrading harvest time Level !!!!!!!");
            preferences.putInteger("harvestTimeLevel", value);
            preferences.flush();
        }
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

    public void doRefine() {
        asteroidsBalance = spaceship.getAsteroidBalance();
        //System.out.println(asteroidsBalance);
        if(asteroidsBalance>0) {
            int roll = MathUtils.random(0, 100);
            int newAsteroidsBalance = asteroidsBalance -= 1;
            int newTritaniumBalance = tritaniumBalance;
            int newGravititeBalance = gravititeBalance;
            int newCubaneBalance = cubaneBalance;
            if (roll < 50) {
                // success, chose either tritanium or gravitite
                roll = MathUtils.random(0, 10);
                if(roll%2==1)newTritaniumBalance = tritaniumBalance += 1;
                if(roll%2==0)newGravititeBalance = gravititeBalance += 1;
                if(roll==10) newCubaneBalance = cubaneBalance += 1;
            } else if (roll > 99) {
                // Special case, get 5 of all
                newTritaniumBalance = tritaniumBalance += 5;
                newGravititeBalance = gravititeBalance += 5;
                newCubaneBalance = cubaneBalance += 5;
            }
            spaceship.setTritaniumBalance(newTritaniumBalance);
            spaceship.setGravititeBalance(newGravititeBalance);
            spaceship.setCubaneBalance(newCubaneBalance);
            spaceship.setAsteroidBalance(newAsteroidsBalance);
            preferences.putInteger("tritaniumBalance", newTritaniumBalance);
            preferences.putInteger("gravititeBalance", newGravititeBalance);
            preferences.putInteger("cubaneBalance", newCubaneBalance);
            preferences.putInteger("asteroidsBalance", newAsteroidsBalance);
            preferences.flush();
            spaceship.updateValues();
        }
    }
    public void resetStats() {
        preferences.putInteger("asteroidsBalance", 0);
        preferences.putInteger("tritaniumBalance", 0);
        preferences.putInteger("gravititeBalance", 0);
        preferences.putInteger("cubaneBalance", 0);
        preferences.putInteger("clickLevel", 1);
        preferences.putInteger("idleLevel", 0);
        preferences.putInteger("navigatorLevel",0);
        preferences.putInteger("harvestTimeLevel",0);
        preferences.putInteger("asteroidScanner", 0);
        preferences.putBoolean("isScanner",false);
        preferences.putInteger("harvestCount", 1);
        preferences.putInteger("tractorQuantityLevel", 1);
        preferences.putInteger("refineQualityLevel", 1);
        preferences.putInteger("autoRefineLevel", 0);
        preferences.putInteger("scannerLevel", 0);
        preferences.flush();
        asteroidsBalance = preferences.getInteger("asteroidsBalance", 0);
        tritaniumBalance = preferences.getInteger("tritaniumBalance", 0);
        gravititeBalance = preferences.getInteger("gravititeBalance", 0);
        cubaneBalance    = preferences.getInteger("cubaneBalance", 0);
        clickLevel = preferences.getInteger("clickLevel", 1);
        idleLevel = preferences.getInteger("idleLevel", 0);
        navigatorLevel = preferences.getInteger("navigatorLevel",0);
        harvestTimeLevel = preferences.getInteger("harvestTimeLevel",0);
        asteroidScanner = preferences.getInteger("asteroidScanner", 0);
        isScanner = preferences.getBoolean("isScanner",false);
        tractorQuantityLevel = preferences.getInteger("tractorQuantityLevel", 1);
        refineQualityLevel = preferences.getInteger("refineQualityLevel", 1);
        autoRefineLevel =    preferences.getInteger("autoRefineLevel", 0);
        scannerLevel =       preferences.getInteger("scannerLevel", 0);
        preferences.flush();
        spaceship.setAsteroidBalance(asteroidsBalance);
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
        spaceship.setTritaniumBalance(tritaniumBalance);
        spaceship.setGravititeBalance(gravititeBalance);
        spaceship.setCubaneBalance(cubaneBalance);
        spaceship.updateValues();
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




    public boolean isPoppedUp() {
        return poppedUp;
    }
    public void setPoppedUp(boolean poppedUp) {
        this.poppedUp = poppedUp;
    }

    public void updateValues() {
        spaceship.updateValues();
    }
}
