package ca.uwo.city_london_app;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomTextView extends LinearLayout {

	private Context mContext;
	private TypedArray mTypedArray;
	private LayoutParams params;

	public CustomTextView(Context context) {
		super(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.setOrientation(LinearLayout.VERTICAL);
		mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.constomTextView);
	}

	public void setText(ArrayList<HashMap<String, Object>> datas) {
		for (HashMap<String, Object> hashMap : datas) {
			String type = (String) hashMap.get("type");
			if (type.equals("image")) {
				int imagewidth = mTypedArray.getDimensionPixelOffset(
						R.styleable.constomTextView_image_width, 100);
				int imageheight = mTypedArray.getDimensionPixelOffset(
						R.styleable.constomTextView_image_height, 100);
				ImageView imageView = new ImageView(mContext);
				params = new LayoutParams(imagewidth, imageheight);
				params.gravity = Gravity.CENTER_HORIZONTAL; // Ã¦â€�Ã·â€“
				imageView.setLayoutParams(params);
				imageView.setImageResource(R.drawable.image_loading);
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
				addView(imageView);
				new DownloadPicThread(imageView, hashMap.get("value")
						.toString()).start();
			} else {
				float textSize = mTypedArray.getDimension(
						R.styleable.constomTextView_textSize, 14);
				int textColor = mTypedArray.getColor(
						R.styleable.constomTextView_textColor, 0xFF272727);
				TextView textView = new TextView(mContext);
				textView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				textView.setText(Html.fromHtml(hashMap.get("value").toString()));
				textView.setTextSize(textSize); 
				textView.setTextColor(textColor); 
				textView.setPadding(10, 0, 10, 0);
				addView(textView);
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> hashMap = (HashMap<String, Object>) msg.obj;
			ImageView imageView = (ImageView) hashMap.get("imageView");
			LayoutParams params = new LayoutParams(msg.arg1, msg.arg2);
			params.gravity = Gravity.CENTER_HORIZONTAL; 
			imageView.setLayoutParams(params);
			Drawable drawable = (Drawable) hashMap.get("drawable");

			imageView.setImageDrawable(drawable); 

		};
	};


	private class DownloadPicThread extends Thread {
		private ImageView imageView;
		private String mUrl;

		public DownloadPicThread(ImageView imageView, String mUrl) {
			super();
			this.imageView = imageView;
			this.mUrl = mUrl;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Drawable drawable = null;
			int newImgWidth = 0;
			int newImgHeight = 0;
			try {
				drawable = Drawable.createFromStream(
						new URL(mUrl).openStream(), "image");

				newImgWidth = drawable.getIntrinsicWidth();
				newImgHeight = drawable.getIntrinsicHeight();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			SystemClock.sleep(2000);
			Message msg = handler.obtainMessage();
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("imageView", imageView);
			hashMap.put("drawable", drawable);
			msg.obj = hashMap;
			msg.arg1 = newImgWidth;
			msg.arg2 = newImgHeight;
			handler.sendMessage(msg);
		}
	}

}
