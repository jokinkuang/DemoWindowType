package com.jokin.framework.cwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class FloatView extends View {
    private final static String TAG = "FloatView";
 
	private Context mContext;
	private WindowManager wm;
	private static WindowManager.LayoutParams wmParams;
	public View mContentView;
	private float mRelativeX;
	private float mRelativeY;
	private float mScreenX;
	private float mScreenY;
	private boolean bShow = false;
 
	public FloatView(Context context) {
		super(context);
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if (wmParams == null) {
			wmParams = new WindowManager.LayoutParams();
		}
		mContext = context;
	}

	public View getContentView() {
		return mContentView;
	}

	private int getStatusBarHeight() {
		int statusBarHeight1 = -1;
		//获取status_bar_height资源的ID
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
		}
		Log.e("WangJ", "状态栏-方法1:" + statusBarHeight1);
		return statusBarHeight1;
	}

	private Rect getDisplayRect() {
		Rect rectangle= new Rect();
		wm.getDefaultDisplay().getRectSize(rectangle);
		Log.d(TAG, "getDisplayRect: "+ rectangle);
		return rectangle;
	}

	private static int index = -1;
	private int getColor() {
		index += 1;
		switch (index) {
			case 0:return Color.RED;
			case 1:return Color.BLUE;
			case 2:return Color.GREEN;
			case 3:return Color.GRAY;
			case 4:return Color.YELLOW;
			case 5:return Color.DKGRAY;
			case 6:return Color.LTGRAY;
			default:
				return Color.CYAN;
		}
	}

	public void setLayout(int layout_id) {
		mContentView = LayoutInflater.from(mContext).inflate(layout_id, null);
		mContentView.setBackgroundColor(getColor());
		mContentView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "rawx="+event.getRawX()+",x="+event.getX());
				mScreenX = event.getRawX();
				mScreenY = event.getRawY();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					getDisplayRect();

					mRelativeX = event.getX();
					mRelativeY = event.getY()+getStatusBarHeight();
					break;
				case MotionEvent.ACTION_MOVE:
					updateViewPosition();
					break;
				case MotionEvent.ACTION_UP:
					updateViewPosition();
					activate();
					mRelativeX = mRelativeY = 0;
					break;
				}
				return true;
			}
		});

		getContentView().findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(FloatView.this.getContext(), "click", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void activate() {
		// if (! blActive) {
			wm.removeViewImmediate(mContentView);
			wm.addView(mContentView, wmParams);
			// blActive = true;
		// }
	}

	private boolean blActive;
	private void updateViewPosition() {
		wmParams.x = (int) (mScreenX - mRelativeX);
		wmParams.y = (int) (mScreenY - mRelativeY);
		wm.updateViewLayout(mContentView, wmParams);
	}
	
	public void show() {
		int LAYOUT_FLAG;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
		} else {
			LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
		}

		if (mContentView != null) {
			wmParams.type = LAYOUT_FLAG;
			wmParams.format = PixelFormat.RGBA_8888;
			wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			wmParams.alpha = 1.0f;
			wmParams.gravity = Gravity.LEFT | Gravity.TOP;
			wmParams.x = 0;
			wmParams.y = 0;
			wmParams.width = 200;
			wmParams.height = 200;
			// 显示自定义悬浮窗口
			wm.addView(mContentView, wmParams);
			bShow = true;
		}
	}
 
	public void close() {
		if (mContentView != null) {
			wm.removeView(mContentView);
			bShow = false;
		}
	}
	
	public boolean isShow() {
		return bShow;
	}
 
}
