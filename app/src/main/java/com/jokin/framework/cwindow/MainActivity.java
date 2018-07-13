package com.jokin.framework.cwindow;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    protected WindowManager mWindowManager;

    protected ViewGroup mContentView;
    protected WindowManager.LayoutParams mContentParams;

    protected FloatView mFloatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.addWindow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FloatView floatView = new FloatView(MainActivity.this);
                // floatView.setLayout(R.layout.layout_module);
                // floatView.show();

                // Intent intent = new Intent(MainActivity.this, ModuleService.class);
                // startService(intent);

                // floatWindow();

                subWindow();
                // realShow();
            }
        });


        // mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        // final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        //         WindowManager.LayoutParams.MATCH_PARENT,
        //         WindowManager.LayoutParams.MATCH_PARENT,
        //         WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
        //         WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        //         PixelFormat.TRANSPARENT);
        // params.gravity = Gravity.START | Gravity.TOP;
        //
        // mContentParams = params;
        //
        // mContentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_module, null);
        // ((TextView)mContentView.findViewById(R.id.text)).setText("Module A!");
        // mWindowManager.addView(mContentView, params);

        //
        mFloatView = new FloatView(this);
        mFloatView.setLayout(R.layout.layout_module);
    }

    private void subWindow() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.e(TAG, "buidl sdk >= 23");
            if (Settings.canDrawOverlays(this)) {
                Log.e(TAG, "can draw overlays");
                //有悬浮窗权限开启服务绑定 绑定权限
                realShow();
            } else {
                //没有悬浮窗权限m,去开启悬浮窗权限
                try {
                    Log.e(TAG, "open permission manager");
                    Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent2, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            //默认有悬浮窗权限  但是 华为, 小米,oppo等手机会有自己的一套Android6.0以下  会有自己的一套悬浮窗权限管理 也需要做适配
            Log.e(TAG, "build sdk < 23");
            realShow();
        }
    }

    private void realShow() {
        final SubWindowView subWindowView = new SubWindowView(CApplication.getInstance());
        subWindowView.setLayout(R.layout.layout_module);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subWindowView.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult");

        // Intent intent = new Intent(MainActivity.this, ModuleService.class);
        // Intent intentA = new Intent(MainActivity.this, ModuleAService.class);
        // Intent intentB = new Intent(MainActivity.this, ModuleBService.class);
        //
        // startService(intent);
        // startService(intentA);
        // startService(intentB);
        realShow();
    }

    private void floatWindow() {
        Intent intent = new Intent(MainActivity.this, ModuleService.class);
        Intent intentA = new Intent(MainActivity.this, ModuleAService.class);
        Intent intentB = new Intent(MainActivity.this, ModuleBService.class);

        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                //有悬浮窗权限开启服务绑定 绑定权限
                startService(intent);
                startService(intentA);
                startService(intentB);

            } else {
                //没有悬浮窗权限m,去开启悬浮窗权限
                try {
                    Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent2, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            //默认有悬浮窗权限  但是 华为, 小米,oppo等手机会有自己的一套Android6.0以下  会有自己的一套悬浮窗权限管理 也需要做适配
            startService(intent);
            startService(intentA);
            startService(intentB);
        }
    }
}
