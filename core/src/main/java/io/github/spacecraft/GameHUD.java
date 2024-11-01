package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class GameHUD {
    private Preferences preferences;
    private Stage stage;
    private BitmapFont font;
    private Texture asteroidTexture;
    private Label asteroidsBalanceLabel;
    private int asteroidsBalance;
    private Texture gravititeTexture;
    private Label gravititeBalanceLabel;
    private int gravititeBalance;
    private Texture tritaniumTexture;
    private Label tritaniumBalanceLabel;
    private int tritaniumBalance;
    private Texture cubaneTexture;
    private Label cubaneBalanceLabel;
    private int cubaneBalance;

    // progress bar
    private ProgressBar harvestProgressBar;
    private float tractorCharge;

    // list to track all instances of resources (for updating in both refinery and hud)
    private Array<Label> asteroidLabelList;
    private Array<Label> gravititeLabelList;
    private Array<Label> tritaniumLabelList;
    private Array<Label> cubaneLabelList;

    public GameHUD(Stage stage) {
        this.stage = stage;
        this.font = new BitmapFont();
        font.setColor(Color.WHITE);

        asteroidLabelList = new Array<>();
        gravititeLabelList = new Array<>();
        tritaniumLabelList = new Array<>();
        cubaneLabelList = new Array<>();

        // create counter groups
        HorizontalGroup asteroidCounterGroup = createAsteroidCounterGroup();
        HorizontalGroup gravititeCounterGroup = createGravititeCounterGroup();
        HorizontalGroup tritaniumCounterGroup = createTritaniumCounterGroup();
        HorizontalGroup cubaneCounterGroup = createCubaneCounterGroup();

        // initialise the progress bar
        initProgressBar();

        // add groups to stage
        stage.addActor(asteroidCounterGroup);
        stage.addActor(gravititeCounterGroup);
        stage.addActor(tritaniumCounterGroup);
        stage.addActor(cubaneCounterGroup);
        stage.addActor(harvestProgressBar);
    }

    private void initProgressBar() {
        // calculate menu button size, to make progress bar sit above buttons
        int margin = 10;
        int totalMargin = margin * 4;
        int availableWidth = Gdx.graphics.getWidth() - 25 * 2 - totalMargin;
        int buttonHeight = (availableWidth / 5) + 50;

        int progressBarHeight = Gdx.graphics.getHeight() - (buttonHeight + 25);
        int progressBarWidth = 60;

        // create progress bar styles
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();

        // bar background colour
        Color backgroundColour = new Color(255 / 255f, 255 / 255f, 255 / 255f, 0.2f); // slightly transparent white
        Drawable backgroundDrawable = createColoredDrawable(progressBarWidth, progressBarHeight, backgroundColour);
        progressBarStyle.background = backgroundDrawable;

        // progress colour
        Color progressColour = new Color(46 / 255f,59 / 255f, 240 / 255f, 0.6f);
        Drawable knobDrawable = createColoredDrawable(progressBarWidth, progressBarHeight, progressColour);
        progressBarStyle.knob = null;
        progressBarStyle.knobBefore = knobDrawable;

        // initialise progress bar
        harvestProgressBar = new ProgressBar(0, 100, 0.1f, true, progressBarStyle);
        harvestProgressBar.setValue(100);
        harvestProgressBar.setPosition(Gdx.graphics.getWidth() - 85, buttonHeight);
        harvestProgressBar.setSize(progressBarWidth, progressBarHeight);
    }

    public void updateProgressBar(float tractorCharge) {
        this.tractorCharge = tractorCharge;
        harvestProgressBar.setValue(100 - tractorCharge); // reversed so 0 is max value
    }

    // helper method to create the progress bar colours
    private Drawable createColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(texture);
    }

    public void updateAsteroidBalanceLabel(int newBalance) {
        this.asteroidsBalance = newBalance;
        for (Label label : asteroidLabelList) {
            label.setText("" + asteroidsBalance);
        }
    }

    public void updateGravititeBalanceLabel(int newBalance) {
        this.gravititeBalance = newBalance;
        for (Label label : gravititeLabelList) {
            label.setText("" + gravititeBalance);
        }
    }

    public void updateTritaniumBalanceLabel(int newBalance) {
        this.tritaniumBalance = newBalance;
        for (Label label : tritaniumLabelList) {
            label.setText("" + tritaniumBalance);
        }
    }

    public void updateCubaneBalanceLabel(int newBalance) {
        this.cubaneBalance = newBalance;
        for (Label label : cubaneLabelList) {
            label.setText("" + cubaneBalance);
        }
    }

    public void render(float delta) {
       stage.act(delta);
       stage.draw();
    }

    public HorizontalGroup createAsteroidCounterGroup() {
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
        asteroidLabelList.add(asteroidsBalanceLabel);

        // create group to display asteroid image next to balance counter
        HorizontalGroup asteroidCounterGroup = new HorizontalGroup();
        asteroidCounterGroup.addActor(asteroidImage);
        asteroidCounterGroup.addActor(asteroidsBalanceLabel);

        // display in top left corner
        asteroidCounterGroup.setPosition(-30, Gdx.graphics.getHeight() - 40);
        asteroidCounterGroup.space(-20);

        return asteroidCounterGroup;
    }

    public HorizontalGroup createGravititeCounterGroup() {
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");
        gravititeBalance = preferences.getInteger("gravititeBalance", 0);

        gravititeTexture = new Texture(Gdx.files.internal("gravitite.png"));
        Image gravititeImage = new Image(gravititeTexture);
        gravititeImage.setScale(0.6f);

        // centers the image to be in line with text (vertically)
        gravititeImage.setOrigin(gravititeImage.getWidth() / 2, gravititeImage.getHeight() / 2);

        // display gravitite balance counter
        gravititeBalanceLabel = new Label("" + gravititeBalance, new Label.LabelStyle(font, Color.WHITE));
        gravititeBalanceLabel.setFontScale(2.5f);
        gravititeLabelList.add(gravititeBalanceLabel);

        // create group to display gravitite image next to balance counter
        HorizontalGroup gravititeCounterGroup = new HorizontalGroup();
        gravititeCounterGroup.addActor(gravititeImage);
        gravititeCounterGroup.addActor(gravititeBalanceLabel);

        // display in top left corner
        gravititeCounterGroup.setPosition(-30, Gdx.graphics.getHeight() - 120);
        gravititeCounterGroup.space(-20);

        return gravititeCounterGroup;
    }

    public HorizontalGroup createTritaniumCounterGroup() {
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");
        tritaniumBalance = preferences.getInteger("tritaniumBalance", 0);

        tritaniumTexture = new Texture(Gdx.files.internal("tritanium.png"));
        Image tritaniumImage = new Image(tritaniumTexture);
        tritaniumImage.setScale(0.6f);

        // centers the image to be in line with text (vertically)
        tritaniumImage.setOrigin(tritaniumImage.getWidth() / 2, tritaniumImage.getHeight() / 2);

        // display tritanium balance counter
        tritaniumBalanceLabel = new Label("" + tritaniumBalance, new Label.LabelStyle(font, Color.WHITE));
        tritaniumBalanceLabel.setFontScale(2.5f);
        tritaniumLabelList.add(tritaniumBalanceLabel);

        // create group to display tritanium image next to balance counter
        HorizontalGroup tritaniumCounterGroup = new HorizontalGroup();
        tritaniumCounterGroup.addActor(tritaniumImage);
        tritaniumCounterGroup.addActor(tritaniumBalanceLabel);

        // display in top left corner
        tritaniumCounterGroup.setPosition(-30, Gdx.graphics.getHeight() - 200);
        tritaniumCounterGroup.space(-20);

        return tritaniumCounterGroup;
    }

    public HorizontalGroup createCubaneCounterGroup() {
        preferences = Gdx.app.getPreferences("SpacecraftPreferences");
        cubaneBalance = preferences.getInteger("cubaneBalance", 0);

        cubaneTexture = new Texture(Gdx.files.internal("cubane.png"));
        Image cubaneImage = new Image(cubaneTexture);
        cubaneImage.setScale(0.6f);

        // centers the image to be in line with text (vertically)
        cubaneImage.setOrigin(cubaneImage.getWidth() / 2, cubaneImage.getHeight() / 2);

        // display cubane balance counter
        cubaneBalanceLabel = new Label("" + cubaneBalance, new Label.LabelStyle(font, Color.WHITE));
        cubaneBalanceLabel.setFontScale(2.5f);
        cubaneLabelList.add(cubaneBalanceLabel);

        // create group to display cubane image next to balance counter
        HorizontalGroup cubaneCounterGroup = new HorizontalGroup();
        cubaneCounterGroup.addActor(cubaneImage);
        cubaneCounterGroup.addActor(cubaneBalanceLabel);

        // display in top left corner
        cubaneCounterGroup.setPosition(-30, Gdx.graphics.getHeight() - 280);
        cubaneCounterGroup.space(-20);

        return cubaneCounterGroup;
    }
}
