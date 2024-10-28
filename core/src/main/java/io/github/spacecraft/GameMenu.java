package io.github.spacecraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Align;

import org.w3c.dom.Text;

public class GameMenu {
    private Stage stage;
    private Skin skin;
    private Table buttonTable;
    private Window popupWindow;
    private float width;
    private float height;
    private float edgeMargin;
    GameHUD gameHUD;
    public TextButton upgradeClickLevel, upgradeIdleCharge, upgradeNavigation, upgradeHarvestTimeButton, upgradeTractorQuantity, upgradeScanner, upgradeRefineryQuality, upgradeToAutoRefine;
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

        // make window scrollable
//        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
//        scrollPane.setFillParent(true);
//        scrollPane.setScrollingDisabled(false, false);

        // add content table to window
        popupWindow.add(contentTable).expandX().fill().padTop(outerPadding); // make content fill the popup

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
    public void updateButtonContent(TextButton button, String title, int levelGetter, int levelCost, String res, boolean isAvailable) {
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
    private Table createUpgradesButtonContent(String title, int levelGetter, boolean available, int levelCost, String res) {
        //int levelCost = levelCost; // set the cost of upgrade to display

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
        contentTable.setFillParent(true);

        // add specific menu content here

        // upgrade click level
        upgradeClickLevel = new TextButton("", skin);
        if(upgradesManager.gravititeBalance<Costs.getClickLevelCost(upgradesManager.getClickLevel())) {}
        TextButton.TextButtonStyle unavailableStyle = skin.get("unavailable", TextButton.TextButtonStyle.class);
        upgradeClickLevel.setStyle(unavailableStyle);
        upgradeClickLevel.clearChildren(); // remove empty text so new content be centered
        upgradeClickLevel.add(createUpgradesButtonContent("Click\nLevel", upgradesManager.getClickLevel(), true,Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite")).expand().fill();

        // upgrade idle charge
        upgradeIdleCharge = new TextButton("", skin);
        if (5 < 10) {
            //TextButton.TextButtonStyle unavailableStyle = skin.get("unavailable", TextButton.TextButtonStyle.class);
            upgradeIdleCharge.setStyle(unavailableStyle);
        }
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
                //if (upgradeSuccessful) { // update button content if upgrade was successfully bought
                //updateButtonContent(upgradeClickLevel, "Click\nLevel", upgradesManager.getClickLevel(), Costs.getClickLevelCost(upgradesManager.getClickLevel()),"gravitite", true);
                upgradesManager.updateValues();
                //}
            }
        });

        upgradeIdleCharge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeIdleCharge();
                //if (upgradeSuccessful) { // update button content if upgrade was successfully bought
                //updateButtonContent(upgradeIdleCharge, "Idle\nCharge", upgradesManager.getIdleChargeLevel());
                upgradesManager.updateValues();
                //}
            }
        });

        upgradeNavigation.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeNavigation();
                //if (upgradeSuccessful) { // update button content if upgrade was successfully bought
                //updateButtonContent(upgradeNavigation, "Navigator", upgradesManager.getNavigatorLevel());
                upgradesManager.updateValues();
                //}
            }
        });

        upgradeHarvestTimeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeHarvestTime();
                //if (upgradeSuccessful) { // update button content if upgrade was successfully bought
                //updateButtonContent(upgradeHarvestTimeButton, "Harvest\nTime", upgradesManager.getHarvestTimeLevel());
                upgradesManager.updateValues();
                //}
            }
        });

        contentTable.add(upgradeClickLevel).expandX().fillX().height(buttonHeight).pad(outerPadding, outerPadding, innerPadding, innerPadding);
        contentTable.add(upgradeIdleCharge).expandX().fillX().height(buttonHeight).pad(outerPadding, innerPadding, innerPadding, outerPadding);
        contentTable.row();
        contentTable.add(upgradeNavigation).expandX().fillX().height(buttonHeight).pad(innerPadding, outerPadding, outerPadding, innerPadding);
        contentTable.add(upgradeHarvestTimeButton).expandX().fillX().height(buttonHeight).pad(innerPadding, innerPadding, outerPadding, outerPadding);

        return contentTable;
    }

    // *******************
    // Refinery section
    // *******************

    private Table createRefineryContent() {
        Table contentTable = new Table();
        contentTable.setFillParent(true);
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
        contentTable.setFillParent(true);

        // ******* NEED TO CHANGE BUTTON GETTERS TO GET LAB BUTTON LEVELS - CURRENTLY USING CLICK LEVEL AS DEFAULT WHILE LAB LEVELS ARE IN DEVELOPMENt
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
        upgradeToAutoRefine = new TextButton("", skin);
        upgradeToAutoRefine.clearChildren();
        upgradeToAutoRefine.add(createUpgradesButtonContent("Auto\nRefine", upgradesManager.getAutoRefineLevel(), true)).expand().fill();

        // add listeners for buttons
        upgradeTractorQuantity.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeTractorQuantity();
                //if (upgradeSuccessful) { // update button content if upgrade was successfully bought
                updateButtonContent(upgradeTractorQuantity, "Tractor\nQuantity", upgradesManager.getTractorQuantityLevel());
                //}
            }
        });

        upgradeScanner.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeScanner();
                //if (upgradeSuccessful) { // update button content if upgrade was successfully bought
                updateButtonContent(upgradeScanner, "Harvest\nScanner", upgradesManager.getScannerLevel());
                //}
            }
        });

        upgradeRefineryQuality.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeRefineryQuality();
                //if (upgradeSuccessful) { // update button content if upgrade was successfully bought
                updateButtonContent(upgradeRefineryQuality, "Refinery\nQuality", upgradesManager.getRefineQualityLevel());
                //}
            }
        });

        upgradeToAutoRefine.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesManager.upgradeToAutoRefine();
                //if (upgradeSuccessful) { // update button content if upgrade was successfully bought
                updateButtonContent(upgradeToAutoRefine, "Auto\nRefine", upgradesManager.getAutoRefineLevel());
                //}
            }
        });

        contentTable.add(upgradeTractorQuantity).expandX().fillX().height(buttonHeight).pad(outerPadding, outerPadding, innerPadding, innerPadding);
        contentTable.add(upgradeScanner).expandX().fillX().height(buttonHeight).pad(outerPadding, innerPadding, innerPadding, outerPadding);
        contentTable.row();
        contentTable.add(upgradeRefineryQuality).expandX().fillX().height(buttonHeight).pad(innerPadding, outerPadding, outerPadding, innerPadding);
        contentTable.add(upgradeToAutoRefine).expandX().fillX().height(buttonHeight).pad(innerPadding, innerPadding, outerPadding, outerPadding);

        return contentTable;
    }
    private Table createShopContent() {
        Table contentTable = new Table();
        contentTable.setFillParent(true);

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
                //updateButtonContent(upgradeClickLevel, "Click\nLevel", upgradesManager.getClickLevel());
                updateButtonContent(upgradeIdleCharge, "Idle\nCharge", upgradesManager.getIdleChargeLevel());
                updateButtonContent(upgradeNavigation, "Navigator", upgradesManager.getNavigatorLevel());
                updateButtonContent(upgradeHarvestTimeButton, "Harvest\nTime", upgradesManager.getHarvestTimeLevel());
                updateButtonContent(upgradeTractorQuantity, "Tractor\nQuantity", upgradesManager.getTractorQuantityLevel());
                updateButtonContent(upgradeScanner, "Harvest\nScanner", upgradesManager.getScannerLevel());
                updateButtonContent(upgradeRefineryQuality, "Refinery\nQuality", upgradesManager.getRefineQualityLevel());
                updateButtonContent(upgradeToAutoRefine, "Auto\nRefine", upgradesManager.getAutoRefineLevel());
            }
        });



        contentTable.add(shopButton).pad(10).expandX();

        return contentTable;
    }

}
