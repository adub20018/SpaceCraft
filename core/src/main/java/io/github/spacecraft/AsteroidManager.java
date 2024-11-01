package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.Comparator;

public class AsteroidManager {
    private Texture asteroidTexture;
    private SnapshotArray<Asteroid> asteroids;
    private float asteroidSpawnTimer = 0;

    public AsteroidManager() {
        asteroidTexture = new Texture("asteroid.png");
        asteroids = new SnapshotArray<>();
    }

    public void updateAsteroids(float worldWidth, float worldHeight, Spaceship spaceship) {
        float delta = Gdx.graphics.getDeltaTime();

        for (int i = asteroids.size - 1; i >= 0; i--) {
            Sprite asteroidSprite = asteroids.get(i).getSprite();
            asteroids.get(i).update(delta); // asteroid flow speed
            if(asteroids.get(i).getHarvestWaitTime() <=0) {
                spaceship.incrementHarvestCount(asteroids.get(i).getRarity());
                asteroids.begin();
                asteroids.removeIndex(i);
                asteroids.end();
                if(spaceship.asteroidCoords.size == 0) {
                    spaceship.isHarvesting = false;
                }
                //continue;
            }
            // remove asteroid once it goes past the bottom of screen
            if (asteroidSprite.getY() < -asteroidSprite.getHeight()) {
                asteroids.begin();
                asteroids.removeIndex(i);
                asteroids.end();
            }
        }

        // manage asteroid re-spawning
        asteroidSpawnTimer += delta;
        if (asteroidSpawnTimer > 1f) {
            asteroidSpawnTimer = 0.1f; // increase this to spawn more asteroids
            createAsteroid(worldWidth, worldHeight);
            if(MathUtils.random(0,99)<spaceship.navigatorLevel) {
                createAsteroid(worldWidth, worldHeight); // create new asteroid
            }
        }
    }

    public void drawAsteroids(SpriteBatch batch) {
        for (Asteroid asteroid : asteroids) {
            Sprite asteroidSprite = asteroid.getSprite();
            asteroidSprite.draw(batch);
        }
    }

    private int randomSpawn() {
        int rarityValue = MathUtils.random(0, 10000); // random value between 0 and 10000
        int asteroidRarity = 1;

        if (rarityValue < 9500) asteroidRarity = 1; // common (95%)
        else if (rarityValue < 9900) asteroidRarity = 2; // uncommon (4%)
        else if (rarityValue < 9990) asteroidRarity = 3; // rare (0.9%)
        else if (rarityValue < 10000) asteroidRarity = 4; // epic (0.1%)
        else asteroidRarity = 5; // legendary (0.001%)

        return asteroidRarity;
    }

    public void createAsteroid(float worldWidth, float worldHeight) {
        int asteroidRarity = randomSpawn();

        float asteroidWidth = 0.7f;
        float asteroidHeight = 0.7f;

        float x = MathUtils.random(0f, worldWidth - asteroidWidth);
        float y = worldHeight;

        Asteroid newAsteroid = null;

        // set asteroid with rarity level and colour
        switch (asteroidRarity) {
            case 1:
                newAsteroid = new Asteroid(x, y, 1, 0.6f);
                break;
            case 2:
                newAsteroid = new Asteroid(x, y, 2, 0.85f);
                newAsteroid.getSprite().setColor(30 / 255f,  255 / 255f, 0 / 255f, 1); // green
                break;
            case 3:
                newAsteroid = new Asteroid(x, y, 3, 1.05f);
                newAsteroid.getSprite().setColor(0 / 255f, 112 / 255f, 221 / 255f, 1); // blue
                break;
            case 4:
                newAsteroid = new Asteroid(x, y, 4, 1.25f);
                newAsteroid.getSprite().setColor(163 / 255f, 53 / 255f, 238 / 255f, 1); // purple
                break;
            case 5:
                newAsteroid = new Asteroid(x, y, 5, 1.5f);
                newAsteroid.getSprite().setColor(253 / 255f, 208 / 255f,  23 / 255f, 1); // gold
                System.out.println("IT HAPPENED!!!!");
                break;
        }
        asteroids.add(newAsteroid);
    }

    public Asteroid selectRandomAsteroid(Spaceship spaceship) {
        System.out.println("Random");

        // set the range that asteroids can be harvested in
        float topBoundary = 10.5f;
        float bottomBoundary = 0.8f;

        // filter asteroids to only those within boundaries
        Array<Asteroid> validAsteroids = new Array<>();
        for (Asteroid asteroid : asteroids) {
            float asteroidY = asteroid.getSprite().getY();
            // if asteroid is within boundaries AND is not currently being harvested
            if ((asteroidY < topBoundary && asteroidY > bottomBoundary)&&!asteroid.isHarvesting()) {
                validAsteroids.add(asteroid);
            }
        }

        if (validAsteroids.size > 0) {
            Asteroid selectedAsteroid = validAsteroids.get(MathUtils.random(0, validAsteroids.size - 1));
            selectedAsteroid.setHarvesting(true);
            selectedAsteroid.setHarvestWaitTime(selectedAsteroid.getHarvestWaitTime()*(1-(0.02f*spaceship.getHarvestTimeLevel())));
            return selectedAsteroid;
        }
        return null; // if no asteroids in view
    }

    public Asteroid selectLargestAsteroid(Spaceship spaceship) {
        System.out.println("Largest");

        // set the range that asteroids can be harvested in
        float topBoundary = 10.5f;
        float bottomBoundary = 0.8f;

        // filter asteroids to only those within boundaries
        Array<Asteroid> validAsteroids = new Array<>();
        for (Asteroid asteroid : asteroids) {
            float asteroidY = asteroid.getSprite().getY();
            // if asteroid is within boundaries AND is not currently being harvested
            if ((asteroidY < topBoundary && asteroidY > bottomBoundary)&&!asteroid.isHarvesting()) {
                validAsteroids.add(asteroid);
            }
        }
        // default sort order is ascending, we want to get the Highest rarity
        validAsteroids.sort(Comparator.comparingInt(Asteroid::getRarity).reversed());

        if (validAsteroids.size > 0) {
            validAsteroids.get(0).setHarvesting(true);
            validAsteroids.get(0).setHarvestWaitTime(validAsteroids.get(0).getHarvestWaitTime()*(1-(0.02f*spaceship.getHarvestTimeLevel())));
            return validAsteroids.get(0);
        }
        return null; // if no asteroids in view
    }
    public SnapshotArray<Asteroid> getAsteroids() {
        return asteroids;
    }
}
