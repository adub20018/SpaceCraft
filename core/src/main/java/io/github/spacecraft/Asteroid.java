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

    public Asteroid(float x, float y) {
        texture = new Texture("asteroid.png");
        sprite = new Sprite(texture);
        asteroidSprites = new Array<>();
        sprite.setX(x);
        sprite.setY(y);
        sprite.setSize(0.7f, 0.7f);
    }

    public void update(float delta) {
        sprite.translateY(-2f * delta);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }


}
