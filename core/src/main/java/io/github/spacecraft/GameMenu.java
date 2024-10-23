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
    private float edgeMargin;

    private TextureRegionDrawable purpleBackground;
    private TextureRegionDrawable blueBackground;

    public GameMenu(Stage stage) {
        this.stage = stage;
        skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));

        edgeMargin = 25; // the gap around sides of screen (so nav does not squeeze against edge)
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        // create button colours
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(66 / 255f, 22 / 255f, 210 / 255f, 1));
        pixmap.fill();
        Texture purpleBackgroundTexture = new Texture(pixmap);
        purpleBackground = new TextureRegionDrawable(new TextureRegion(purpleBackgroundTexture));

        pixmap.setColor(new Color(46 / 255f, 59 / 255f, 240 / 255f, 1));
        pixmap.fill();
        Texture blueBackgroundTexture = new Texture(pixmap);
        blueBackground = new TextureRegionDrawable(new TextureRegion(blueBackgroundTexture));

        createMenuInterface();
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

        // create menu buttons
        ImageButton upgradesButton = createMenuButton(upgradeTexture);
        ImageButton refineryButton = createMenuButton(refineryTexture);
        ImageButton homeButton = createMenuButton(homeTexture);
        ImageButton laboratoryButton = createMenuButton(laboratoryTexture);
        ImageButton shopButton = createMenuButton(shopTexture);

        // calculate button size dynamically to fit all screen sizes
        int numOfButtons = 5;
        float margin = 10; // space between buttons
        float totalMargin = margin * (numOfButtons - 1);
        float availableWidth = width - edgeMargin * 2 - totalMargin;
        float buttonWidth = availableWidth / numOfButtons;
        float buttonHeight = buttonWidth;

        // add buttons to table
        buttonTable.add(upgradesButton).size(buttonWidth, buttonHeight).padRight(margin);
        buttonTable.add(refineryButton).size(buttonWidth, buttonHeight).padRight(margin);
        buttonTable.add(homeButton).size(buttonWidth, buttonHeight).padRight(margin);
        buttonTable.add(laboratoryButton).size(buttonWidth, buttonHeight).padRight(margin);
        buttonTable.add(shopButton).size(buttonWidth, buttonHeight);

        buttonTable.setY(edgeMargin);

        // add table to stage
        stage.addActor(buttonTable);

        // add listeners to buttons
        upgradesButton.addListener(createButtonListener("Upgrades", buttonHeight, createUpgradesContent()));
        refineryButton.addListener(createButtonListener("Refinery", buttonHeight, createRefineryContent()));
        homeButton.addListener(new ClickListener() {
            @Override // home button is done manually because it just closes other menus
            public void clicked(InputEvent event, float x, float y) {
                closePopup();
            }
        });
        laboratoryButton.addListener(createButtonListener("Laboratory", buttonHeight, createLaboratoryContent()));
        shopButton.addListener(createButtonListener("Shop", buttonHeight, createShopContent()));
    }

    private void showPopup(String title, float buttonHeight, Table contentTable) {
        // remove previous popup if exists
        if (popupWindow != null) {
            popupWindow.remove();
        }

        float totalWidth = width - edgeMargin * 2;

        // create new popup window
        popupWindow = new Window(title, skin);
        popupWindow.setY(buttonHeight + 50);
        popupWindow.setSize(totalWidth, height * 0.3f);
        popupWindow.setX(edgeMargin);

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

    private ImageButton createMenuButton(Texture buttonTexture) {
        ImageButton button = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture)));
        button.getStyle().up = purpleBackground; // default background colour
        button.getStyle().down = blueBackground; // on press background colour
        return button;

    }

    private ClickListener createButtonListener(String buttonTitle, float buttonHeight, Table contentTable) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPopup(buttonTitle, buttonHeight, contentTable);
            }
        };
    }

    private Table createUpgradesContent() {
        Table contentTable = new Table();
        contentTable.setFillParent(true);

        // add specific menu content here
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

        // add specific menu content here
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
    private Table createLaboratoryContent() {
        Table contentTable = new Table();
        contentTable.setFillParent(true);

        // add specific menu content here
        TextButton researchButton = new TextButton("Research in Laboratory", skin);

        // add listeners for buttons
        researchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Laboratory", "Researching in Laboratory");
            }
        });

        contentTable.add(researchButton).pad(10).expandX();

        return contentTable;
    }
    private Table createShopContent() {
        Table contentTable = new Table();
        contentTable.setFillParent(true);

        // add specific menu content here
        TextButton shopButton = new TextButton("Buy Something", skin);

        // add listeners for buttons
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Shop", "Buying something in Shop");
            }
        });

        contentTable.add(shopButton).pad(10).expandX();

        return contentTable;
    }
}
