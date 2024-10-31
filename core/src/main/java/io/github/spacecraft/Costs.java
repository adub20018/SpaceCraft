package io.github.spacecraft;

import static java.lang.Math.pow;

public class Costs {
    //public static int clickLevelCost;
    //public static int idleLevelCost;
    //public static int navigatorLevelCost;
    //public static int harvestLevelCost;

    private Costs() {

    }


    public static int[] getClickLevelCost(int currentClickLevel) {
        int cost = (int) pow(2,currentClickLevel/5f);
        //clickLevelCost = cost;
        return new int[]{cost, 0, 0};
    }

    public static int[] getIdleLevelCost(int currentIdleLevel) {
        int cost = (int) pow(1.5,currentIdleLevel/4f);
        //idleLevelCost = cost;
        return new int[]{0, cost, 0};
    }

    public static int[] getNavigatorLevelCost(int currentNavigatorLevel) {
        int cost = (int) pow(3,currentNavigatorLevel/3f);
        //navigatorLevelCost = cost;
        return new int[]{0, 0, cost};
    }

    public static int[] getHarvestTimeLevelCost(int currentHarvestTimeLevel) {
        int cost = (int) pow(1.7,currentHarvestTimeLevel/2f);
        //harvestLevelCost = cost;
        return new int[]{0, cost, 0};
    }

    public static int[] getTractorQuantityCost(int currentTractorQuantityLevel){
        int cost1, cost2;
        cost1 = (int) pow(2,currentTractorQuantityLevel);
        cost2 = (int) pow(2,currentTractorQuantityLevel);
        return new int[]{cost1, 0, cost2};
    }
    public static int[] getHarvestScannerCost(int currentHarvestScannerLevel){
        if(currentHarvestScannerLevel>0) {
            return new int[]{-1,-1,-1};
        } else {
            return new int[]{200,200,200};
        }
    }
    public static int[] getRefineryQualityCost(int currentRefineryQualityLevel){
        int cost1, cost2;
        cost1 = (int) pow(3,currentRefineryQualityLevel*2)/90;
        cost2 = (int) pow(2,currentRefineryQualityLevel*3)/180;
        return new int[]{cost1,0,cost2};
    }
    public static int[] getRefinePowerCost(int currentRefinePowerLevel){
        int cost1, cost2;
        cost1 = (int) pow(2,currentRefinePowerLevel*3)/180;
        cost2 = (int) pow(4,currentRefinePowerLevel*2)/90;
        return new int[]{cost1,0,cost2};
    }
}
