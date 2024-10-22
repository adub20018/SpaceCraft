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


    public float getHarvestWaitTime() {
        return harvestWaitTime;
    }

    public void setHarvestWaitTime(float harvestWaitTime) {
        this.harvestWaitTime = harvestWaitTime;
    }
}
