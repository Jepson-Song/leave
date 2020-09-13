package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class GithubActivity extends AppCompatActivity {

    private static final String TAG = "GithubActivity";
    private WebView wvWeb;

    private SharedPreferences sp;

    private String originButtonColor = "#26bcd5";
    private String vipButtonColor = "#cc6699";
    private String isOriginColor;

    private String nowUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);

        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        isOriginColor = sp.getString("isOriginColor", "true");
        if (isOriginColor.equals("true")){
            changeStatusBarColor(originButtonColor);
        }else{
            changeStatusBarColor(vipButtonColor);
        }
        openWeb();
    }
    private int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
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

    void onDownloadStart(String url) {
//创建request对象
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
        //设置什么网络情况下可以下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏的标题
        request.setTitle("下载");
        //设置通知栏的message
        request.setDescription("今日头条正在下载.....");
        //设置漫游状态下是否可以下载
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,"update.apk");
        //获取系统服务
        DownloadManager downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //进行下载
        long id = downloadManager.enqueue(request);
    }

    private void openWeb(){
        wvWeb = (WebView) findViewById(R.id.wvWeb);
        //需要加载的网页的url
        //String url = "file:///" + getExternalFilesDir() + "/demo.html";
        //Log.d(TAG, "openWeb: "+url);
        wvWeb.loadUrl("https://github.com/Jepson-Song/leave");//这里写的是assets文件夹下html文件的名称，需要带上后面的后缀名，前面的路径是安卓系统自己规定的android_asset就是表示的在assets文件夹下的意思。
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
//        wvWeb.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                // TODO 实现下载逻辑
//                Log.e("onDownloadStart", "url===" + url + "---userAgent=" + userAgent + "---contentDisposition=" + contentDisposition + "---mimetype=" + mimetype + "---contentLength=" + contentLength);
//                nowUrl = wvWeb.getUrl();
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(nowUrl));
//                // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
//                request.allowScanningByMediaScanner();
//                // 设置通知的显示类型，下载进行时和完成后显示通知
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                // 设置通知栏的标题，如果不设置，默认使用文件名
//                request.setTitle("下载完成");
//                // 设置通知栏的描述
////                    request.setDescription("This is description");
//                // 允许在计费流量下下载
//                request.setAllowedOverMetered(true);
//                // 允许该记录在下载管理界面可见
//                request.setVisibleInDownloadsUi(true);
//                // 允许漫游时下载
//                request.setAllowedOverRoaming(true);
//
//                String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
//                Log.e("fileName:{}", fileName);
//                request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory() + "/Download/", fileName);
//
//
//                final DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                // 添加一个下载任务
//                long downloadId = downloadManager.enqueue(request);
//            }
//        });
    }

}