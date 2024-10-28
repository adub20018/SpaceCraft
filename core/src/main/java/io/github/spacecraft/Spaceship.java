package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private int autoRefineLevel;
    private int scannerLevel;
    private int tritaniumBalance, gravititeBalance, cubaneBalance;

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
        autoRefineLevel = 0;
//        updateValues();
    }

    public void draw(SpriteBatch batch, float worldWidth) {

        batch.draw(sprite, spaceshipX, spaceshipY,  sprite.getWidth(), sprite.getHeight()); // draw spaceship in center of screen

        // store spaceship coords
        spaceshipCoords = new Vector2(spaceshipX + sprite.getWidth() / 2, spaceshipY + sprite.getHeight() / 2);
    }

    public int getHarvestCount() {
        return harvestCount;
    }
    public void setHarvestCount(int harvestCount) {
        this.harvestCount = harvestCount;
    }

    public void incrementHarvestCount() {
        harvestCount++;
        asteroidBalance++;
        gameHUD.updateAsteroidBalanceLabel(asteroidBalance);
        asteroidCoords.begin();
        asteroidCoords.removeIndex(0);
        asteroidCoords.end();

    }

    public void harvestAsteroid(AsteroidManager asteroidManager) {
        tractorIdleCharge += 100f;
        if (harvestCount > 0) {
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
                //isHarvesting = false;
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
                tractorBeam.line(spaceshipCoords.x, spaceshipCoords.y, asteroidCoord.x, asteroidCoord.y);
            }

            Gdx.gl.glLineWidth(4f); // set the tractor beam width
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
        if(upgradesManager.gravititeBalance<Costs.getClickLevelCost(upgradesManager.getClickLevel())) {
            gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
                "Click\nLevel", upgradesManager.getClickLevel(),
                Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", false);
        } else {
        gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
            "Click\nLevel", upgradesManager.getClickLevel(),
            Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", true);
        }


        // IDLE CHARGE
        if(upgradesManager.gravititeBalance<Costs.getClickLevelCost(upgradesManager.getClickLevel())) {
            gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
                "Click\nLevel", upgradesManager.getClickLevel(),
                Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", false);
        } else {
            gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
                "Click\nLevel", upgradesManager.getClickLevel(),
                Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", true);
        }


        // NAVIGATOR
        if(upgradesManager.gravititeBalance<Costs.getClickLevelCost(upgradesManager.getClickLevel())) {
            gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
                "Click\nLevel", upgradesManager.getClickLevel(),
                Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", false);
        } else {
            gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
                "Click\nLevel", upgradesManager.getClickLevel(),
                Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", true);
        }


        // HARVEST TIME
        if(upgradesManager.gravititeBalance<Costs.getClickLevelCost(upgradesManager.getClickLevel())) {
            gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
                "Click\nLevel", upgradesManager.getClickLevel(),
                Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", false);
        } else {
            gameMenu.updateButtonContent(gameMenu.upgradeClickLevel,
                "Click\nLevel", upgradesManager.getClickLevel(),
                Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", true);
        }


    }

    public boolean tractorUpdate(String updateType) {
        if(harvestCount<=0) return false;
        switch(updateType) {
            case("tick"): {
                tractorIdleCharge -= Gdx.graphics.getDeltaTime()*(1+(0.5f * tractorIdleLevel));
                //System.out.println(tractorIdleCharge);
                //System.out.println(tractorIdleLevel);
                break;
            }
            case("click"): {
                tractorIdleCharge -= 10 + (tractorClickLevel * 0.25f);
                break;
            }
        }
        // System.out.println(tractorIdleCharge);
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

    public void setAutoRefineLevel(int currentLevel) {
        autoRefineLevel = currentLevel;
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


}
