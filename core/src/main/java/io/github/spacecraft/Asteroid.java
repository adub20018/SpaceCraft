package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Asteroid {
    private Texture texture;
    private Sprite sprite;
    private float x, y;
    private Array<Sprite> asteroidSprites;
    private float asteroidSpawnTimer = 0;
    private float speed;
    private boolean harvesting;


    private float harvestWaitTime;

    public Asteroid(float x, float y) {
        texture = new Texture("asteroid.png");
        sprite = new Sprite(texture);
        sprite.setX(x);
        sprite.setY(y);
        sprite.setSize(0.7f, 0.7f);
        asteroidSprites = new Array<>();
        speed = MathUtils.random(-2.5f,-0.9f);
        harvesting = false;
        harvestWaitTime = 500f;
    }

    public void update(float delta) {
        if(!harvesting) {
            sprite.translateY(speed * delta);
        } else {
            harvestWaitTime =- delta;
        }
        System.out.println(harvestWaitTime);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

//    public void drawAsteroids(SpriteBatch batch) {
//        for (Sprite asteroidSprite : asteroidSprites) {
//            asteroidSprite.draw(batch);
//        }
//    }

//    public void createAsteroid(float worldWidth, float worldHeight) {
//        float asteroidWidth = 0.7f;
//        float asteroidHeight = 0.7f;
//
//        Sprite asteroidSprite = new Sprite(texture);
//
//        // create the asteroids
//        asteroidSprite.setSize(asteroidWidth, asteroidHeight);
//        asteroidSprite.setX(MathUtils.random(0f, worldWidth - asteroidWidth));
//        asteroidSprite.setY(worldHeight);
//
//        // add asteroid to array
//        asteroidSprites.add(asteroidSprite);
//    }

//    public void updateAsteroidPosition(float worldWidth, float worldHeight) {
//        float delta = Gdx.graphics.getDeltaTime();
//
//        for (int i = asteroidSprites.size - 1; i >= 0; i--) {
//            Sprite asteroidSprite = asteroidSprites.get(i);
//            asteroidSprite.translateY(-2f * delta); // asteroid flow speed
//
//            // remove asteroid once it goes past the bottom of screen
//            if (asteroidSprite.getY() < -asteroidSprite.getHeight()) {
//                asteroidSprites.removeIndex(i);
//            }
//        }
//
//        // manage asteroid re-spawning
//        asteroidSpawnTimer += delta;
//        if (asteroidSpawnTimer > 1f) {
//            asteroidSpawnTimer = 0.1f; // increase this to spawn more asteroids
//            createAsteroid(worldWidth, worldHeight); // create new asteroid
//        }
//    }


    public float getHarvestWaitTime() {
        return harvestWaitTime;
    }

    public void setHarvestWaitTime(float harvestWaitTime) {
        this.harvestWaitTime = harvestWaitTime;
    }
}
