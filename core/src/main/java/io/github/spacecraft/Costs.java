package io.github.spacecraft;

import static java.lang.Math.pow;

public class Costs {
    public static int clickLevelCost;
    private Costs() {

    }


    static int getClickLevelCost(int currentClickLevel) {
        int cost = (int) pow(2,Math.floor(currentClickLevel/5));
        clickLevelCost = cost;
        return cost;
    }
}
