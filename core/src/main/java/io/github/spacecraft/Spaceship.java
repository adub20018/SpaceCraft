package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Spaceship {
    private Sprite sprite;
    private Texture texture;
    private int harvestCount;
    private ShapeRenderer tractorBeam;
    protected Boolean isHarvesting;
    public Vector2 spaceshipCoords;
    public SnapshotArray<Vector2> asteroidCoords;
    private Viewport viewport;
    private Preferences preferences;
    private int asteroidBalance;
    private GameHUD gameHUD;
    private GameMenu gameMenu;
    public float spaceshipX, spaceshipY;
    public Rectangle spaceRect;
    public float tractorIdleCharge;
    public int tractorIdleLevel;
    public int tractorClickLevel;
    public int navigatorLevel;
    public int harvestTimeLevel;
    public boolean isScanner;
    public int tractorQuantityLevel;
    private int refineQualityLevel;
    private int refinePowerLevel;
    private int scannerLevel;
    private int tritaniumBalance, gravititeBalance, cubaneBalance;

    private Asteroid chippedAsteroid;
    private int hasfailed;

    public Spaceship(Viewport viewport, GameHUD gameHUD) {
        this.viewport = viewport;
        texture = new Texture("spaceship.png");
        sprite = new Sprite(texture);
        sprite.setSize(1.2f, 1.2f); // set size for spaceship in world units
        harvestCount = 1;
        tractorBeam = new ShapeRenderer();
        isHarvesting = false;
        this.gameHUD = gameHUD;
        spaceshipX = (viewport.getWorldWidth() - sprite.getWidth()) / 2;
        spaceshipY = 3.5f;
        spaceRect = new Rectangle(spaceshipX, spaceshipY,  sprite.getWidth(), sprite.getHeight());
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");
        asteroidBalance = 0;
        tritaniumBalance = 0;
        gravititeBalance = 0;
        cubaneBalance = 0;
        tractorClickLevel = 1;
        tractorIdleLevel = 0;
        tractorIdleCharge = 100f;
        navigatorLevel = 0;
        harvestTimeLevel = 0;
        isScanner = false;
        asteroidCoords = new SnapshotArray<>();
        tractorQuantityLevel = 1;
        refineQualityLevel = 1;
        refinePowerLevel = 0;

        // updateValues();
    }

    public void draw(SpriteBatch batch, float worldWidth) {
        batch.draw(sprite, spaceshipX, spaceshipY,  sprite.getWidth(), sprite.getHeight()); // draw spaceship in center of screen

        // store spaceship coords
        spaceshipCoords = new Vector2(spaceshipX + sprite.getWidth() / 2, spaceshipY + sprite.getHeight() / 2);
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);

        spaceshipX = (viewport.getWorldWidth() - width) / 2;
        spaceshipY = 3.5f;
        spaceRect.set(spaceshipX, spaceshipY,  sprite.getWidth(), sprite.getHeight());
    }

    public int getHarvestCount() {
        return harvestCount;
    }
    public void setHarvestCount(int harvestCount) {
        this.harvestCount = harvestCount;
    }

    public void incrementHarvestCount(int rarity) {
        if (!AudioManager.getInstance().soundEffectsMuted) {
            AudioManager.getInstance().playAsteroidDestroyedSound();
        }
        harvestCount++;
        switch(rarity) {
            case(1):  // Common
                asteroidBalance+=10;
                break;
            case(2): // Uncommon
                asteroidBalance+=50;
                break;
            case(3): // Rare
                asteroidBalance+=250;
                break;
            case(4): // Epic
                asteroidBalance+=1250;
                break;
            case(5): // Legendary
                asteroidBalance+=10000;
                break;
        }
        //asteroidBalance++;
        gameHUD.updateAsteroidBalanceLabel(asteroidBalance);
        asteroidCoords.begin();
        asteroidCoords.removeIndex(0);
        asteroidCoords.end();

        if (asteroidCoords.size == 0) {
            AudioManager.getInstance().stopTractorBeamSound();
        }
    }

    public void harvestAsteroid(AsteroidManager asteroidManager) {
        tractorIdleCharge += 100f;
        if (harvestCount > 0) {
            if (!isHarvesting && !AudioManager.getInstance().soundEffectsMuted) {
                AudioManager.getInstance().playTractorBeamSound();
            }
            isHarvesting = true;
            Asteroid harvestedAsteroid;
            if(!isScanner) {
                harvestedAsteroid = asteroidManager.selectRandomAsteroid(this);
            } else {
                harvestedAsteroid = asteroidManager.selectLargestAsteroid(this);
            }

            if (harvestedAsteroid != null) {
                asteroidCoords.begin();
                asteroidCoords.add(new Vector2(
                    harvestedAsteroid.getSprite().getX() + harvestedAsteroid.getSprite().getWidth() / 2,
                    harvestedAsteroid.getSprite().getY() + harvestedAsteroid.getSprite().getHeight() / 2
                ));
                asteroidCoords.end();
                harvestCount--;
            } else {
                // if no asteroid to harvest
                System.out.println("No asteroid available !!!!!!!!");
                return;
            }
        }
    }

    public void drawTractorBeam() {
        if (isHarvesting) {
            tractorBeam.setProjectionMatrix(viewport.getCamera().combined);
            tractorBeam.begin(ShapeRenderer.ShapeType.Line);
            tractorBeam.setColor(Color.RED);
            for(Vector2 asteroidCoord : asteroidCoords) {
                tractorBeam.line(spaceshipCoords.x+0.15f, spaceshipCoords.y, asteroidCoord.x, asteroidCoord.y);
                tractorBeam.line(spaceshipCoords.x-0.15f, spaceshipCoords.y, asteroidCoord.x, asteroidCoord.y);
                tractorBeam.line(spaceshipCoords.x, spaceshipCoords.y, asteroidCoord.x, asteroidCoord.y);
            }

            Gdx.gl.glLineWidth(12f); // set the tractor beam width
            tractorBeam.end();
        }
    }

    public void dispose() {
        tractorBeam.dispose(); // dispose of tractor beam when no longer needed
    }

    public void updateValues() {
        System.out.println(asteroidBalance+"   "+tritaniumBalance+"   "+gravititeBalance+"   "+cubaneBalance);
        gameHUD.updateAsteroidBalanceLabel(asteroidBalance);
        gameHUD.updateTritaniumBalanceLabel(tritaniumBalance);
        gameHUD.updateGravititeBalanceLabel(gravititeBalance);
        gameHUD.updateCubaneBalanceLabel(cubaneBalance);

        // CLICK LEVEL
        gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
            "Click\nPower", upgradesManager.getClickLevel(),
            Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite",
            upgradesManager.gravititeBalance>=Costs.getClickLevelCost(upgradesManager.getClickLevel())[0]);


        // IDLE CHARGE
        gameMenu.updateButtonContent(gameMenu.upgradeIdleCharge,
            "Idle\nCharge", upgradesManager.getIdleChargeLevel(),
            Costs.getIdleLevelCost(upgradesManager.getIdleChargeLevel()),"tritanium",
            upgradesManager.tritaniumBalance>=Costs.getIdleLevelCost(upgradesManager.getIdleChargeLevel())[1]);


        // NAVIGATOR
        gameMenu.updateButtonContent(gameMenu.upgradeNavigation,
            "Navigator", upgradesManager.getNavigatorLevel(),
            Costs.getNavigatorLevelCost(upgradesManager.getNavigatorLevel()),"cubane",
            upgradesManager.cubaneBalance>=Costs.getNavigatorLevelCost(upgradesManager.getNavigatorLevel())[2]);

        // HARVEST TIME
        gameMenu.updateButtonContent(gameMenu.upgradeHarvestTimeButton,
            "Harvest\nTime", upgradesManager.getHarvestTimeLevel(),
            Costs.getHarvestTimeLevelCost(upgradesManager.getHarvestTimeLevel()),"tritanium",
            upgradesManager.tritaniumBalance>=Costs.getHarvestTimeLevelCost(upgradesManager.getHarvestTimeLevel())[1]);


        /*
        *  LAB SECTION
        */
        // TRACTOR QUANTITY
        gameMenu.updateButtonContent(gameMenu.upgradeTractorQuantity,
            "Tractor\nQuantity", upgradesManager.getTractorQuantityLevel(),
            Costs.getTractorQuantityCost(upgradesManager.getTractorQuantityLevel()),"tritanium",
            tritaniumBalance>=Costs.getTractorQuantityCost(tractorQuantityLevel)[1]&&
                cubaneBalance>=Costs.getTractorQuantityCost(tractorQuantityLevel)[2]);

        // SCANNER
        gameMenu.updateButtonContent(gameMenu.upgradeScanner,"Harvest\nScanner",upgradesManager.getScannerLevel(),
            Costs.getHarvestScannerCost(upgradesManager.getScannerLevel()),"tritanium",
            gravititeBalance>=Costs.getHarvestScannerCost(scannerLevel)[0]&&
                tritaniumBalance>=Costs.getHarvestScannerCost(scannerLevel)[1]&&
                cubaneBalance>=Costs.getHarvestScannerCost(scannerLevel)[2]&&!isScanner);

        // REFINERY QUALITY
        gameMenu.updateButtonContent(gameMenu.upgradeRefineryQuality, "Refinery\nQuality", upgradesManager.getRefineQualityLevel(),
        Costs.getRefineryQualityCost(upgradesManager.getRefineQualityLevel()),"tritanium",
            gravititeBalance>=Costs.getRefineryQualityCost(refineQualityLevel)[0]&&
                cubaneBalance>=Costs.getRefineryQualityCost(refineQualityLevel)[2]);

        // REFINE POWER
        gameMenu.updateButtonContent(gameMenu.upgradeRefinePower,"Refine\nPower", upgradesManager.getRefinePowerLevel(),
            Costs.getRefinePowerCost(upgradesManager.getRefinePowerLevel()),"tritanium",
            gravititeBalance>=Costs.getRefinePowerCost(refinePowerLevel)[0]&&
                cubaneBalance>=Costs.getRefinePowerCost(refinePowerLevel)[2]);
    }

    public boolean tractorUpdate(String updateType) {
        if(harvestCount<=0) return false;
        switch(updateType) {
            case("tick"): {
                tractorIdleCharge -= Gdx.graphics.getDeltaTime()*(1+(0.5f * tractorIdleLevel));
                gameHUD.updateProgressBar(tractorIdleCharge);
                break;
            }
            case("click"): {
                if (!AudioManager.getInstance().soundEffectsMuted) {
                    AudioManager.getInstance().playSpaceshipClickSound();
                }
                tractorIdleCharge -= 10 + (tractorClickLevel * 0.25f);
                gameHUD.updateProgressBar(tractorIdleCharge);
                break;
            }
        }
        return tractorIdleCharge <= 0;
    }

    public void setClickLevel(int value) {
        tractorClickLevel = value;
    }
    public void setIdleLevel(int tractorIdleLevel) {
        this.tractorIdleLevel = tractorIdleLevel;
    }
    public void setNavigatorLevel(int navigatorLevel) {
        this.navigatorLevel = navigatorLevel;
    }
    public int getHarvestTimeLevel() {
        return harvestTimeLevel;
    }

    public void setHarvestTimeLevel(int harvestTimeLevel) {
        this.harvestTimeLevel = harvestTimeLevel;
    }

    public void setTractorQuantityLevel(int currentLevel) {
        tractorQuantityLevel = currentLevel;
    }

    public void setRefineQualityLevel(int currentLevel) {
        refineQualityLevel = currentLevel;
    }

    public void setRefinePowerLevel(int currentLevel) {
        refinePowerLevel = currentLevel;
    }

    public void setScannerLevel(int currentLevel) {
        scannerLevel = currentLevel;
    }


    public int getTritaniumBalance() {
        return tritaniumBalance;
    }

    public void setTritaniumBalance(int tritaniumBalance) {
        this.tritaniumBalance = tritaniumBalance;
    }

    public int getGravititeBalance() {
        return gravititeBalance;
    }

    public void setGravititeBalance(int gravititeBalance) {
        this.gravititeBalance = gravititeBalance;
    }

    public int getCubaneBalance() {
        return cubaneBalance;
    }

    public void setCubaneBalance(int cubaneBalance) {
        this.cubaneBalance = cubaneBalance;
    }

    public void setAsteroidBalance(int asteroidsBalance) {
        this.asteroidBalance = asteroidsBalance;
    }

    public int getAsteroidBalance() {
        return asteroidBalance;
    }

    public void setGameMenu(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    public void setUpgradesManager(UpgradesManager upgradesManager) {
        this.upgradesManager = upgradesManager;
    }

    private UpgradesManager upgradesManager;

    public void chipAsteroid(Asteroid asteroid) {
        chippedAsteroid = asteroid;
        if (MathUtils.random() > 0.95f||hasfailed>20) {
            hasfailed = 0;
            if (!AudioManager.getInstance().soundEffectsMuted) {
                AudioManager.getInstance().playChipSuccessSound();
            }
            switch (asteroid.getRarity()) {
                case (1):  // Common
                    asteroidBalance++;
                    break;
                case (2): // Uncommon
                    asteroidBalance += 5;
                    break;
                case (3): // Rare
                    asteroidBalance += 25;
                    break;
                case (4): // Epic
                    asteroidBalance += 125;
                    break;
                case (5): // Legendary
                    asteroidBalance += 1000;
                    break;
            }
            //asteroidBalance++;
            gameHUD.updateAsteroidBalanceLabel(asteroidBalance);
        } else {
            hasfailed++;
            if (!AudioManager.getInstance().soundEffectsMuted) {
                AudioManager.getInstance().playChipFailSound();
            }
        }

    }

    public void drawChipBeam() {
        if(chippedAsteroid != null) {
            tractorBeam.setProjectionMatrix(viewport.getCamera().combined);
            tractorBeam.begin(ShapeRenderer.ShapeType.Line);
            tractorBeam.setColor(Color.GREEN);
            tractorBeam.line(spaceshipCoords.x, spaceshipCoords.y,
                chippedAsteroid.getSprite().getX() + chippedAsteroid.getSprite().getWidth() / 2,
                chippedAsteroid.getSprite().getY() + chippedAsteroid.getSprite().getHeight() / 2
            );
            Gdx.gl.glLineWidth(12f);
            tractorBeam.end();
            chippedAsteroid = null;
        }
    }
}
