package com.taek_aaa.goalsnowball.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.PictureController;
import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.GoalDataSet;
import com.taek_aaa.goalsnowball.dialog.MonthGoalDialog;
import com.taek_aaa.goalsnowball.dialog.TodayGoalDialog;
import com.taek_aaa.goalsnowball.dialog.WeekGoalDialog;

import java.util.GregorianCalendar;

import static com.taek_aaa.goalsnowball.data.CalendarDatas.dayOfWeekArray;
import static com.taek_aaa.goalsnowball.data.CalendarDatas.endOfMonth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final int PICK_FROM_ALBUM = 101;
    Bitmap photo;
    public static LayoutInflater inflater;
    ImageView imageView;
    public static int viewHeight = 700;        //원하는 뷰의 높이(해상도)
    Boolean isPicture = false;
    TextView todaytv, weektv, monthtv, dDayWeektv, dDayMonthtv, mainGoldtv, percentToday, percentWeek, percentMonth;
    public static GoalDataSet goalDataSet;
    //public static LinkedList<DBData> llDBData = new LinkedList<DBData>();
    TodayGoalDialog todayGoalDialog;
    WeekGoalDialog weekGoalDialog;
    MonthGoalDialog monthGoalDialog;
    public static String[] categoryPhysicalArrays = {"개", "쪽", "권", ""};
    public static String[] categoryTimeArrays = {"이상", "이하"};
    public static float defaultHeight, defaultWidth;
    PictureController pictureController;
    public static boolean isSuccessToday=false, isSuccessWeek=false, isSuccessMonth=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        goalDataSet = new GoalDataSet();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pictureController = new PictureController();

        init();     //나중에 디비로 구현하면 여기서 몇개 제외하기


        drawDDay();
        drawMainImage();
        drawGoal();

        drawTodayPercent();
        drawWeekPercent();
        drawMonthPercent();
        registerForContextMenu(imageView);
    }

    /**
     * 가려젔다가 다시 시작되었을때 값들 업데이트
     **/
    @Override
    protected void onRestart() {
        super.onRestart();
        mainGoldtv.setText("" + goalDataSet.getTotalGold() + "Gold");
        drawTodayPercent();
        drawWeekPercent();
        drawMonthPercent();
    }

    /**
     * 뒤로가기 눌렀을 때
     **/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    /** 네비게이션 바에서 메뉴 선택 시 **/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_todayGoalSetting) {
            startActivity(new Intent(this, TodayGoalDoingActivity.class));
        } else if (id == R.id.nav_weekGoalSetting) {
            startActivity(new Intent(this, WeekGoalDoingActivity.class));

        } else if (id == R.id.nav_monthGoaslSetting) {
            startActivity(new Intent(this, MonthGoalDoingActivity.class));
        } else if (id == R.id.nav_totalGoalSetting) {

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * intent 결과 처리
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                pictureSetToImageView(data);
                break;
        }
    }

    /**
     * 골라온 이미지를 이미지뷰에 입힘
     **/
    protected void pictureSetToImageView(Intent data) {
        try {
            Uri uri = data.getData();
            String uriPath = uri.getPath();
            Log.e("test", uriPath);
            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            inflater = getLayoutInflater();

            Bitmap rotatedPhoto;
            ExifInterface exif = new ExifInterface(uriPath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = pictureController.exifOrientationToDegrees(exifOrientation);

            rotatedPhoto = pictureController.rotate(photo, exifDegree);

            Bitmap sizedPhoto = pictureController.setSizedImage(rotatedPhoto);

            imageView = (ImageView) findViewById(R.id.mainImageView);
            imageView.setImageBitmap(sizedPhoto);
            Toast.makeText(getBaseContext(), "사진을 입력하였습니다.", Toast.LENGTH_SHORT).show();
            isPicture = true;
        } catch (Exception e) {
            e.getStackTrace();
            isPicture = false;
        }

    }

    /**
     * 목표 부분들을 누르면 다이알러그 보여줌
     **/
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainTodayGoalTv:
                todayGoalDialog.show();
                break;
            case R.id.mainWeekGoalTv:
                weekGoalDialog.show();
                break;
            case R.id.mainMonthGoalTv:
                monthGoalDialog.show();
                break;
        }
    }
    /** 달성률 클릭 시 **/
    public void onClickRate(View v) {
        switch (v.getId()){
            case R.id.percentToday :
                startActivity(new Intent(this, TodayAchievementRateActivity.class));
                break;
            case R.id.percentWeek :
                break;
            case R.id.percentMonth :
                break;
        }

    }

    /**
     * 오늘의 목표를 텍스트뷰에 출력
     **/
    public void drawTodayGoal() {
        if (goalDataSet.isTodayGoal == true) {
            todaytv.setText(goalDataSet.getTodayGoal());
            todaytv.setGravity(Gravity.CENTER);
            Log.e("test", goalDataSet.getTodayGoal());
        } else {
            todaytv.setText("");
        }
    }

    /**
     * 이번주 목표를 텍스트뷰에 출력
     **/
    public void drawWeekGoal() {
        if (goalDataSet.isWeekGoal == true) {
            weektv.setText(goalDataSet.getWeekGoal());
            weektv.setGravity(Gravity.CENTER);
            Log.e("test", goalDataSet.getWeekGoal());
        } else {
            weektv.setText("");
        }

    }

    /**
     * 이번달 목표를 텍스트뷰에 출력
     **/
    public void drawMonthGoal() {
        if (goalDataSet.isMonthGoal == true) {
            monthtv.setText(goalDataSet.getMonthGoal());
            monthtv.setGravity(Gravity.CENTER);
            Log.e("test", goalDataSet.getMonthGoal());
        } else {
            monthtv.setText("");
        }

    }

    /**
     * 목표를 다이얼로그에서 설정하고 다이얼로그가 dismiss 되면 목표 출력
     **/
    public void drawGoal() {
        todaytv = (TextView) findViewById(R.id.mainTodayGoalTv);
        weektv = (TextView) findViewById(R.id.mainWeekGoalTv);
        monthtv = (TextView) findViewById(R.id.mainMonthGoalTv);

        todayGoalDialog = new TodayGoalDialog(this);
        todayGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawTodayGoal();
            }
        });
        weekGoalDialog = new WeekGoalDialog(this);
        weekGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawWeekGoal();
            }
        });
        monthGoalDialog = new MonthGoalDialog(this);
        monthGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawMonthGoal();
            }
        });
    }

    /**
     * 이미지 선택 안했을 시에 이미지를 이미지뷰에 출력
     **/
    public void drawMainImage() {
        imageView = (ImageView) findViewById(R.id.mainImageView);
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.profile);
        Bitmap bitmapDefault = drawable.getBitmap();
        Log.e("length", "" + bitmapDefault.getHeight());
        Log.e("length", "" + bitmapDefault.getWidth());
        Bitmap sizedBitmapDefault = pictureController.setSizedImage(bitmapDefault);

        defaultHeight = sizedBitmapDefault.getHeight();
        defaultWidth = sizedBitmapDefault.getWidth();
        Log.e("length", "" + defaultHeight);
        Log.e("length", "" + defaultWidth);
        imageView.setImageBitmap(sizedBitmapDefault);

    }


    /**
     * 오늘쪽 상단의 디데이를 출력
     **/
    public void drawDDay() {
        CalendarDatas calendarData = new CalendarDatas();
        int cYear = calendarData.cYear;
        int hMonth = calendarData.hMonth;
        int cMonth = calendarData.cMonth;
        int cdate = calendarData.cdate;
        int dayOfWeekIndex = calendarData.dayOfWeekIndex;

        dDayWeektv = (TextView) findViewById(R.id.d_week);
        dDayMonthtv = (TextView) findViewById(R.id.d_month);

        Log.e("qq", "" + calendarData.today);
        Log.e("qq", "" + calendarData.cYear);
        Log.e("qq", "" + calendarData.hMonth);
        Log.e("qq", "" + calendarData.cMonth);
        Log.e("qq", "" + calendarData.cdate);
        Log.e("qq", "" + dayOfWeekArray[calendarData.dayOfWeekIndex]);

        switch (calendarData.dayOfWeekIndex) {
            case 1:
                dDayWeektv.setText("이번주   D - 1");
                break;
            case 2:
                dDayWeektv.setText("이번주   D - 7");
                break;
            case 3:
                dDayWeektv.setText("이번주   D - 6");
                break;
            case 4:
                dDayWeektv.setText("이번주   D - 5");
                break;
            case 5:
                dDayWeektv.setText("이번주   D - 4");
                break;
            case 6:
                dDayWeektv.setText("이번주   D - 3");
                break;
            case 7:
                dDayWeektv.setText("이번주   D - 2");
                break;

        }
        switch (cMonth) {
            case 0:
                int tmp0;
                tmp0 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp0);
                break;
            case 1:
                boolean tempBoolean;
                int tmp1;
                tmp1 = endOfMonth[cMonth] - cdate;
                tempBoolean = calendarData.isYoonYear(cYear);
                if (tempBoolean == true) {
                    dDayMonthtv.setText("이번달  D - " + "" + (tmp1 + 1));
                    Log.e("ttt", "" + tempBoolean);
                } else {
                    dDayMonthtv.setText("이번달  D - " + "" + tmp1);
                }
                break;
            case 2:
                int tmp2;
                tmp2 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp2);
                break;
            case 3:
                int tmp3;
                tmp3 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp3);
                break;
            case 4:
                int tmp4;
                tmp4 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp4);
                break;
            case 5:
                int tmp5;
                tmp5 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp5);
                break;
            case 6:
                int tmp6;
                tmp6 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp6);
                break;
            case 7:
                int tmp7;
                tmp7 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp7);
                break;
            case 8:
                int tmp8;
                tmp8 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp8);
                break;
            case 9:
                int tmp9;
                tmp9 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp9);
                break;
            case 10:
                int tmp10;
                tmp10 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp10);
                break;
            case 11:
                int tmp11;
                tmp11 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - " + "" + tmp11);
                break;
        }
    }

    /**
     * 2월 윤년 계산
     **/
    public Boolean isYoonYear(int year) {
        GregorianCalendar gr = new GregorianCalendar();
        return gr.isLeapYear(year);
    }

    /**
     * 오늘 목표 달성률 출력
     **/
    public void drawTodayPercent() {
        double result;
        int goal = goalDataSet.getAmountToday();
        int current;
        if ((goalDataSet.getTypeToday().toString() == "물리적양") ||(goalDataSet.getTypeToday().toString() == "시간적양") ) {
            current = goalDataSet.getCurrentAmountToday();
        } else {
            current = 0;
            goal = 10;
        }

        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result == 100.0) {
            percentToday.setText("" + result + "%");
            percentToday.setTextColor(Color.GREEN);
            /*soundPoolMain = new SoundPool(1, STREAM_MUSIC, 0);
            tuneMain = soundPoolMain.load(this, R.raw.coin, 1);
            soundPoolMain.play(tuneMain, 1, 1, 0, 0, 1);*/

        } else {
            percentToday.setText("" + result + "%");
        }

    }

    /**
     * 이번주 목표 달성률 출력
     **/
    public void drawWeekPercent() {
        double result;
        int goal = goalDataSet.getAmountWeek();
        int current;
        if ((goalDataSet.getTypeWeek().toString() == "물리적양") || (goalDataSet.getTypeWeek().toString() == "시간적양")) {
            current = goalDataSet.getCurrentAmountWeek();
        } else {
            current = 0;
            goal = 10;
        }
        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result == 100.0) {
            percentWeek.setText("" + result + "%");
            percentWeek.setTextColor(Color.GREEN);
            /*soundPoolMain = new SoundPool(1, STREAM_MUSIC, 0);
            tuneMain = soundPoolMain.load(this, R.raw.coin, 1);
            soundPoolMain.play(tuneMain, 1, 1, 0, 0, 1);*/
        } else {
            percentWeek.setText("" + result + "%");
        }
    }

    /**
     * 이번달 목표 달성률 출력
     **/
    public void drawMonthPercent() {
        double result;
        int goal = goalDataSet.getAmountMonth();
        int current;
        if ((goalDataSet.getTypeMonth().toString() == "물리적양") || (goalDataSet.getTypeMonth().toString() == "시간적양")) {
            current = goalDataSet.getCurrentAmountMonth();
        } else {
            current = 0;
            goal = 10;
        }
        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result == 100.0) {
            percentMonth.setText("" + result + "%");
            percentMonth.setTextColor(Color.GREEN);
            /*soundPoolMain = new SoundPool(1, STREAM_MUSIC, 0);
            tuneMain = soundPoolMain.load(this, R.raw.coin, 1);
            soundPoolMain.play(tuneMain, 1, 1, 0, 0, 1);*/

        } else {
            percentMonth.setText("" + result + "%");
        }
    }

    /**
     * init
     **/
    public void init() {
        goalDataSet.setCurrentAmountToday(0);   //나중에 디비로구현하면 삭제하기
        goalDataSet.setTotalGold(10);
        goalDataSet.setTypeToday("");
        goalDataSet.setTypeWeek("");
        goalDataSet.setTypeMonth("");

        mainGoldtv = (TextView) findViewById(R.id.mainGoldtv);
        mainGoldtv.setText("" + goalDataSet.getTotalGold() + "Gold");
        percentToday = (TextView) findViewById(R.id.percentToday);
        percentWeek = (TextView) findViewById(R.id.percentWeek);
        percentMonth = (TextView) findViewById(R.id.percentMonth);


    }

    /**
     * 컨텍스트 메뉴
     **/
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 컨텍스트 메뉴가 최초로 한번만 호출되는 콜백 메서드

        menu.setHeaderTitle("어떤 작업을 수행하시겠습니까?");

        String[] currencyUnit = {"사진 추가/수정", "사진 회전"};

        for (int i = 1; i <= 2; i++) {
            menu.add(0, i, 100, currencyUnit[i - 1]);
        }
    }

    /**
     * 컨텍스트 메뉴에서 아이템 선택
     **/
    public boolean onContextItemSelected(MenuItem item) {
        // 롱클릭했을 때 나오는 context Menu 의 항목을 선택(클릭) 했을 때 호출

        switch (item.getItemId()) {
            case 1:// 사진추가
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_FROM_ALBUM);

                return true;

            case 2:// 사진 회전
                if (isPicture == true) {
                    Bitmap rotatedPicture;
                    rotatedPicture = pictureController.rotate(photo, 90);
                    photo = rotatedPicture;
                    imageView = (ImageView) findViewById(R.id.mainImageView);
                    imageView.setImageBitmap(rotatedPicture);
                    return true;
                } else {
                    Toast.makeText(this, "기본 이미지는 회전을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                }
        }
        return super.onContextItemSelected(item);
    }

}
