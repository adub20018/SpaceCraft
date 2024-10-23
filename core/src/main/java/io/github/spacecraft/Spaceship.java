package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Spaceship {
    private Sprite sprite;
    private Texture texture;
    private int harvestCount;
    private ShapeRenderer tractorBeam;
    protected Boolean isHarvesting;
    private Vector2 spaceshipCoords, asteroidCoords;
    private Viewport viewport;
    private Preferences preferences;
    private int asteroidBalance;
    private GameHUD gameHUD;

    public Spaceship(Viewport viewport, GameHUD gameHUD) {
        this.viewport = viewport;
        texture = new Texture("spaceship.png");
        sprite = new Sprite(texture);
        sprite.setSize(1.2f, 1.2f); // set size for spaceship in world units
        harvestCount = 1;
        tractorBeam = new ShapeRenderer();
        isHarvesting = false;
        this.gameHUD = gameHUD;

        preferences = Gdx.app.getPreferences("SpacecraftPreferences");
        asteroidBalance = preferences.getInteger("asteroidBalance", 0);

    }

    public void draw(SpriteBatch batch, float worldWidth) {
        float spaceshipX = (worldWidth - sprite.getWidth()) / 2;
        float spaceshipY = 3.5f;
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
            Asteroid harvestedAsteroid = asteroidManager.selectRandomAsteroid();
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
            }
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
}
