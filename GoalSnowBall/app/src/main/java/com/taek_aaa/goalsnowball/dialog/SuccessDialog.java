package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.taek_aaa.goalsnowball.R;

import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 17..
 */

public class SuccessDialog extends Dialog {
    public static int whereSuccess;
    final public static int SUCCESS_FROM_TODAY = 60001;
    final public static int SUCCESS_FROM_WEEK = 60002;
    final public static int SUCCESS_FROM_MONTH = 60003;

    ImageView fire, coin;
    TextView msg;
    public SuccessDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_success);

        fire = (ImageView)findViewById(R.id.fireWork);
        coin = (ImageView)findViewById(R.id.coin);
        msg = (TextView)findViewById(R.id.SuccessCoinMsg);



        GlideDrawableImageViewTarget imageViewTarget1 = new GlideDrawableImageViewTarget(fire);
        Glide.with(getContext()).load(R.raw.firework2).into(imageViewTarget1);

        GlideDrawableImageViewTarget imageViewTarget2 = new GlideDrawableImageViewTarget(coin);
        Glide.with(getContext()).load(R.raw.coinmotion).into(imageViewTarget2);
        //오늘목표달성했을때


        if(whereSuccess==SUCCESS_FROM_TODAY) {
            msg.setText("" + goalDataSet.getBettingGoldToday() + "Gold를 획득하였습니다.");
            goalDataSet.setBettingGoldToday(0);
            whereSuccess=0;
        }else if(whereSuccess==SUCCESS_FROM_WEEK){
            msg.setText("" + goalDataSet.getBettingGoldWeek() + "Gold를 획득하였습니다.");
            goalDataSet.setBettingGoldWeek(0);
            whereSuccess=0;
        }else if(whereSuccess==SUCCESS_FROM_MONTH){
            msg.setText("" + goalDataSet.getBettingGoldMonth() + "Gold를 획득하였습니다.");
            goalDataSet.setBettingGoldMonth(0);
            whereSuccess=0;
        }else{
            Log.e("erro","successDialog에서 에러");
        }



    }

}