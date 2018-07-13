package com.jokin.framework.cwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.WindowManager.LayoutParams;

public class SubWindowView extends View {
    private final static String TAG = "FloatView";

	private Context mContext;
	private WindowManager wm;
	private static LayoutParams wmParams;
	public View mContentView;
	private float mRelativeX;
	private float mRelativeY;
	private float mScreenX;
	private float mScreenY;
	private boolean bShow = false;

	private List<Integer> mWindowTypes = new ArrayList<>();

	{
		mWindowTypes.add(LayoutParams.FIRST_APPLICATION_WINDOW); // 1
		mWindowTypes.add(LayoutParams.TYPE_BASE_APPLICATION); // 1
		mWindowTypes.add(LayoutParams.TYPE_APPLICATION); // 2
		mWindowTypes.add(LayoutParams.TYPE_APPLICATION_STARTING); // 3
		mWindowTypes.add(LayoutParams.TYPE_DRAWN_APPLICATION); // 4
		mWindowTypes.add(LayoutParams.LAST_APPLICATION_WINDOW); // 99

		mWindowTypes.add(LayoutParams.FIRST_SUB_WINDOW); // 1000
		mWindowTypes.add(LayoutParams.LAST_SUB_WINDOW); // 1999

		mWindowTypes.add(LayoutParams.FIRST_SYSTEM_WINDOW); // 2000

		mWindowTypes.add(LayoutParams.TYPE_STATUS_BAR); // 2000
		mWindowTypes.add(LayoutParams.TYPE_SEARCH_BAR); // 2001
		mWindowTypes.add(LayoutParams.TYPE_PHONE); // 2002
		mWindowTypes.add(LayoutParams.TYPE_SYSTEM_ALERT); // 2003
		mWindowTypes.add(LayoutParams.TYPE_TOAST); // 2005
		mWindowTypes.add(LayoutParams.TYPE_SYSTEM_OVERLAY); // 2006
		mWindowTypes.add(LayoutParams.TYPE_SYSTEM_DIALOG); // 2008
		mWindowTypes.add(LayoutParams.TYPE_SYSTEM_ERROR); // 2010
		mWindowTypes.add(LayoutParams.TYPE_INPUT_METHOD); // 2011

		mWindowTypes.add(LayoutParams.TYPE_APPLICATION_OVERLAY); // 2038
		mWindowTypes.add(LayoutParams.LAST_SYSTEM_WINDOW); // 2999
	}

	private static int index = -1;
	public SubWindowView(Context context) {
		super(context);
		index++;
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if (wmParams == null) {
			wmParams = new LayoutParams();
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

	private int getColor() {
		switch (index) {
			case 0:return Color.RED;
			case 1:return Color.BLUE;
			case 2:return Color.GREEN;
			case 3:return Color.GRAY;
			case 4:return Color.YELLOW;
			case 5:return Color.DKGRAY;
			case 6:return Color.LTGRAY;
			default:
				return Color.CYAN+0x000010;
		}
	}
	private int getWindowType() {
		if (index < mWindowTypes.size()) {
			return mWindowTypes.get(index);
		}
		return -1;
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

		getContentView().findViewById(R.id.text).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SubWindowView.this.getContext(), "click", Toast.LENGTH_SHORT).show();
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

		int LAYOUT_FLAG = getWindowType();
		((TextView)mContentView.findViewById(R.id.text)).setText(String.valueOf(LAYOUT_FLAG));

		try {

			if (mContentView != null) {
				wmParams.type = LAYOUT_FLAG;
				wmParams.format = PixelFormat.RGBA_8888;
				wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
				wmParams.alpha = 1.0f;
				wmParams.gravity = Gravity.LEFT | Gravity.TOP;
				wmParams.x = 0;
				wmParams.y = 0;
				wmParams.width = 140;
				wmParams.height = 100;
				// 显示自定义悬浮窗口
				wm.addView(mContentView, wmParams);
				Log.e(TAG, "suport i:"+index+"| type:"+LAYOUT_FLAG);
				bShow = true;
			}
		} catch (Exception e) {
			Log.e(TAG, "not suport i:"+index+"| type:"+LAYOUT_FLAG+"|"+e.toString());
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
