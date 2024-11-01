package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class GameMenu {
    private Stage stage;
    private Skin skin;
    private Table buttonTable;
    private Window popupWindow;
    private float width;
    private float height;
    private float edgeMargin;
    GameHUD gameHUD;
    public TextButton upgradeClickLevel, upgradeIdleCharge, upgradeNavigation, upgradeHarvestTimeButton, upgradeTractorQuantity, upgradeScanner, upgradeRefineryQuality, upgradeRefinePower;
    float outerPadding = 15;     // dynamically calculate the size of buttons within table
    float innerPadding = 7.5f; // padding on edges shared with other button (to avoid double padding)
    float buttonHeight = 150;
    float availableWidth;
    float popupWidth;

    private TextureRegionDrawable purpleBackground;
    private TextureRegionDrawable blueBackground;

    private UpgradesManager upgradesManager;

    public GameMenu(Stage stage, UpgradesManager upgradesManager, GameHUD gameHUD) {
        this.stage = stage;
        this.upgradesManager = upgradesManager;
        this.gameHUD = gameHUD;
        skin = new Skin(Gdx.files.internal("newUiSkin/uiskin.json"));

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
        availableWidth = width - edgeMargin * 2 - totalMargin;
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
        upgradesManager.setPoppedUp(true);

        popupWidth = width - edgeMargin * 2;

        // create new popup window
        popupWindow = new Window(title, skin);
        popupWindow.setY(buttonHeight + 50);
        popupWindow.setSize(popupWidth, height * 0.3f);
        popupWindow.setX(edgeMargin);
        popupWindow.setMovable(false);

        // make window scrollable
        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setY(0);
        scrollPane.setScrollingDisabled(true, false); // only vertical scrolling
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setFadeScrollBars(false);

        // add content table to window
        popupWindow.add(scrollPane).expand().fill().padTop(outerPadding); // make content fill the popup

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
        upgradesManager.setPoppedUp(false);
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

    // method to update button content when an upgrade has been purchased WITH LEVEL COST
    public void updateButtonContent(TextButton button, String title, int levelGetter, int[] levelCost, String res, boolean isAvailable) {
        // clear existing and add new button content
        if(!isAvailable) {
            TextButton.TextButtonStyle unavailableStyle = skin.get("unavailable", TextButton.TextButtonStyle.class);
            button.setStyle(unavailableStyle);
        } else {
            TextButton.TextButtonStyle availableStyle = skin.get("default", TextButton.TextButtonStyle.class);
            button.setStyle(availableStyle);
        }
        button.clearChildren();
        button.add(createUpgradesButtonContent(title, levelGetter, isAvailable, levelCost, res)).expand().fill();
    }

    // method to update button content when an upgrade has been purchased
    public void updateButtonContent(TextButton button, String title, int levelGetter) {
        // clear existing and add new button content
        button.clearChildren();
        button.add(createUpgradesButtonContent(title, levelGetter, true)).expand().fill();
    }


    // *******************
    // Ship Upgrades section
    // *******************
                                // WITH LEVEL COST
    private Table createUpgradesButtonContent(String title, int levelGetter, boolean available, int[] levelCost, String res) {
        Table buttonTable = new Table();
        VerticalGroup clickLevelButtonTitle = new VerticalGroup();
        Label titleLabel = new Label(title, skin);
        titleLabel.setFontScale(2f);
        titleLabel.setAlignment(Align.left);

        Table levelCostTable = new Table();
        levelCostTable.setBackground(skin.getDrawable("square-large"));
        Table levelAndCostTable = new Table();
        Label currentLevelLabel = new Label(levelGetter + "/50", skin, "CostLabel");
        currentLevelLabel.setFontScale(2f);
        currentLevelLabel.setAlignment(Align.center);

        // gravitite case
        if (levelCost[0] > 0) {
            Label gravititeCostLabel = new Label(String.valueOf(levelCost[0]), skin);
            gravititeCostLabel.setFontScale(2f);

            // gravitite image
            Drawable gravititeDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gravitite.png"))); // res should be the path to your image file
            Image gravititeImage = new Image(gravititeDrawable);
            gravititeImage.setScale(0.9f);
            gravititeImage.setAlign(Align.center);
            gravititeImage.setScaling(Scaling.fit); // Ensures it fits within bounds without distortion

            if (!available) {
                Label.LabelStyle unavailableLabelStyle = skin.get("unavailable", Label.LabelStyle.class);
                currentLevelLabel.setStyle(unavailableLabelStyle);
                //upgradeCostLabel.setStyle(unavailableLabelStyle);
                levelCostTable.setBackground(skin.getDrawable("unavailable-square-large"));
            }

            levelCostTable.add(gravititeImage).padLeft(-15).padBottom(4);
            levelCostTable.add(gravititeCostLabel).padLeft(-75);
        }

        // tritanium case
        if (levelCost[1] > 0) {
            Label tritaniumCostLabel = new Label(String.valueOf(levelCost[1]), skin);
            tritaniumCostLabel.setFontScale(2f);

            // cubane image
            Drawable tritaniumDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("tritanium.png"))); // res should be the path to your image file
            Image tritaniumImage = new Image(tritaniumDrawable);
            tritaniumImage.setScale(0.9f);
            tritaniumImage.setAlign(Align.center);
            tritaniumImage.setScaling(Scaling.fit); // Ensures it fits within bounds without distortion

            if (!available) {
                Label.LabelStyle unavailableLabelStyle = skin.get("unavailable", Label.LabelStyle.class);
                currentLevelLabel.setStyle(unavailableLabelStyle);
                levelCostTable.setBackground(skin.getDrawable("unavailable-square-large"));
            }

            levelCostTable.add(tritaniumImage).padLeft(-15).padBottom(4);
            levelCostTable.add(tritaniumCostLabel).padLeft(-75);
        }

        // cubane case
        if (levelCost[2] > 0) {
            Label cubaneCostLabel = new Label(String.valueOf(levelCost[2]), skin);
            cubaneCostLabel.setFontScale(2f);

            // cubane image
            Drawable cubaneDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("cubane.png"))); // res should be the path to your image file
            Image cubaneImage = new Image(cubaneDrawable);
            cubaneImage.setScale(0.9f);
            cubaneImage.setAlign(Align.center);
            cubaneImage.setScaling(Scaling.fit); // Ensures it fits within bounds without distortion

            if (!available) {
                Label.LabelStyle unavailableLabelStyle = skin.get("unavailable", Label.LabelStyle.class);
                currentLevelLabel.setStyle(unavailableLabelStyle);
                levelCostTable.setBackground(skin.getDrawable("unavailable-square-large"));
            }

            levelCostTable.add(cubaneImage).padLeft(-15).padBottom(4);
            levelCostTable.add(cubaneCostLabel).padLeft(-75);
        }

        clickLevelButtonTitle.addActor(titleLabel);

        levelAndCostTable.add(currentLevelLabel).padBottom(5).growX();
        levelAndCostTable.row();
        levelAndCostTable.add(levelCostTable);

        buttonTable.add(clickLevelButtonTitle);
        buttonTable.add().growX();
        buttonTable.add(levelAndCostTable);
        buttonTable.pad(10);

        return buttonTable;
    }


    private Table createUpgradesButtonContent(String title, int levelGetter, boolean available) {
        int levelCost = 2;

        Table buttonTable = new Table();
        VerticalGroup clickLevelButtonTitle = new VerticalGroup();
        Label titleLabel = new Label(title, skin);
        titleLabel.setFontScale(2f);
        titleLabel.setAlignment(Align.left);

        Table levelAndCostTable = new Table();
        Label currentLevelLabel = new Label(levelGetter + "/50", skin, "CostLabel");
        currentLevelLabel.setFontScale(2f);
        currentLevelLabel.setAlignment(Align.center);
        Label upgradeCostLabel = new Label(String.valueOf(levelCost), skin, "CostLabel");
        upgradeCostLabel.setFontScale(2f);
        upgradeCostLabel.setAlignment(Align.center);

        if (!available) {
            Label.LabelStyle unavailableLabelStyle = skin.get("unavailable", Label.LabelStyle.class);
            currentLevelLabel.setStyle(unavailableLabelStyle);
            upgradeCostLabel.setStyle(unavailableLabelStyle);
        }

        clickLevelButtonTitle.addActor(titleLabel);
        levelAndCostTable.add(currentLevelLabel).width(100).padBottom(5);
        levelAndCostTable.row();
        levelAndCostTable.add(upgradeCostLabel).width(100);

        buttonTable.add(clickLevelButtonTitle);
        buttonTable.add().growX();
        buttonTable.add(levelAndCostTable);
        buttonTable.pad(10);

        return buttonTable;
    }

    private Table createUpgradesContent() {
        Table contentTable = new Table();

        // add specific menu content here
        // upgrade click level
        upgradeClickLevel = new TextButton("", skin);
        TextButton.TextButtonStyle unavailableStyle = skin.get("unavailable", TextButton.TextButtonStyle.class);
        upgradeClickLevel.setStyle(unavailableStyle);
        upgradeClickLevel.clearChildren(); // remove empty text so new content be centered
        upgradeClickLevel.add(createUpgradesButtonContent("Click\nPower", upgradesManager.getClickLevel(), true, Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite")).expand().fill();

        // upgrade idle charge
        upgradeIdleCharge = new TextButton("", skin);
        upgradeIdleCharge.clearChildren();
        upgradeIdleCharge.add(createUpgradesButtonContent("Idle\nCharge", upgradesManager.getIdleChargeLevel(), false)).expand().fill();

        // upgrade navigation
        upgradeNavigation = new TextButton("", skin);
        upgradeNavigation.clearChildren();
        upgradeNavigation.add(createUpgradesButtonContent("Navigator", upgradesManager.getNavigatorLevel(), true)).expand().fill();

        // upgrade harvest time
        upgradeHarvestTimeButton = new TextButton("", skin);
        upgradeHarvestTimeButton.clearChildren();
        upgradeHarvestTimeButton.add(createUpgradesButtonContent("Harvest\nTime", upgradesManager.getHarvestTimeLevel(), true)).expand().fill();

        // add listeners for buttons
        upgradeClickLevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeClickLevel(); // boolean to show if upgrade was paid for
                upgradesManager.updateValues();
            }
        });

        upgradeIdleCharge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeIdleCharge();
                upgradesManager.updateValues();
            }
        });

        upgradeNavigation.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeNavigation();
                upgradesManager.updateValues();
            }
        });

        upgradeHarvestTimeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeHarvestTime();
                upgradesManager.updateValues();
            }
        });

        contentTable.add(upgradeClickLevel).expandX().fillX().height(buttonHeight).pad(outerPadding, outerPadding, innerPadding, outerPadding);
        contentTable.row();
        contentTable.add(upgradeIdleCharge).expandX().fillX().height(buttonHeight).pad(innerPadding, outerPadding, innerPadding, outerPadding);
        contentTable.row();
        contentTable.add(upgradeNavigation).expandX().fillX().height(buttonHeight).pad(innerPadding, outerPadding, innerPadding, outerPadding);
        contentTable.row();
        contentTable.add(upgradeHarvestTimeButton).expandX().fillX().height(buttonHeight).pad(innerPadding, outerPadding, innerPadding, outerPadding);

        return contentTable;
    }


    // *******************
    // Refinery section
    // *******************
    private Table createRefineryContent() {
        Table contentTable = new Table();
        contentTable.setHeight(height);
        contentTable.padTop(outerPadding);

        // add specific menu content here
        TextButton refineAsteroidButton = new TextButton("", skin);
        Label refineLabel = new Label("Refine", skin);
        refineLabel.setFontScale(2f);

        refineAsteroidButton.clearChildren();
        refineAsteroidButton.add(refineLabel);

        // resource counters
        HorizontalGroup resourceCounters = new HorizontalGroup(); // stores all resource counts

        // individual resource counters
        HorizontalGroup asteroidCounterGroup = gameHUD.createAsteroidCounterGroup();
        HorizontalGroup gravititeCounterGroup = gameHUD.createGravititeCounterGroup();
        HorizontalGroup tritaniumCounterGroup = gameHUD.createTritaniumCounterGroup();
        HorizontalGroup cubaneCounterGroup = gameHUD.createCubaneCounterGroup();

        // add counters to group
        resourceCounters.addActor(asteroidCounterGroup);
        resourceCounters.addActor(gravititeCounterGroup);
        resourceCounters.addActor(tritaniumCounterGroup);
        resourceCounters.addActor(cubaneCounterGroup);

        contentTable.add(refineAsteroidButton).size(availableWidth, buttonHeight);
        contentTable.row();
        contentTable.add(resourceCounters).left();
        contentTable.row();
        contentTable.add().growY().fill(); // empty element to fill space

        // add listeners for buttons
        refineAsteroidButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Refinery", "Refining Asteroid");
                upgradesManager.doRefine(true);
            }
        });
        return contentTable;
    }


    // *******************
    // Laboratory section
    // *******************
    private Table createLaboratoryContent() {
        Table contentTable = new Table();

        // increase tractor quantity
        upgradeTractorQuantity = new TextButton("", skin);
        upgradeTractorQuantity.clearChildren();
        upgradeTractorQuantity.add(createUpgradesButtonContent("Tractor\nQuantity", upgradesManager.getTractorQuantityLevel(), true)).expand().fill();

        // upgrade harvest scanner
        upgradeScanner = new TextButton("", skin);
        upgradeScanner.clearChildren();
        upgradeScanner.add(createUpgradesButtonContent("Harvest\nScanner", upgradesManager.getScannerLevel(), true)).expand().fill();

        // upgrade refinery quality
        upgradeRefineryQuality = new TextButton("", skin);
        upgradeRefineryQuality.clearChildren();
        upgradeRefineryQuality.add(createUpgradesButtonContent("Refinery\nQuality", upgradesManager.getRefineQualityLevel(), true)).expand().fill();

        // upgrade to auto refine
        upgradeRefinePower = new TextButton("", skin);
        upgradeRefinePower.clearChildren();
        upgradeRefinePower.add(createUpgradesButtonContent("Refine\nPower", upgradesManager.getRefinePowerLevel(), true)).expand().fill();

        // add listeners for buttons
        upgradeTractorQuantity.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeTractorQuantity();
                upgradesManager.updateValues();
            }
        });

        upgradeScanner.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeScanner();
                upgradesManager.updateValues();
            }
        });

        upgradeRefineryQuality.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeRefineryQuality();
                upgradesManager.updateValues();
            }
        });

        upgradeRefinePower.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeRefinePower();
                upgradesManager.updateValues();
            }
        });

        contentTable.add(upgradeTractorQuantity).expandX().fillX().height(buttonHeight).pad(outerPadding, outerPadding, innerPadding, outerPadding);
        contentTable.row();
        contentTable.add(upgradeRefineryQuality).expandX().fillX().height(buttonHeight).pad(innerPadding, outerPadding, innerPadding, outerPadding);
        contentTable.row();
        contentTable.add(upgradeRefinePower).expandX().fillX().height(buttonHeight).pad(innerPadding, outerPadding, innerPadding, outerPadding);
        contentTable.row();
        contentTable.add(upgradeScanner).expandX().fillX().height(buttonHeight).pad(innerPadding, outerPadding, innerPadding, outerPadding);

        return contentTable;
    }
    private Table createShopContent() {
        Table contentTable = new Table();

        // add specific menu content here
        TextButton shopButton = new TextButton("", skin);
        Label resetStatsLabel = new Label("Reset Stats", skin);
        resetStatsLabel.setFontScale(2f);
        shopButton.clearChildren();
        shopButton.add(resetStatsLabel);

        // add listeners for buttons
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Shop", "Buying something in Shop");
                upgradesManager.resetStats();
            }
        });
        contentTable.add(shopButton).pad(10).expandX();

        return contentTable;
    }
}
