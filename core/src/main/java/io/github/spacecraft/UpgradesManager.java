package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

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
    private int refinePowerLevel;
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
        refinePowerLevel = preferences.getInteger("refinePowerLevel", 0);
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
        spaceship.setRefinePowerLevel(refinePowerLevel);
        spaceship.setScannerLevel(scannerLevel);
        spaceship.setTritaniumBalance(tritaniumBalance);
        spaceship.setGravititeBalance(gravititeBalance);
        spaceship.setCubaneBalance(cubaneBalance);
        System.out.println("IDLE LEVEL: "+spaceship.tractorIdleLevel);
    }

    public void updateSpaceshipValues() {
        spaceship.setClickLevel(clickLevel);
        spaceship.setIdleLevel(idleLevel);
        spaceship.setNavigatorLevel(navigatorLevel);
        spaceship.setHarvestTimeLevel(harvestTimeLevel);
        spaceship.isScanner = isScanner;
        spaceship.setTractorQuantityLevel(tractorQuantityLevel);
        spaceship.setHarvestCount(tractorQuantityLevel);
        spaceship.setRefineQualityLevel(refineQualityLevel);
        spaceship.setRefinePowerLevel(refinePowerLevel);
        spaceship.setScannerLevel(scannerLevel);
        spaceship.setTritaniumBalance(tritaniumBalance);
        spaceship.setGravititeBalance(gravititeBalance);
        spaceship.setCubaneBalance(cubaneBalance);
    }


    // *******************
    // Upgrades section
    // *******************

    public void upgradeClickLevel() {
        // check asteroids balance
        gravititeBalance = spaceship.getGravititeBalance();
        // subtract cost from balance
        if(gravititeBalance>=Costs.getClickLevelCost(clickLevel)[0]){
            int newGravititeBalance = gravititeBalance -= Costs.getClickLevelCost(clickLevel)[0];
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
        updateSpaceshipValues();
    }

    public void upgradeIdleCharge() {
        tritaniumBalance = spaceship.getTritaniumBalance();

        if(tritaniumBalance>=Costs.getIdleLevelCost(idleLevel)[1]) {
            int newTritaniumBalance = tritaniumBalance -= Costs.getIdleLevelCost(idleLevel)[1];
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

        if(cubaneBalance>=Costs.getNavigatorLevelCost(navigatorLevel)[2]) {
            int newCubaneBalance = cubaneBalance -= Costs.getNavigatorLevelCost(navigatorLevel)[2];
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
        if(tritaniumBalance>=Costs.getHarvestTimeLevelCost(harvestTimeLevel)[1]) {
            int newTritaniumBalance = tritaniumBalance -= Costs.getHarvestTimeLevelCost(harvestTimeLevel)[1];
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
        tritaniumBalance = spaceship.getTritaniumBalance();
        cubaneBalance = spaceship.getCubaneBalance();
        if(tritaniumBalance>=Costs.getTractorQuantityCost(tractorQuantityLevel)[0]&&
            cubaneBalance>=Costs.getTractorQuantityCost(tractorQuantityLevel)[2]) {
            int newTritaniumBalance = tritaniumBalance -= Costs.getHarvestTimeLevelCost(harvestTimeLevel)[1];
            int newCubaneBalance = cubaneBalance -= Costs.getNavigatorLevelCost(navigatorLevel)[2];
            int newTractorQuantityLevel = tractorQuantityLevel+=1;
            spaceship.setTractorQuantityLevel(newTractorQuantityLevel);
            spaceship.setHarvestCount(spaceship.getHarvestCount()+1);
            spaceship.setTritaniumBalance(newTritaniumBalance);
            spaceship.setCubaneBalance(newCubaneBalance);
            preferences.putInteger("tractorQuantityLevel", newTractorQuantityLevel );
            preferences.putInteger("harvestCount", spaceship.getHarvestCount());
            preferences.flush();
        }
    }

    public void upgradeScanner() {
        gravititeBalance = spaceship.getGravititeBalance();
        tritaniumBalance = spaceship.getTritaniumBalance();
        cubaneBalance = spaceship.getCubaneBalance();
        if(isScanner){
            System.out.println("already upgraded scanner");
            return;
        }
        int[] harvestScannerCost = Costs.getHarvestScannerCost(scannerLevel);
        if (gravititeBalance>=harvestScannerCost[0]&&
            tritaniumBalance>=harvestScannerCost[1]&&
            cubaneBalance>=harvestScannerCost[2]) {

            int newGravititeBalance = gravititeBalance -= harvestScannerCost[0];
            int newTritaniumBalance = tritaniumBalance -= harvestScannerCost[1];
            int newCubaneBalance = cubaneBalance -= harvestScannerCost[2];
            int newScannerLevel = scannerLevel+=1;
            spaceship.setScannerLevel(newScannerLevel);
            spaceship.setGravititeBalance(newGravititeBalance);
            spaceship.setTritaniumBalance(newTritaniumBalance);
            spaceship.setCubaneBalance(newCubaneBalance);
            preferences.putInteger("scannerLevel", newScannerLevel);
            System.out.println("Scanner upgraded!!!");
            spaceship.isScanner = true;
            isScanner = true;
            preferences.putBoolean("isScanner", true);
            preferences.flush();
        }
    }

    public void upgradeRefineryQuality() {
        gravititeBalance = spaceship.getGravititeBalance();
        cubaneBalance = spaceship.getCubaneBalance();
        gravititeBalance = spaceship.getGravititeBalance();
        cubaneBalance = spaceship.getCubaneBalance();
        if(gravititeBalance>=Costs.getRefineryQualityCost(refineQualityLevel)[0]&&
            cubaneBalance>=Costs.getRefineryQualityCost(refineQualityLevel)[2]) {

            int newGravititeBalance = gravititeBalance -= Costs.getRefineryQualityCost(refineQualityLevel)[0];
            int newCubaneBalance = cubaneBalance -= Costs.getRefineryQualityCost(refineQualityLevel)[2];
            int newRefineQualityLevel = refineQualityLevel += 1;

            spaceship.setRefineQualityLevel(newRefineQualityLevel);
            spaceship.setGravititeBalance(newGravititeBalance);
            spaceship.setCubaneBalance(newCubaneBalance);
            preferences.putInteger("refineQualityLevel", newRefineQualityLevel);
            preferences.flush();
        }
    }

    public void upgradeRefinePower() {
        gravititeBalance = spaceship.getGravititeBalance();
        cubaneBalance = spaceship.getCubaneBalance();
        if(gravititeBalance>=Costs.getRefinePowerCost(refinePowerLevel)[0]&&
            cubaneBalance>=Costs.getRefinePowerCost(refinePowerLevel)[2]) {

            int newGravititeBalance = gravititeBalance -= Costs.getRefinePowerCost(refinePowerLevel)[0];
            int newCubaneBalance = cubaneBalance -= Costs.getRefinePowerCost(refinePowerLevel)[2];
            int newRefinePowerLevel = refinePowerLevel += 1;


            spaceship.setRefinePowerLevel(newRefinePowerLevel);
            spaceship.setGravititeBalance(newGravititeBalance);
            spaceship.setCubaneBalance(newCubaneBalance);
            preferences.putInteger("refinePowerLevel", newRefinePowerLevel);
            preferences.flush();
        }
    }

    public void doRefine(boolean firstrefine) {
        asteroidsBalance = spaceship.getAsteroidBalance();
        //System.out.println(asteroidsBalance);
        if(asteroidsBalance>0) {
            int roll = MathUtils.random(0, 100);
            int newAsteroidsBalance = asteroidsBalance -= 1;
            int newTritaniumBalance = tritaniumBalance;
            int newGravititeBalance = gravititeBalance;
            int newCubaneBalance = cubaneBalance;
            if (roll > (99 - refineQualityLevel)) {
                // Special case, get 5 of all
                newTritaniumBalance = tritaniumBalance += 5;
                newGravititeBalance = gravititeBalance += 5;
                newCubaneBalance = cubaneBalance += 5;
            }else if (roll < 50 + refineQualityLevel) {
                // success, chose either tritanium or gravitite
                roll = MathUtils.random(0, 10);
                if(roll%2==1)newTritaniumBalance = tritaniumBalance += 1;
                if(roll%2==0)newGravititeBalance = gravititeBalance += 1;
                if(roll==10) newCubaneBalance = cubaneBalance += 1;
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
            if(firstrefine) {
                for(int i = 1; i<= refinePowerLevel; i++) {
                    doRefine(false);
                }
            spaceship.updateValues();
            }
        }
    }
    public void resetStats() {
        if(spaceship.isHarvesting) return;
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
        preferences.putInteger("refinePowerLevel", 0);
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
        refinePowerLevel =    preferences.getInteger("refinePowerLevel", 0);
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
        spaceship.setRefinePowerLevel(refinePowerLevel);
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
    public int getRefinePowerLevel() {
        return refinePowerLevel;
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
