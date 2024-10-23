package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Pixmap;

public class GameMenu {
    private Stage stage;
    private Skin skin;
    private Table buttonTable;
    private Window popupWindow;
    private float width;
    private float height;

    public GameMenu(Stage stage) {
        this.stage = stage;

        skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));

        createMenuInterface();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

    }

    private void createMenuInterface() {
        // table to hold buttons
        buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.bottom();

        // load textures for buttons
        Texture upgradeTexture = new Texture("menu_icons/upgrade.png");
        Texture refineryTexture = new Texture("menu_icons/refine.png");
        Texture homeTexture = new Texture("menu_icons/home.png");
        Texture laboratoryTexture = new Texture("menu_icons/laboratory.png");
        Texture shopTexture = new Texture("menu_icons/shop.png");

        // create purple background for image buttons
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(66 / 255f, 22 / 255f, 210 / 255f, 1));
        pixmap.fill();
        Texture purpleBackgroundTexture = new Texture(pixmap);
        TextureRegionDrawable purpleBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(purpleBackgroundTexture));

        // create menu buttons and apply background colour
        ImageButton upgradesButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(upgradeTexture)));
        upgradesButton.getStyle().up = purpleBackgroundDrawable;
        ImageButton refineryButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(refineryTexture)));
        refineryButton.getStyle().up = purpleBackgroundDrawable;
        ImageButton homeButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(homeTexture)));
        homeButton.getStyle().up = purpleBackgroundDrawable;
        ImageButton laboratoryButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(laboratoryTexture)));
        laboratoryButton.getStyle().up = purpleBackgroundDrawable;
        ImageButton shopButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(shopTexture)));
        shopButton.getStyle().up = purpleBackgroundDrawable;

        // calculate button size to fit edge to edge of screen
        int numOfButtons = 5;
        float edgeMargin = 25;
        float margin = 10; // space between buttons
        float totalMargin = margin * (numOfButtons - 1);

        float availableWidth = Gdx.graphics.getWidth() - edgeMargin * 2 - totalMargin;

        float buttonWidth = availableWidth / numOfButtons;
        float buttonHeight = buttonWidth;
        // add buttons to table
        buttonTable.add(upgradesButton).size(buttonWidth, buttonHeight).padRight(margin);
        buttonTable.add(refineryButton).size(buttonWidth, buttonHeight).padRight(margin);
        buttonTable.add(homeButton).size(buttonWidth, buttonHeight).padRight(margin);
        buttonTable.add(laboratoryButton).size(buttonWidth, buttonHeight).padRight(margin);
        buttonTable.add(shopButton).size(buttonWidth, buttonHeight);

        buttonTable.setY(25);

        // add table to stage
        stage.addActor(buttonTable);

        // add listeners to buttons
        upgradesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Table upgradesContent = createUpgradesContent();
                showPopup("Upgrades", buttonHeight, upgradesContent);
            }
        });
        refineryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Table refineryContent = createRefineryContent();
                showPopup("Refinery", buttonHeight, refineryContent);
            }
        });
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closePopup();
            }
        });
        laboratoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Table upgradesContent = createUpgradesContent();
                showPopup("Laboratory", buttonHeight, upgradesContent);
            }
        });
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Table upgradesContent = createUpgradesContent();
                showPopup("Shop", buttonHeight, upgradesContent);
            }
        });
    }
    private void showPopup(String title, float buttonHeight, Table contentTable) {
        // remove previous popup if exists
        if (popupWindow != null) {
            popupWindow.remove();
        }

        float totalWidth = width - 25 * 2;

        // create new popup window
        popupWindow = new Window(title, skin);
        popupWindow.setY(buttonHeight + 50);
        popupWindow.setSize(totalWidth, height * 0.3f);
        popupWindow.setX(25);

        // add content table to window
        popupWindow.add(contentTable).expand().fill(); // make content fill the popup

        stage.addActor(popupWindow);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    private void closePopup() {
        if (popupWindow != null) {
            popupWindow.remove();
        }
    }

    private Table createUpgradesContent() {
        Table contentTable = new Table();
        contentTable.setFillParent(true);

        TextButton upgradeHarvestTimeButton = new TextButton("Upgrade Harvest Time", skin);
        upgradeHarvestTimeButton.setSize(100, 100);
        // add listeners for buttons
        upgradeHarvestTimeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Upgrades", "Harvest Time Upgraded");
            }
        });

        contentTable.add(upgradeHarvestTimeButton).size(200, 200);

        return contentTable;
    }

    private Table createRefineryContent() {
        Table contentTable = new Table();
        contentTable.setFillParent(true);

        TextButton refineAsteroidButton = new TextButton("Refine Asteroid", skin);

        // add listeners for buttons
        refineAsteroidButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Refinery", "Refining Asteroid");
            }
        });

        contentTable.add(refineAsteroidButton).pad(10).expandX();

        return contentTable;
    }
}
