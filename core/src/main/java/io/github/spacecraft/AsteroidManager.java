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
//        asteroidSprites = new Array<>();
        asteroids = new SnapshotArray<>();
    }

    public void updateAsteroids(float worldWidth, float worldHeight) {
        float delta = Gdx.graphics.getDeltaTime();

        for (int i = asteroids.size - 1; i >= 0; i--) {
            Sprite asteroidSprite = asteroids.get(i).getSprite();
            asteroidSprite.translateY(-2f * delta); // asteroid flow speed

            // remove asteroid once it goes past the bottom of screen
            if (asteroidSprite.getY() < -asteroidSprite.getHeight()) {
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
            System.out.println(asteroidSprite.getY() + "!!!!!!!!!!!!!!!!!!!");
        }
        //System.out.println("Asteroids Drawn !!!!!!!!!!!!!");
    }

    public void createAsteroid(float worldWidth, float worldHeight) {
        float asteroidWidth = 0.7f;
        float asteroidHeight = 0.7f;

//        Sprite asteroidSprite = new Sprite(asteroidTexture);

        float x = MathUtils.random(0f, worldWidth - asteroidWidth);
        float y = worldHeight;

        Asteroid newAsteroid = new Asteroid(x, y);

        // create the asteroids
//        asteroidSprite.setSize(asteroidWidth, asteroidHeight);
//        asteroidSprite.setX(MathUtils.random(0f, worldWidth - asteroidWidth));
//        asteroidSprite.setY(worldHeight);

        // add asteroid to array
//        asteroidSprites.add(asteroidSprite);
        asteroids.add(newAsteroid);
    }
}
