package io.github.spacecraft;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Spaceship {
    private Sprite sprite;
    private Texture texture;
    private int harvestCount;

    public Spaceship() {
        texture = new Texture("spaceship.png");
        sprite = new Sprite(texture);
        sprite.setSize(1, 1); // set size for spaceship in world units
        harvestCount = 1;
    }

    public void draw(SpriteBatch batch, float worldWidth ) {
        batch.draw(sprite, (worldWidth - 1.2f) / 2, 3.5f, 1.2f, 1.2f); // draw spaceship in center of screen
    }

    public int getHarvestCount() {
        return harvestCount;
    }

    public void incrementHarvestCount() {
        harvestCount++;
    }

    public void harvestAsteroid(AsteroidManager asteroidManager) {
        if(harvestCount > 0) {
            asteroidManager.pauseAsteroid();
            harvestCount--;
        }
    }
}
