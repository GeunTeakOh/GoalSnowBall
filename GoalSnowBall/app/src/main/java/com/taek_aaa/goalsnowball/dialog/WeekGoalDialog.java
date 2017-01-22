package com.taek_aaa.goalsnowball.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;

import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class WeekGoalDialog extends GoalDialog implements View.OnClickListener {


    public WeekGoalDialog(Context context) {
        super(context);
        title.setText("이번주의 목표를 입력하세요.");
        findViewById(R.id.DialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.DialogExitButton).setOnClickListener(this);
        findViewById(R.id.DialogX).setOnClickListener(this);
        bettinggold = userDBManager.getGold() / 2;
        bettingGoldet.setText("" + bettinggold);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                goalDataSet.setUnitWeek(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DialogConfirmButton:
                Log.e("test", "" + checkedId);
                try {

                    bettinggold = userDBManager.getGold() / 2;
                    if (Integer.parseInt(bettingGoldet.getText().toString()) > bettinggold) {
                        Toast.makeText(getContext(), "배팅액은 총 보유 골드의 절반을 넘을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        int textAmount = Integer.parseInt(editTextAmonut.getText().toString());
                        textContents = editTextContents.getText().toString();
                        if (textContents.equals("")) {
                            throw new Exception();
                        }

                        if (physicalRadio.isChecked()) {
                            goalDataSet.setTypeWeek("물리적양");
                            Log.e("tt", "" + physicalRadio.getId());
                        } else {
                            goalDataSet.setTypeWeek("시간적양");
                            Log.e("tt", "" + timeRadio.getId());
                        }

                        goalDataSet.setAmountWeek(textAmount);
                        goalDataSet.setWeekGoal(textContents);

                        title.setText("이번주의 목표를 입력하세요.");
                        editTextContents.setHint("목표를 입력하세요.");

                        goalDataSet.setBettingGoldWeek(Integer.parseInt(bettingGoldet.getText().toString()));
                    }
                    dismiss();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "값을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.DialogExitButton:
                dismiss();
                break;
            case R.id.DialogX:
                dismiss();
                break;
        }
    }

}
