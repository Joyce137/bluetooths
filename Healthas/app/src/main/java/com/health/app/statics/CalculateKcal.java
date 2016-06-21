package com.health.app.statics;

/**
 * Created by CaoRuijuan on 6/18/16.
 */
public class CalculateKcal {
    public static double getKcal(int stepnum) {
        //已知体重、距离
        //跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
        double mKcal;
        mKcal = 55 * stepnum * 1.306 / 1000;
        return mKcal;
    }

    public static String getKcal(String stepnum) {
        //已知体重、距离
        //跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
        int stepNum = Integer.valueOf(stepnum);
        double mKcal;
        mKcal = 55 * stepNum * 1.306 / 1000;
        int kal = (int) mKcal;
        return String.valueOf(kal);
    }
}
