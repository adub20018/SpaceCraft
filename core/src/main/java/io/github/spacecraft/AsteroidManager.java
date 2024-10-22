package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

public class AsteroidManager {
    private Texture asteroidTexture;
    private SnapshotArray<Asteroid> asteroids;
    private float asteroidSpawnTimer = 0;

    public AsteroidManager() {
        asteroidTexture = new Texture("asteroid.png");
        asteroids = new SnapshotArray<>();
    }

    public void updateAsteroids(float worldWidth, float worldHeight) {
        float delta = Gdx.graphics.getDeltaTime();

        for (int i = asteroids.size - 1; i >= 0; i--) {
            Sprite asteroidSprite = asteroids.get(i).getSprite();
            asteroids.get(i).update(delta); // asteroid flow speed

            // remove asteroid once it goes past the bottom of screen
            if (asteroidSprite.getY() < -asteroidSprite.getHeight() || asteroids.get(i).getHarvestWaitTime() <=0) {
                asteroids.removeIndex(i);
            }
        }

        // manage asteroid re-spawning
        asteroidSpawnTimer += delta;
        if (asteroidSpawnTimer > 1f) {
            asteroidSpawnTimer = 0.1f; // increase this to spawn more asteroids
            createAsteroid(worldWidth, worldHeight); // create new asteroid
        }
    }

    public void drawAsteroids(SpriteBatch batch) {
        for (Asteroid asteroid : asteroids) {
            Sprite asteroidSprite = asteroid.getSprite();
            asteroidSprite.draw(batch);
        }
        if(asteroids.size > 0) {
            System.out.println(asteroids.get(0).getSprite().getX());
        }
    }

    public void createAsteroid(float worldWidth, float worldHeight) {
        float asteroidWidth = 0.7f;
        float asteroidHeight = 0.7f;

        float x = MathUtils.random(0f, worldWidth - asteroidWidth);
        float y = worldHeight;

        Asteroid newAsteroid = new Asteroid(x, y);

        asteroids.add(newAsteroid);
    }
}
