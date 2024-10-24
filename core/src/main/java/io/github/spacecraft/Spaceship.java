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
import com.badlogic.gdx.utils.viewport.Viewport;

public class Spaceship {
    private Sprite sprite;
    private Texture texture;
    private int harvestCount;
    private ShapeRenderer tractorBeam;
    protected Boolean isHarvesting;
    public Vector2 spaceshipCoords, asteroidCoords;
    private Viewport viewport;
    private Preferences preferences;
    private int asteroidBalance;
    private GameHUD gameHUD;
    public float spaceshipX, spaceshipY;
    public Rectangle spaceRect;
    public float tractorIdleCharge;
    public int tractorIdleLevel;
    public int tractorClickLevel;
    public int navigatorLevel;
    public int harvestTimeLevel;

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
        asteroidBalance = preferences.getInteger("asteroidBalance", 0);
        tractorClickLevel = 1;
        tractorIdleLevel = 0;
        tractorIdleCharge = 100f;
        navigatorLevel = 0;
        harvestTimeLevel = 0;
    }

    public void draw(SpriteBatch batch, float worldWidth) {

        batch.draw(sprite, spaceshipX, spaceshipY,  sprite.getWidth(), sprite.getHeight()); // draw spaceship in center of screen

        // store spaceship coords
        spaceshipCoords = new Vector2(spaceshipX + sprite.getWidth() / 2, spaceshipY + sprite.getHeight() / 2);
    }

    public int getHarvestCount() {
        return harvestCount;
    }

    public void incrementHarvestCount() {
        harvestCount++;
        asteroidBalance++;
        preferences.putInteger("asteroidBalance", asteroidBalance);
        preferences.flush();
        gameHUD.updateAsteroidBalanceLabel(asteroidBalance);
    }

    public void harvestAsteroid(AsteroidManager asteroidManager) {
        if (harvestCount > 0) {
            isHarvesting = true;
            Asteroid harvestedAsteroid = asteroidManager.selectLargestAsteroid(this);
            if (harvestedAsteroid != null) {
                asteroidCoords = new Vector2(
                    harvestedAsteroid.getSprite().getX() + harvestedAsteroid.getSprite().getWidth() / 2,
                    harvestedAsteroid.getSprite().getY() + harvestedAsteroid.getSprite().getHeight() / 2
                );
                drawTractorBeam();
                harvestCount--;
            } else {
                // if no asteroid to harvest
                System.out.println("No asteroid available !!!!!!!!");
                isHarvesting = false;
                return;
            }
            tractorIdleCharge += 100f;
        }
    }

    public void drawTractorBeam() {
        if (isHarvesting) {
            tractorBeam.setProjectionMatrix(viewport.getCamera().combined);
            tractorBeam.begin(ShapeRenderer.ShapeType.Line);
            tractorBeam.setColor(Color.RED);
            tractorBeam.line(spaceshipCoords.x, spaceshipCoords.y, asteroidCoords.x, asteroidCoords.y);
            Gdx.gl.glLineWidth(4f); // set the tractor beam width
            tractorBeam.end();
        }
    }

    public void dispose() {
        tractorBeam.dispose(); // dispose of tractor beam when no longer needed
    }

    public boolean tractorUpdate(String updateType) {
        if(harvestCount<=0) return false;
        switch(updateType) {
            case("tick"): {
                tractorIdleCharge -= Gdx.graphics.getDeltaTime()*(1+(0.05f * tractorIdleLevel));
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

}
