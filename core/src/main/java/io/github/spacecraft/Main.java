package io.github.spacecraft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Spaceship spaceship;
    private Asteroid asteroid;

    private float worldWidth;
    private float worldHeight;

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void create() {
        // backgroundTexture = new Texture("texture png") // create a background texture
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(5, 9);

        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();

        spaceship = new Spaceship();
        asteroid = new Asteroid();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    private void input() {
        // put on touch logic here
    }

    private void logic() {
        asteroid.updateAsteroidPosition(worldWidth, worldHeight);
    }

    private void draw() {
        ScreenUtils.clear(Color.PURPLE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        /* !! do all drawing within spriteBatch.begin() and end() statements !! */

        // draw asteroids
        asteroid.drawAsteroids(spriteBatch);

        // draw spaceship
        spaceship.draw(spriteBatch, worldWidth);

        spriteBatch.end();
    }
}
