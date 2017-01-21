package com.taek_aaa.goalsnowball.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryPhysicalArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryTimeArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;
import static com.taek_aaa.goalsnowball.activity.MainActivity.isSuccessToday;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.getGoldToday;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public class TodayAchievementRateActivity extends AchievementRateActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (goalDataSet.getTypeToday().equals("물리적양")) {
            typeOfContents = "물리적양";
        } else if (goalDataSet.getTypeToday().equals("시간적양")) {
            typeOfContents = "시간적양";
        } else {
            Log.e("lk","오류");
            typeOfContents = "error";

        }
        try {
            drawGoal();
            drawGoalAmount(typeOfContents);
            drawCurrentAmount(typeOfContents);
            drawPercent(typeOfContents);
            drawRemainAmount(typeOfContents);
            drawBettingGold();
            drawBettingResult();
            drawDue();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "오늘의 목표를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    public void drawGoal() {
        achievementStringtv.setText("" + goalDataSet.getTodayGoal());
    }

    public void drawGoalAmount(String type) throws Exception{
        if (type.equals("물리적양")) {
            achievementAmounttv.setText("" + goalDataSet.getAmountToday() + "" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
        } else if (type.equals("시간적양")) {
            achievementAmounttv.setText("" + goalDataSet.getAmountToday() + "분 " + categoryTimeArrays[goalDataSet.getUnitToday()]);
        } else {
            throw new Exception();
        }
    }

    public void drawCurrentAmount(String type) throws Exception{
        if (type.equals("물리적양")) {
            currentAmounttv.setText("" + goalDataSet.getCurrentAmountToday() + "" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
        } else if (type.equals("시간적양")) {
            currentAmounttv.setText("" + goalDataSet.getCurrentAmountToday() + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawPercent(String type) throws Exception {
        double result;
        int goal = goalDataSet.getAmountToday();
        int current;

        if (type.equals("물리적양") || (type.equals("시간적양"))) {
            progressBar.setMax(goalDataSet.getAmountToday());
            progressBar.setProgress(goalDataSet.getCurrentAmountToday());
            progressBar.setVisibility(ProgressBar.VISIBLE);
            current = goalDataSet.getCurrentAmountToday();
        } else {
            throw new Exception();
        }

        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result >= 100.0) {
            result = 100;
            percentAmounttv.setText("" + result + "%");
            percentAmounttv.setTextColor(Color.GREEN);
        }else{
            percentAmounttv.setText("" + result + "%");
        }
    }

    public void drawRemainAmount(String type) throws Exception {
        if (type.equals("물리적양")) {
            remainAmounttv.setText("" + (goalDataSet.getAmountToday() - goalDataSet.getCurrentAmountToday()) + "" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
        } else if (type.equals("시간적양")) {
            remainAmounttv.setText("" + (goalDataSet.getAmountToday() - goalDataSet.getCurrentAmountToday()) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawBettingGold()  {
        if(isSuccessToday==false) {
            betAmounttv.setText("" + goalDataSet.getBettingGoldToday() + " Gold");
        }else{
            betAmounttv.setText("" + getGoldToday + " Gold");
        }
    }

    public void drawBettingResult() {
        if(goalDataSet.getBettingGoldToday()==0){
            //획득
            resultBettv.setText("획득하였습니다.");
        }else{
            //도전중
            resultBettv.setText("도전중입니다.");
        }
        ////// 여기 실패해서 미획득 일때 구현하기



    }


}
