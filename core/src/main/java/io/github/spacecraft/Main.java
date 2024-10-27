package io.github.spacecraft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Spaceship spaceship;

    private AsteroidManager asteroidManager;
    private GameHUD gameHUD;
    private GameMenu gameMenu;
    private UpgradesManager upgradesManager;

    private Stage stage;

    private float worldWidth;
    private float worldHeight;
    float deltatest;

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void create() {
        // backgroundTexture = new Texture("texture png") // create a background texture
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(5, 11);

        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();

        // initialise stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        gameHUD = new GameHUD(stage);


        spaceship = new Spaceship(viewport, gameHUD);
        upgradesManager = new UpgradesManager(spaceship);

        gameMenu = new GameMenu(stage, upgradesManager, gameHUD);


        asteroidManager = new AsteroidManager();

        deltatest = 0;
    }

    @Override
    public void render() {
        input();
        logic();
        draw();

        gameMenu.render(Gdx.graphics.getDeltaTime());
        gameHUD.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    private void input() {
        Vector2 touchPos = new Vector2();
        // put on touch logic here
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos);
        if (Gdx.input.justTouched()) {
            System.out.println("Touched");
            System.out.println("IDLE CHARGE: " + spaceship.tractorIdleCharge);
//            System.out.println(spaceship.tractorClickLevel);
//            System.out.println(spaceship.getHarvestCount());

            if (spaceship.spaceRect.contains(touchPos)&&spaceship.getHarvestCount()>0) {
                spaceship.tractorUpdate("click");
            }
        }
    }

    private void logic() {
        //asteroid.updateAsteroidPosition(worldWidth, worldHeight);
        asteroidManager.updateAsteroids(worldWidth, worldHeight, spaceship);
        //System.out.println(spaceship.getHarvestCount());
        if(spaceship.tractorUpdate("tick")){
            System.out.println("TICK HARVEST");
            spaceship.harvestAsteroid(asteroidManager);

        }
    }

    private void draw() {
        ScreenUtils.clear(new Color(0 / 255f, 8 / 255f, 64 / 255f, 1));
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        if (spaceship.isHarvesting) {
            spaceship.drawTractorBeam();
        }

        spriteBatch.begin();

        /* !! do all drawing within spriteBatch.begin() and end() statements !! */

        // draw asteroids
        asteroidManager.drawAsteroids(spriteBatch);

        // draw spaceship
        spaceship.draw(spriteBatch, worldWidth);

        spriteBatch.end();
    }
}
