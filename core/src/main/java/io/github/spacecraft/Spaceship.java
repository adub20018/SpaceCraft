package io.github.spacecraft;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Spaceship {
    private Sprite sprite;
    private Texture texture;

    public Spaceship() {
        texture = new Texture("spaceship.png");
        sprite = new Sprite(texture);
        sprite.setSize(1, 1); // set size for spaceship in world units

    }

    public void draw(SpriteBatch batch, float worldWidth ) {
        batch.draw(sprite, (worldWidth - 1.2f) / 2, 2, 1.2f, 1.2f); // draw spaceship in center of screen

    }

}
