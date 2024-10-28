package io.github.spacecraft;

import static java.lang.Math.pow;

public class Costs {
    public static int clickLevelCost;
    public static int idleLevelCost;
    public static int navigatorLevelCost;
    public static int harvestLevelCost;

    private Costs() {

    }


    static int getClickLevelCost(int currentClickLevel) {
        int cost = (int) pow(2,currentClickLevel/5f);
        clickLevelCost = cost;
        return cost;
    }

    public static int getIdleLevelCost(int currentIdleLevel) {
        int cost = (int) pow(1.5,currentIdleLevel/4f);
        idleLevelCost = cost;
        return cost;
    }

    public static int getNavigatorLevelCost(int currentNavigatorLevel) {
        int cost = (int) pow(3,currentNavigatorLevel/3f);
        navigatorLevelCost = cost;
        return cost;
    }

    public static int getHarvestTimeLevelCost(int currentHarvestTimeLevel) {
        int cost = (int) pow(1.7,currentHarvestTimeLevel/2f);
        harvestLevelCost = cost;
        return cost;
    }
}
