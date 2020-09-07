package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

public class WebActivity extends AppCompatActivity {
    private static final String TAG = "WebActivity";
    private WebView wvWeb;
    private String leaveName = "请假.html";


    private Context context;
    private String newPath, newWebPath;

    private SharedPreferences sp;

    private String originButtonColor = "#26bcd5";
    private String vipButtonColor = "#cc6699";
    private String isOriginColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        context = getApplicationContext();
        newPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Log.d(TAG, "onCreate: newPath="+newPath);
        newWebPath = newPath + File.separator + leaveName;
        Log.d(TAG, "onCreate: newWebPath="+newWebPath);

        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        isOriginColor = sp.getString("isOriginColor", "true");
        if (isOriginColor.equals("true")){
            changeStatusBarColor(originButtonColor);
        }else{
            changeStatusBarColor(vipButtonColor);
        }

        openWeb();
    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击回退键时，不会退出浏览器而是返回网页上一页
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvWeb.canGoBack()) {
            wvWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openWeb(){
        wvWeb = (WebView) findViewById(R.id.wvWeb);
        //需要加载的网页的url
        //String url = "file:///" + getExternalFilesDir() + "/demo.html";
        //Log.d(TAG, "openWeb: "+url);
        wvWeb.loadUrl("file://"+newWebPath);//这里写的是assets文件夹下html文件的名称，需要带上后面的后缀名，前面的路径是安卓系统自己规定的android_asset就是表示的在assets文件夹下的意思。
        wvWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        wvWeb.getSettings().setLoadWithOverviewMode(true);//自适应屏幕
        wvWeb.getSettings().setSupportZoom(true);
        wvWeb.getSettings().setUseWideViewPort(true);//扩大比例的缩放
        wvWeb.getSettings().setBuiltInZoomControls(true);//设置是否出现缩放工具
        WebSettings settings = wvWeb.getSettings();
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        wvWeb.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
    }
}