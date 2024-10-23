package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameHUD {
    private Stage stage;
    private BitmapFont font;
    private Texture asteroidTexture;
    private Label asteroidsBalanceLabel;
    private int asteroidsBalance;
    private Preferences preferences;

    public GameHUD(Stage stage) {
        this.stage = stage;
        this.font = new BitmapFont();
        font.setColor(Color.WHITE);

        // create counter groups
        HorizontalGroup asteroidCounterGroup = createAsteroidCounterGroup(asteroidsBalance);

        // add groups to stage
        stage.addActor(asteroidCounterGroup);
    }

    public void updateAsteroidBalanceLabel(int newBalance) {
        this.asteroidsBalance = newBalance;
        asteroidsBalanceLabel.setText("" + asteroidsBalance);
    }

    public void render(float delta) {
       stage.act(delta);
       stage.draw();
    }

    public HorizontalGroup createAsteroidCounterGroup(int asteroidsBalance) {
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");
        asteroidsBalance = preferences.getInteger("asteroidBalance", 0);

        asteroidTexture = new Texture(Gdx.files.internal("asteroid.png"));
        Image asteroidImage = new Image(asteroidTexture);
        asteroidImage.setScale(0.6f);

        // centers the image to be in line with text (vertically)
        asteroidImage.setOrigin(asteroidImage.getWidth() / 2, asteroidImage.getHeight() / 2);

        // display asteroid balance counter
        asteroidsBalanceLabel = new Label("" + asteroidsBalance, new Label.LabelStyle(font, Color.WHITE));
        asteroidsBalanceLabel.setFontScale(2.5f);

        // create group to display asteroid image next to balance counter
        HorizontalGroup asteroidCounterGroup = new HorizontalGroup();
        asteroidCounterGroup.addActor(asteroidImage);
        asteroidCounterGroup.addActor(asteroidsBalanceLabel);

        // display in top left corner
        asteroidCounterGroup.setPosition(-30, Gdx.graphics.getHeight() - 40);
        asteroidCounterGroup.space(-20);

        return asteroidCounterGroup;
    }
}
