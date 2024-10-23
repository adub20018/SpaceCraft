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
    private float radius;
    private float deltatest;

    public Asteroid(float x, float y, int rarity, float radius) {
        texture = new Texture("asteroid.png");
        sprite = new Sprite(texture);
        sprite.setX(x);
        sprite.setY(y);
        this.radius = radius;
        sprite.setSize(radius, radius);
        asteroidSprites = new Array<>();
        speed = MathUtils.random(-2.5f,-0.9f);
        if (rarity == 2) speed = MathUtils.random(-1.1f, -0.8f);
        if (rarity == 3) speed = MathUtils.random(-0.9f, -0.7f);
        if (rarity == 4) speed = MathUtils.random(-0.8f, -0.5f);
        if (rarity == 5) speed = MathUtils.random(-0.5f, -0.2f);
        harvesting = false;
        harvestWaitTime = 10f;
    }

    public void update(float delta) {
        if(!harvesting) {
            sprite.translateY(speed * delta);
        } else {
            harvestWaitTime -= delta;
        }

        //System.out.println(harvestWaitTime);
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
    public void setHarvesting(boolean harvesting) {
        this.harvesting = harvesting;
    }

}
