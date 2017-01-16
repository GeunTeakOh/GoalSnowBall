package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryPhysicalArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryTimeArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 15..
 */

public class TodayGoalDoingActivity extends Activity {

    TextView doingGoalTodaytv;
    EditText amountOfEdit;
    Boolean isAmount;
    Boolean isStartButtonClicked = true;      //start, pause구분
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int secs = 0;
    int mins = 0;
    int hours = 0;
    Handler handler = new Handler();
    TextView blackboardtv, didAmounttv;
    static int tmpAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (goalDataSet.getTypeToday().equals("물리적양")) {
                setContentView(R.layout.activity_today_goal_amount_doing);
                Log.e("aa", "물리적양");
                isAmount = true;
                amountOfEdit = (EditText)findViewById(R.id.doing_current_amount_today);
                //
                amountOfEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        amountOfEdit.setText(""+goalDataSet.getCurrentAmountToday());
                        //amountOfEdit.setText("test");
                    }
                });


                //didAmounttv = (TextView)findViewById(R.id.doing_current_amount_today);

            } else {
                setContentView(R.layout.activity_today_goal_time_doing);
                Log.e("aa", "시간적양");

                TextView stopWatchtv = (TextView) findViewById(R.id.timerTextView);
                stopWatchtv.setText("00:00:00");

                isAmount = false;
            }
            blackboardtv = (TextView)findViewById(R.id.doing_goalAmount_today);



            doingGoalTodaytv = (TextView) findViewById(R.id.doing_goal_today);
            doingGoalTodaytv.setText("오늘의 목표 : " + goalDataSet.getTodayGoal());


            if(isAmount) {
                blackboardtv.setText("목표량 : " + goalDataSet.getAmountToday() + "" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
                TextView unittv = (TextView)findViewById(R.id.doing_unit_today);
                unittv.setText(""+categoryPhysicalArrays[goalDataSet.getUnitToday()]);
            }else{
                blackboardtv.setText("목표량 : " + goalDataSet.getAmountToday() + "분 " + categoryTimeArrays[goalDataSet.getUnitToday()]);
            }


            tmpAmount=goalDataSet.getCurrentAmountToday();
        } catch (Exception e) {
            Toast.makeText(this, "오늘의 목표를 먼저 설정하세요.", Toast.LENGTH_SHORT).show();
            Log.e("error",""+e.getStackTrace());


            e.getStackTrace();
            finish();
        }




    }

    public void saveCurrentAmountToEditText(){
        goalDataSet.setCurrentAmountToday(Integer.parseInt(amountOfEdit.getText().toString()));
        Toast.makeText(getBaseContext(),"수행량이 저장되었습니다.",Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.upButton:
                tmpAmount += 1;
                if(tmpAmount>goalDataSet.getAmountToday()){
                    tmpAmount=goalDataSet.getAmountToday();
                }
                amountOfEdit.setText(""+tmpAmount);
                break;
            case R.id.downButton:
                tmpAmount-=1;
                if(tmpAmount<0){
                    tmpAmount=0;
                }
                amountOfEdit.setText(""+tmpAmount);
                break;
        }
    }

    public void onClickSaveBtnGoal(View v){
        saveCurrentAmountToEditText();

    }


    //timer start버튼
    public void onClickTimerStartbtn(View v) {
        Button startbtn = (Button) findViewById(R.id.timerStartbtn);
        final TextView timerTv = (TextView) findViewById(R.id.timerTextView);
        try {

            if (isStartButtonClicked) {
                startbtn.setText("Pause");
                starttime = SystemClock.uptimeMillis();
                handler.postDelayed(updateTimer, 0);
                isStartButtonClicked = false;
            } else {
                startbtn.setText("Start");
                timerTv.setTextColor(Color.BLUE);
                timeSwapBuff += timeInMilliseconds;
                handler.removeCallbacks(updateTimer);
                isStartButtonClicked = true;
            }
        } catch (Exception e) {
            Toast.makeText(this, "확인 버튼을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    //timer end 버튼
    public void onClickTimerEndbtn(View v) {
        String howlongtime;
        String shour;
        String sminute;
        String sseconds;
        int ihowlongtime;
        String strText;

        TextView stopWatchtv = (TextView) findViewById(R.id.timerTextView);
        howlongtime = stopWatchtv.getText().toString();
        shour = howlongtime.substring(0, 2);
        sminute = howlongtime.substring(3, 5);
        sseconds = howlongtime.substring(6);
        ihowlongtime = Integer.valueOf(shour) * 60 * 60 + Integer.valueOf(sminute) * 60 + Integer.valueOf(sseconds);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/hh:mm");
        Log.e("ppq", "" + df);
        Date clsTime = new Date();
        String resulttime = df.format(clsTime);

        Log.i("test", String.valueOf(howlongtime));
        Button startbtn = (Button) findViewById(R.id.timerStartbtn);
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        secs = 0;
        mins = 0;
        hours = 0;
        handler.removeCallbacks(updateTimer);
        stopWatchtv.setText("00:00:00");
        startbtn.setVisibility(View.VISIBLE);
        startbtn.setText("Start");
        isStartButtonClicked = true;
        Log.i("test", "찍힘");


    }


    /**
     * 타이머를 관리하는 쓰레드
     **/
    public Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView timerTv = (TextView) findViewById(R.id.timerTextView);
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            hours = mins / 60;
            secs = secs % 60;
            timerTv.setText("" + String.format("%02d", hours) + ":" + "" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
            handler.postDelayed(this, 0);
        }
    };

}
