package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.test.ui.ProgressButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //private TextView tvName, tvSchool, tvType, tvDate, tvReason, tvDestination, tvExplain, tvReviewer1, tvReviewer2;
    private EditText etName, etSchool, etType,  etReason, etDestination, etExplain, etReviewer1, etReviewer2;//etDate,etAppicationDate;
    private String strName, strSchool, strType, strStartDate, strEndDate, strReason, strDestination, strExplain, strReviewer1, strReviewer2;
    private String strStartTime, strEndTime;
    private String strName2, strReviewer12, strReviewer22;

    private String leaveName = "请假.html";
    private String leaveDirName = "请假_files";
    private String delayName = "延期.html";
    private String delayDirName = "延期_files";
    private String cancelName = "销假.html";
    private String cancelDirName = "销假_files";
    private String imgDirName = "images";
    private String strDuration = "12小时";
    private String strStart = "2020-06-26 07时";
    private String strEnd = "2020-06-26 19时";
    private String strApplicationTime = "06-25 12:14";
    private String strApplicationDate;
    private String strRev1Pass = "四五六审核（已通过）";
    private String strRev1Time = "06-25 13:25";
    private String strRev2Pass = "七八九审核（已通过）";
    private String strRev2Time = "06-25 14:28";
    private String strApplicationCost = "审批共耗时2小时14分钟";

    private String strApplicationHour = "12:14";
    private String strRev1Hour = "13:25";
    private String strRev2Hour = "14:28";

    private Context context;
    //private String newLeavePath, newDelayPath, newCancelPath;
    private String newPath, newLeaveDirPath, newDelayDirPath, newCancelDirPath, newImgDirPath;

    //用SharedPreferences存储用户输入的数据
    private SharedPreferences sp;

    private ProgressButton pb_button; //动画按钮

    private Button btStartDate, btStartTime, btEndDate, btEndTime, btApplicationDate;
    private Button btAuthor, btVersionName;

    private String originButtonColor = "#26bcd5", originLayoutColor = "#e0ffff";//"#e6fafa";//"#f0ffff";
    private String vipButtonColor = "#cc6699", vipLayoutColor = "#fcedf7";//"#fcf9fb";//"#fcedf7";//"#ffe0f5";//"#ffcccc";
    private String isOriginColor = "true";

    private String buttonCode = "";

    private Boolean isCreate = false;

    private String caidanCode = "12345678";
    private int caidanCodeLen = caidanCode.length();

    // 右下角彩蛋弹出框内容
    private String[] randStrs = new String[]{
//            "打破假闭环！",
            "基础扎实、工作踏实、作风朴实、开拓创新。",
            "公为天下、报效祖国，\n诚实守信、襟怀坦荡，\n勇猛精进、敢为人先，\n毅然果决、坚韧不拔。",
            "西岳轩昂,北斗辉煌,泽被万方,化育先翔。巍哉学府,辈出栋梁,重德厚生,国乃盛强。千仞之墙,百炼之钢,镂木铄金,飞天巡洋。公诚勇毅,永矢毋忘,中华灿烂,工大无疆。"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isCreate = true;

        context = getApplicationContext();

        //网页以及相关资源文件存储在sd卡中的新位置
        newPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Log.d(TAG, "onCreate: newPath=" + newPath);

        //leave文件夹在sd卡内的新位置
        newLeaveDirPath = newPath + File.separator + leaveDirName;
        Log.d(TAG, "onCreate: newLeaveDirPath=" + newLeaveDirPath);
        //delay文件夹在sd卡内的新位置
        newDelayDirPath = newPath + File.separator + delayDirName;
        Log.d(TAG, "onCreate: newDelayDirPath=" + newDelayDirPath);
        //cancel文件夹在sd卡内的新位置
        newCancelDirPath = newPath + File.separator + cancelDirName;
        Log.d(TAG, "onCreate: newCancelDirPath=" + newCancelDirPath);
        //imges文件夹在sd卡内的新位置
        newImgDirPath = newPath + File.separator + imgDirName;
        Log.d(TAG, "onCreate: newImgDirPath=" + newImgDirPath);

        //检查APP权限
        //checkPermission();

        //调用控件
        etName = (EditText) findViewById(R.id.etName);
        etSchool = (EditText) findViewById(R.id.etSchool);
        etType = (EditText) findViewById(R.id.etType);
//        etDate = (EditText) findViewById(R.id.etDate);
        etReason = (EditText) findViewById(R.id.etReason);
        etDestination = (EditText) findViewById(R.id.etDestination);
        etExplain = (EditText) findViewById(R.id.etExplain);
        etReviewer1 = (EditText) findViewById(R.id.etReviewer1);
        etReviewer2 = (EditText) findViewById(R.id.etReviewer2);
//        etAppicationDate = (EditText) findViewById(R.id.etApplicationDate);

        btStartDate = (Button)findViewById(R.id.btStartDate);
        btStartDate.setOnClickListener(new ButtonListener());

        btStartTime = (Button)findViewById(R.id.btStartTime);
        btStartTime.setOnClickListener(new ButtonListener());

        btEndDate = (Button)findViewById(R.id.btEndDate);
        btEndDate.setOnClickListener(new ButtonListener());

        btEndTime = (Button)findViewById(R.id.btEndTime);
        btEndTime.setOnClickListener(new ButtonListener());

        btApplicationDate = (Button)findViewById(R.id.btApplicationDate);
        btApplicationDate.setOnClickListener(new ButtonListener());


        pb_button = (ProgressButton) findViewById(R.id.pb_btn);
        pb_button.setBgColor(Color.parseColor("#26bcd5"));//(Color.rgb(38, 188, 213));//
        pb_button.setTextColor(Color.WHITE);
        pb_button.setProColor(Color.WHITE);
        pb_button.setButtonText("生成");
        pb_button.setOnClickListener(new ButtonListener());

        btAuthor = (Button)findViewById(R.id.btAuthor);
        btAuthor.setOnClickListener(new ButtonListener());

        btVersionName = (Button) findViewById(R.id.btVersionName);
        btVersionName.setOnClickListener(new ButtonListener());
        btVersionName.setText("V"+getVersionName(context));

        //初始化控件的值
        initData();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (!isCreate){
            if (isOriginColor.equals("true")) {
                changeLayoutColor(originLayoutColor);
                changeButtonColor(originButtonColor);
            }else{
                changeLayoutColor(vipLayoutColor);
                changeButtonColor(vipButtonColor);
            }
        }
        isCreate = false;
    }

    /**
     * 所有按钮的监听
     */
    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            String[] dates, times;
            switch (v.getId()) {
                case R.id.btStartDate:
                    buttonCode = buttonCode + "1";
                    Log.d(TAG, "buttonCode: " + buttonCode);
                    dates = btStartDate.getText().toString().trim().split("-");
                    new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month + 1;
                            btStartDate.setText(String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
                            btEndDate.setText(String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
                        }
                    }, Integer.parseInt(dates[0]), Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[2])).show();
                    break;
                case R.id.btStartTime:
                    buttonCode = buttonCode + "2";
                    Log.d(TAG, "buttonCode: " + buttonCode);
                    times = btStartTime.getText().toString().trim().split(":");
                    new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hour, int minute) {
                            btStartTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                        }
                    }, Integer.parseInt(times[0]), Integer.parseInt(times[1]), true).show();
                    break;
                case R.id.btEndDate:
                    buttonCode = buttonCode + "3";
                    Log.d(TAG, "buttonCode: " + buttonCode);
                    dates = btEndDate.getText().toString().trim().split("-");//|\s+
                    new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month + 1;
                            btEndDate.setText(String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
                        }
                    }, Integer.parseInt(dates[0]), Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[2])).show();
                    break;
                case R.id.btEndTime:
                    buttonCode = buttonCode + "4";
                    Log.d(TAG, "buttonCode: " + buttonCode);
                    times = btEndTime.getText().toString().trim().split(":");
                    new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hour, int minute) {
                            btEndTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                        }
                    }, Integer.parseInt(times[0]), Integer.parseInt(times[1]), true).show();
                    break;
                case R.id.btApplicationDate:
                    buttonCode = buttonCode + "5";
                    Log.d(TAG, "buttonCode: " + buttonCode);
                    dates = btApplicationDate.getText().toString().trim().split("-");
                    new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month + 1;
                            btApplicationDate.setText(String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
                        }
                    }, Integer.parseInt(dates[0]), Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[2])).show();
                    break;
                case R.id.pb_btn:

                    //读取并处理控件数据
                    getData();

                    Message m = mHandler.obtainMessage();
                    mHandler.sendMessageDelayed(m, 1000);

                    buttonCode = buttonCode + "8";
                    Log.d(TAG, "buttonCode: " + buttonCode);
                    if (buttonCode.length() >= caidanCodeLen) {
                        Log.d(TAG, "7_caidan: " + buttonCode.substring(buttonCode.length() - caidanCodeLen, buttonCode.length()));
                        Log.d(TAG, "caidan: isOriginColor: " + isOriginColor);
                    }
                    if ((buttonCode.length() >= caidanCodeLen && buttonCode.substring(buttonCode.length() - caidanCodeLen, buttonCode.length()).equals(caidanCode)) || strName.equals("宋金鹏")) {
                        Log.d(TAG, "caidan: " + "触发彩蛋");
                        if (isOriginColor.equals("true")) {
                            Log.d(TAG, "caidan: " + "激活紫色主题");
                            Toast.makeText(MainActivity.this, " 触发【紫色主题】彩蛋！", Toast.LENGTH_SHORT).show();
                            changeStatusBarColor(vipButtonColor);
                            changeButtonColor(vipButtonColor);
                            changeLayoutColor(vipLayoutColor);
                            isOriginColor = "false";
                        } else {
                            Log.d(TAG, "caidan: " + "返回蓝色主题");
                            Toast.makeText(MainActivity.this, " 触发【蓝色主题】彩蛋！", Toast.LENGTH_SHORT).show();
                            changeStatusBarColor(originButtonColor);
                            changeButtonColor(originButtonColor);
                            changeLayoutColor(originLayoutColor);
                            isOriginColor = "true";
                        }
                    }

                    pb_button.startAnim();
                    break;
                case R.id.btVersionName:
                    buttonCode = buttonCode + "6";
                    Log.d(TAG, "buttonCode: " + buttonCode);
                    githubLink();
                    break;
                case R.id.btAuthor:
                    buttonCode = buttonCode + "7";
                    //避免过长溢出
                    if (buttonCode.length() >= 100) {
                        buttonCode = buttonCode.substring(buttonCode.length() - caidanCodeLen, buttonCode.length());
                    }
                    Log.d(TAG, "buttonCode: " + buttonCode);
                    caidan();
                    break;
            }
        }

    }

    /**
     * 右下角彩蛋
     */
    public void caidan(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("");
        int rand = (int)(+Math.random()*(randStrs.length-1+1));
        builder.setMessage(randStrs[rand]);
        builder.setNegativeButton("否",null);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(MainActivity.this,"GG",0).show();
            }
        });
        builder.show();
    }

    /**
     * 右下角彩蛋
     */
    public void githubLink(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("");
        builder.setMessage(
                "代码开源：\n" +
                "https://github.com/Jepson-Song/leave\n\n" +
                "免责声明：\n" +
                "该软件为作者从网络上随意复制粘贴和胡乱敲击形成，作者并不知道其用途，也不知道是否有人使用 :)\n对于任何人通过任何方式获得该软件作者一概不知，对于任何用途产生的任何后果亦与作者无关。"
                //"该APP仅用于作者学习安卓开发，不涉及任何商业行为，不针对任何个人或个体。作者不清楚该APP是否有其他用途，对于其他用途产生的任何后果亦与作者无关。"
        );
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("前往GitHub主页", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(MainActivity.this,"GG",0).show();
//            转入新activity，在新页面中打开网页
            Intent intent = new Intent(MainActivity.this, GithubActivity.class);
            startActivity(intent);
            }
        });
        builder.show();
    }

    /**
     * 修改状态栏颜色
     * @param color 颜色值 "#ffffff"格式
     */
    private void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));   //这里动态修改颜色
        }

    }

    /**
     * 修改按钮背景颜色
     * @param color 颜色值 "#ffffff"格式
     */
    private void changeButtonColor(String color){

        btStartDate .setBackgroundColor(Color.parseColor(color));

        btStartTime.setBackgroundColor(Color.parseColor(color));

        btEndDate.setBackgroundColor(Color.parseColor(color));

        btEndTime.setBackgroundColor(Color.parseColor(color));

        btApplicationDate.setBackgroundColor(Color.parseColor(color));

//        btAuthor.setBackgroundColor(Color.parseColor(color));

        pb_button.setBgColor(Color.parseColor(color));//(Color.rgb(38, 188, 213));//
    }

    /**
     * 修改页面布局背景颜色
     * @param color 颜色值 "#ffffff"格式
     */
    private void changeLayoutColor(String color){
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.activity_main);
        layout.setBackgroundColor(Color.parseColor(color));
    }

    /**
     * 初始化页面控件的数据，如果用户之前输入过一次，再打开APP时显示的就是上次输入的内容（每次onCreate时触发）
     */
    private void initData(){
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        //private String strName, strType, strStartDate, strReason, strDestination, strExplain, strReviewer1, strReviewer2;
        //strName2 = sp.getString("strName2", "null");
        strName = sp.getString("strName", "某某某");
        strSchool = sp.getString("strSchool", "某某学院");
        strType = sp.getString("strType", "私事");
        //strDuration = sp.getString("strDuration", "null");
        strStartDate = sp.getString("strStartDate", "2020-06-26");
        strStartTime = sp.getString("strStartTime", "07:00");
        strEndDate = sp.getString("strEndDate", "2020-06-26");
        strEndTime = sp.getString("strEndTime", "19:00");
        //strStart = sp.getString("strStart", "null");
        //strEnd = sp.getString("strEnd", "null");
        strReason = sp.getString("strReason", "病假");
        strDestination = sp.getString("strDestination", "校医院");
        strExplain = sp.getString("strExplain", "生病");
        //strApplicationTime = sp.getString("strApplicationTime", "null");
        //strReviewer12 = sp.getString("strReviewer12", "null");
        strReviewer1 = sp.getString("strReviewer1", "某某");
        //strRev1Time = sp.getString("strRev1Time", "null");
        //strReviewer22 = sp.getString("strName", strReviewer22);
        strReviewer2 = sp.getString("strReviewer2", "某某某");
        //strRev2Time = sp.getString("strRev2Time", strReviewer2);
        //strApplicationCost = sp.getString("strApplicationCost", "null");
        strApplicationDate = sp.getString("strApplicationDate", "2020-06-25");
        isOriginColor = sp.getString("isOriginColor", "true");

        // 解决和旧版本冲突导致的闪退问题，因为新版本strApplicationDate格式发生了变化
        if (strApplicationDate.length()==4){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("strApplicationDate", "2020-06-25");
            editor.commit();
            strApplicationDate = "2020-06-25";
        }

        etName.setText(strName);
        etSchool.setText(strSchool);
        etType.setText(strType);
        btStartDate.setText(strStartDate);
        btStartTime.setText(strStartTime);
        btEndDate.setText(strEndDate);
        btEndTime.setText(strEndTime);
        etReason.setText(strReason);
        etDestination.setText(strDestination);
        etExplain.setText(strExplain);
        etReviewer1.setText(strReviewer1);
        etReviewer2.setText(strReviewer2);
        btApplicationDate.setText(strApplicationDate);
        
        if (isOriginColor.equals("true")){
            changeButtonColor(originButtonColor);
            changeStatusBarColor(originButtonColor);
            changeLayoutColor(originLayoutColor);
        }else{
            changeButtonColor(vipButtonColor);
            changeStatusBarColor(vipButtonColor);
            changeLayoutColor(vipLayoutColor);
        }
    }

    /**
     * 将用户的输入的数据保存下来，以便用户下次打开APP时将数据再显示出来（每次click button结束时触发）
     */
    private void saveData(){
        /**
         * 获取SharedPreferenced对象
         * 第一个参数是生成xml的文件名
         * 第二个参数是存储的格式
         */
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        //获取到edit对象
        SharedPreferences.Editor editor = sp.edit();
        //通过editor对象写入数据
        //editor.putString("strName2", strName2);
        editor.putString("strName", strName);
        editor.putString("strSchool", strSchool);
        editor.putString("strType", strType);
        //editor.putString("strDuration", strDuration);
        editor.putString("strStartDate", strStartDate);
        editor.putString("strStartTime", strStartTime);
        editor.putString("strEndDate", strEndDate);
        editor.putString("strEndTime", strEndTime);
        //editor.putString("strStart", strStart);
        //editor.putString("strEnd", strEnd);
        editor.putString("strReason", strReason);
        editor.putString("strDestination", strDestination);
        //editor.putString("strApplicationTime", strApplicationTime);
        //editor.putString("strReviewer12", strReviewer12);
        editor.putString("strReviewer1", strReviewer1);
        //editor.putString("strRev1Time", strRev1Time);
        //editor.putString("strReviewer22", strReviewer22);
        editor.putString("strReviewer2", strReviewer2);
        //editor.putString("strRev2Time", strRev2Time);
        //editor.putString("strApplicationCost", strApplicationCost);
        editor.putString("strApplicationDate", strApplicationDate);
        editor.putString("isOriginColor", isOriginColor);


        //提交数据存入到xml文件中
        editor.commit();
    }

    /**
     * 从页面控件读取数据（每次click button一开始时触发）
     */
    private void getData() {
        strName = etName.getText().toString().trim();
        Log.d(TAG, "read: strName=" + strName);
        strSchool = etSchool.getText().toString().trim();
        Log.d(TAG, "read: strSchool=" + strSchool);
        strType = etType.getText().toString().trim();
        Log.d(TAG, "read: strType=" + strType);
        strStartDate = btStartDate.getText().toString().trim();
        Log.d(TAG, "read: strStartDate=" + strStartDate);
        strStartTime = btStartTime.getText().toString().trim();
        strEndDate = btEndDate.getText().toString().trim();
        strEndTime = btEndTime.getText().toString().trim();
        strReason = etReason.getText().toString().trim();
        strDestination = etDestination.getText().toString().trim();
        strExplain = etExplain.getText().toString().trim();
        strReviewer1 = etReviewer1.getText().toString().trim();
        strReviewer2 = etReviewer2.getText().toString().trim();
        strApplicationDate = btApplicationDate.getText().toString().trim();

        int intStartYear, intStartMonth, intStartDay;
        String strStartYear, strStartMont, strStartDay;

        String[] dates = strStartDate.split("-");
        intStartYear = Integer.parseInt(dates[0]);
        intStartMonth = Integer.parseInt(dates[1]);
        intStartDay = Integer.parseInt(dates[2]);

        strStartYear = String.format("%04d", intStartYear);
        strStartMont = String.format("%02d", intStartMonth);
        strStartDay = String.format("%02d", intStartDay);

        int intStartHour, intStartMinute;
        String strStartHour, strStartMinute;

        String[] times = strStartTime.split(":");
        intStartHour = Integer.parseInt(times[0]);
        intStartMinute = Integer.parseInt(times[1]);
        strStartHour = String.format("%02d", intStartHour);
        strStartMinute = String.format("%02d", intStartMinute);

        int intEndYear, intEndMonth, intEndDay;
        String strEndYear, strEndMont, strEndDay;

        dates = strEndDate.split("-");
        intEndYear = Integer.parseInt(dates[0]);
        intEndMonth = Integer.parseInt(dates[1]);
        intEndDay = Integer.parseInt(dates[2]);

        strEndYear = String.format("%04d", intEndYear);
        strEndMont = String.format("%02d", intEndMonth);
        strEndDay = String.format("%02d", intEndDay);

        int intEndHour, intEndMinute;
        String strEndHour, strEndMinute;

        times = strEndTime.split(":");
        intEndHour = Integer.parseInt(times[0]);
        intEndMinute = Integer.parseInt(times[1]);
        strEndHour = String.format("%02d", intEndHour);
        strEndMinute = String.format("%02d", intEndMinute);


        int intApplicationYear, intApplicationMonth, intApplicationDay;
        String strApplicationYear, strApplicationMonth, strApplicationDay;

        dates = strApplicationDate.split("-");
        intApplicationYear = Integer.parseInt(dates[0]);
        intApplicationMonth = Integer.parseInt(dates[1]);
        intApplicationDay = Integer.parseInt(dates[2]);

        strApplicationYear = String.format("%04d", intApplicationYear);
        strApplicationMonth = String.format("%02d", intApplicationMonth);
        strApplicationDay = String.format("%02d", intApplicationDay);

        strApplicationTime = strApplicationMonth + "-" + strApplicationDay + " " + strApplicationHour;
        strRev1Time = strApplicationMonth + "-" + strApplicationDay + " " + strRev1Hour;
        strRev2Time = strApplicationMonth + "-" + strApplicationDay + " " + strRev2Hour;

        strStart = strStartYear + "-" + strStartMont + "-" + strStartDay + " " + strStartHour + "时";
        strEnd = strEndYear + "-" + strEndMont + "-" + strEndDay + " " + strEndHour + "时";
        int intDuration = (intEndDay-intStartDay)*24+(intEndHour-intStartHour);
        strDuration = intDuration+"小时";

        strName2 = getName2(strName);
        strReviewer12 = getName2(strReviewer1);
        strReviewer22 = getName2(strReviewer2);

        strRev1Pass = strReviewer1 + "审核（已通过）";
        strRev2Pass = strReviewer2 + "审核（已通过）";
    }

    /**
     * 得到两个字的姓名，放在头像处
     * 
     * @param name 原姓名
     * @return 两个字的姓名
     */
    private String getName2(String name) {
        Log.d(TAG, "getName2: "+name+" len="+name.length());
        String name2;
        if (name.length() > 2) {
            name2 = name.substring(name.length() - 2, name.length());
        } else {
            name2 = name;
        }
        Log.d(TAG, "getName2: name2="+name2);
        return name2;
    }

    /**
     * 创建新的请假页面
     *
     * @param newPath sd卡内的某个路径
     * @throws IOException 异常
     */
    private void createLeaveWeb(String newPath) throws IOException {
        InputStream is = this.getResources().getAssets().open(leaveName);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        String line = null;
        LinkedList<String> list = new LinkedList<String>();
        int num = 0;
        while ((line = reader.readLine()) != null) {
            ++num;
            switch (num) {
                case 92:
                    line = strName2;
                    break;
                case 97:
                    line = strName;
                    break;
                case 107:
                    line = strSchool;
                    break;
                case 115:
                    line = strType;
                    break;
                case 121:
                    line = strDuration;
                    break;
                case 129:
                    line = strStart;
                    break;
                case 135:
                    line = strEnd;
                    break;
                case 144:
                    line = strReason;
                    break;
                case 150:
                    line = strDestination;
                    break;
                case 156:
                    line = strExplain;
                    break;
                case 174:
                    line = strName2;
                    break;
                case 178:
                    line = strApplicationTime;
                    break;
                case 187:
                    line = strReviewer12;
                    break;
                case 191:
                    line = strRev1Pass;
                    break;
                case 193:
                    line = strRev1Time;
                    break;
                case 202:
                    line = strReviewer22;
                    break;
                case 206:
                    line = strRev2Pass;
                    break;
                case 208:
                    line = strRev2Time;
                    break;
                case 224:
                    line = strApplicationCost;
                    break;
            }

            list.add(line);
        }

        reader.close();
        Log.d(TAG, "总行数="+num);

        File file = new File(newPath+File.separator+leaveName);
        if (file.exists()){
            Log.d(TAG, "createLeaveWeb: web文件已存在");
        }else{
            try {
                file.createNewFile();
                Log.d(TAG, "createLeaveWeb: 创建web文件");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Log.d(TAG, "createLeaveWeb: 向web文件写入数据");
        try {
            FileWriter fw = new FileWriter(file);
            for (int i = 0; i<=list.size()-1; i++){
                fw.write(list.get(i));
                //fw.flush();
            }
            fw.close();
            Log.d(TAG, "createLeaveWeb: 写入web成功");
        } catch (IOException e){
            Log.d(TAG, "createLeaveWeb: 写入web失败");
            e.printStackTrace();
        }
    }

    /**
     * 创建新的延期页面
     *
     * @param newPath sd卡内的某个路径
     * @throws IOException 异常
     */
    private void createDelayWeb(String newPath) throws IOException {
        InputStream is = this.getResources().getAssets().open(delayName);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        String line = null;
        LinkedList<String> list = new LinkedList<String>();
        int num = 0;
        while ((line = reader.readLine()) != null) {
            ++num;
            switch (num) {
                case 63:
                    line = strType;
                    break;
                case 73:
                    line = strReason;
                    break;
                case 83:
                    line = strStart;
                    break;
                case 93:
                    line = strEnd;
                    break;
                case 103:
                    line = strDuration;
                    break;
                case 113:
                    line = strDestination;
                    break;
                case 123:
                    line = strExplain;
                    break;
                case 139:
                    line = strEnd;
                    break;
            }

            list.add(line);
        }

        reader.close();
        Log.d(TAG, "总行数="+num);

        File file = new File(newPath+File.separator+delayName);
        if (file.exists()){
            Log.d(TAG, "createDelayWeb: web文件已存在");
        }else{
            try {
                file.createNewFile();
                Log.d(TAG, "createDelayWeb: 创建web文件");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Log.d(TAG, "createDelayWeb: 向web文件写入数据");
        try {
            FileWriter fw = new FileWriter(file);
            for (int i = 0; i<=list.size()-1; i++){
                fw.write(list.get(i));
                //fw.flush();
            }
            fw.close();
            Log.d(TAG, "createDelayWeb: 写入web成功");
        } catch (IOException e){
            Log.d(TAG, "createDelayWeb: 写入web失败");
            e.printStackTrace();
        }
    }

    /**
     * 创建新的销假页面
     *
     * @param newPath sd卡内的某个路径
     * @throws IOException 异常
     */
    private void createCancelWeb(String newPath) throws IOException {
        InputStream is = this.getResources().getAssets().open(cancelName);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        String line = null;
        LinkedList<String> list = new LinkedList<String>();
        int num = 0;
        while ((line = reader.readLine()) != null) {
            ++num;
            switch (num) {
                case 63:
                    line = strType;
                    break;
                case 73:
                    line = strReason;
                    break;
                case 83:
                    line = strStart;
                    break;
                case 93:
                    line = strEnd;
                    break;
                case 103:
                    line = strDuration;
                    break;
                case 113:
                    line = strDestination;
                    break;
                case 123:
                    line = strExplain;
                    break;
                case 139:
                    line = strEnd;
                    break;
            }

            list.add(line);
        }

        reader.close();
        Log.d(TAG, "总行数="+num);

        File file = new File(newPath+File.separator+cancelName);
        if (file.exists()){
            Log.d(TAG, "createCancelWeb: web文件已存在");
        }else{
            try {
                file.createNewFile();
                Log.d(TAG, "createCancelWeb: 创建web文件");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Log.d(TAG, "createCancelWeb: 向web文件写入数据");
        try {
            FileWriter fw = new FileWriter(file);
            for (int i = 0; i<=list.size()-1; i++){
                fw.write(list.get(i));
                //fw.flush();
            }
            fw.close();
            Log.d(TAG, "createCancelWeb: 写入web成功");
        } catch (IOException e){
            Log.d(TAG, "createCancelWeb: 写入web失败");
            e.printStackTrace();
        }
    }

    /**
     * 从assets目录中复制整个文件夹内容
     * 将运行html所需要的相关js文件和css文件以及图片资源拷贝到sd卡中
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldFile String  原文件夹名  如：leaveDirName
     * @param newFile String  复制后路径  如：xx:/bb/cc
     */
    public void copyFilesFromAssets(Context context, String oldFile, String newFile) {
        try {
            String fileNames[] = context.getAssets().list(oldFile);//获取assets目录下的所有文件及目录名

            Log.d(TAG, "num="+fileNames.length);
            //Toast.makeText(MainActivity.this, "num="+fileNames.length , Toast.LENGTH_SHORT).show();

            if (fileNames.length > 0) {//如果是目录
                File file = new File(newFile);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, oldFile + "/" + fileName, newFile + "/" + fileName);
                }
            } else {//如果是文件
                Log.d(TAG, "oldPath="+oldFile);
                Log.d(TAG, "newPath="+newFile);
                //Toast.makeText(MainActivity.this, oldPath, Toast.LENGTH_SHORT).show();

                InputStream is = context.getAssets().open(oldFile);
                FileOutputStream fos = new FileOutputStream(new File(newFile));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
            //MainActivity.handler.sendEmptyMessage(COPY_FALSE);
        }
    }

    /**
     * 检查app权限（好像不需要）
     */
    public void checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //读sd卡权限
                isGranted = false;
            }
            Log.i("cbs","isGranted == "+isGranted);
            if (!isGranted) {
                this.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
            }
        }

    }

    /**
     * pb_button触发时传递信息创建页面
     */
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //创建leave页面
            try {
                createLeaveWeb(newPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //移动leave文件夹
            File file = new File(newLeaveDirPath);
            if (file.exists()){
                Log.d(TAG, "onClick: leave文件夹已经存在，不需要移动");
            }else{
                Log.d(TAG, "onClick: 移动leave文件夹...");
                copyFilesFromAssets(context, leaveDirName, newLeaveDirPath);
            }

            //创建delay页面
            try {
                createDelayWeb(newPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //copyFilesFromAssets(context, delayName, newDelayPath);

            //移动delay文件夹
            file = new File(newDelayDirPath);
            if (file.exists()){
                Log.d(TAG, "onClick: delay文件夹已经存在，不需要移动");
            }else{
                Log.d(TAG, "onClick: 移动delay文件夹...");
                copyFilesFromAssets(context, delayDirName, newDelayDirPath);
            }

            //创建cancel页面
            try {
                createCancelWeb(newPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //copyFilesFromAssets(context, cancelName, newCancelPath);

            //移动cancel文件夹
            file = new File(newCancelDirPath);
            if (file.exists()){
                Log.d(TAG, "onClick: cancel文件夹已经存在，不需要移动");
            }else{
                Log.d(TAG, "onClick: 移动cancel文件夹...");
                copyFilesFromAssets(context, cancelDirName, newCancelDirPath);
            }

            //移动images文件夹
            file = new File(newImgDirPath);
            if (file.exists()){
                Log.d(TAG, "onClick: images文件夹已经存在，不需要移动");
            }else{
                Log.d(TAG, "onClick: 移动images文件夹...");
                copyFilesFromAssets(context, imgDirName, newImgDirPath);
            }

            //转入新activity，在新页面中打开网页
//            Intent intent = new Intent(MainActivity.this, WebActivity.class);
//            startActivity(intent);

            //保存用户输入的数据
            saveData();

            pb_button.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    Intent i=new Intent();
                    i.setClass(MainActivity.this,WebActivity.class);
                    startActivity(i);

                    changeButtonColor("#00000000"); //透明
                    if (isOriginColor.equals("true")) {
                        changeLayoutColor(originButtonColor);
                    }else{
                        changeLayoutColor(vipButtonColor);
                    }
                }
            });

        }
    };

    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

}