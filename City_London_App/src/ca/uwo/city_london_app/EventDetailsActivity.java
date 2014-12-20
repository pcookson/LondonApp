
package ca.uwo.city_london_app;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import ca.uwo.city_london_app.Service.SyncHttp;

public class EventDetailsActivity extends Activity {

	private final int FINISH = 0; 
	private LayoutInflater mEventbodyLayoutInflater;
	private ViewFlipper mEventBodyFlipper; 
	private ArrayList<HashMap<String, Object>> mNewsData;
	private Button mFavoritesButton; 
	private SQLiteDatabase mysql;

	private int mPosition = 0; 
	private int mCursor = 0; 

	private String mUrlTitle; 
	private float mStartX; 
	private CustomTextView mNewsBodyDetail;
	private int mCounter = 1; 
	private ArrayList<HashMap<String, Object>> mBodyList;
	private String mNewsTitle;
	private String mEventFromDate;
	private String mEventToDate;
	private String mEventPhone;
	private String mEventLocation;
	private String mEventText;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.arg1) {
			case FINISH:
				ArrayList<HashMap<String, Object>> bodyList = (ArrayList<HashMap<String, Object>>) msg.obj;
				mNewsBodyDetail.setText(bodyList);
				mBodyList = bodyList;// for save news detail purpose
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventdetails_layout);

		Button eventDetailPrev = (Button) findViewById(R.id.eventdetail_titlebar_previous);
		Button eventDetailNext = (Button) findViewById(R.id.eventdetail_titlebar_next);
		mFavoritesButton = (Button) findViewById(R.id.eventdetail_titlebar_save);

		EventDetailOnClickListener eventDetailOnClickListener = new EventDetailOnClickListener();

		eventDetailPrev.setOnClickListener(eventDetailOnClickListener);
		eventDetailNext.setOnClickListener(eventDetailOnClickListener);

		mFavoritesButton.setOnClickListener(eventDetailOnClickListener);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		String categoryName = bundle.getString("categoryTitle");
		TextView titleBarTitle = (TextView) findViewById(R.id.eventdetail_titlebar_title);
		titleBarTitle.setText(categoryName);

		Serializable serializable = bundle.getSerializable("eventData");
		mNewsData = (ArrayList<HashMap<String, Object>>) serializable;

		mCursor = mPosition = bundle.getInt("position");



		// get LayoutInflater object
		mEventbodyLayoutInflater = getLayoutInflater();

		inflateView(0);

	}

	/**
	 * previous event
	 */
	private void showPrevious() {
		if (mPosition > 0) {
			mPosition--;
			// current event
			HashMap<String, Object> hashMap = mNewsData.get(mPosition);
			mUrlTitle = (String) hashMap.get("url_title");
			if (mCursor > mPosition) {
				mCursor = mPosition;
				inflateView(0);
			} else
				mCounter--;
			mEventBodyFlipper.setInAnimation(this, R.anim.push_right_in); 
			mEventBodyFlipper.setOutAnimation(this, R.anim.push_right_out); 
			mEventBodyFlipper.showPrevious();



		} else {
			Toast.makeText(EventDetailsActivity.this, "No more previous event",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * next event
	 */
	private void showNext() {
		if (mPosition < mNewsData.size() - 1) {
			mEventBodyFlipper.setInAnimation(this, R.anim.push_left_in);
			mEventBodyFlipper.setOutAnimation(this, R.anim.push_left_out);
			mPosition++;

			HashMap<String, Object> hashMap = mNewsData.get(mPosition);
			mUrlTitle = (String) hashMap.get("url_title");
			if (mCounter >= mEventBodyFlipper.getChildCount()) {// mCounter
				inflateView(mEventBodyFlipper.getChildCount());
			}

			mEventBodyFlipper.showNext();

			mCounter++;// mCounter


		} else {
			Toast.makeText(EventDetailsActivity.this, "No more next event",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void inflateView(int index) {
		// get event
		HashMap<String, Object> hashMap = mNewsData.get(mPosition);
		mUrlTitle = (String) hashMap.get("url_title");

		// eventbody_layout
		View mEventBodyView = mEventbodyLayoutInflater.inflate(
				R.layout.eventbody_layout, null);



		// event title
		TextView newsTitle = (TextView) mEventBodyView
				.findViewById(R.id.event_body_title);
		newsTitle.setText(hashMap.get("eventlist_item_title").toString());
		mNewsTitle = hashMap.get("eventlist_item_title").toString();// save item

		// event date + phone number
		TextView eventDate = (TextView) mEventBodyView
				.findViewById(R.id.event_body_date);
		eventDate.setText("Date: "
				+ hashMap.get("eventlist_item_fromdate").toString() + " - "
				+ hashMap.get("eventlist_item_todate").toString()
				+ "        Phone: "
				+ hashMap.get("eventlist_item_phone").toString());
		mEventFromDate = hashMap.get("eventlist_item_fromdate").toString();// save
																			// item
		mEventToDate = hashMap.get("eventlist_item_todate").toString();// save
																		// item
		mEventPhone = hashMap.get("eventlist_item_phone").toString();// save
																		// item

		// event address
		TextView eventLocation = (TextView) mEventBodyView
				.findViewById(R.id.event_body_location);
		eventLocation
				.setText(hashMap.get("eventlist_item_location").toString());
		mEventLocation = hashMap.get("eventlist_item_location").toString();// save
																			// item

		// mNewsBodyDetail.setText(getNewsBody());

		// start thread
		new UpdateNewsThread().start();

		// event content
		mNewsBodyDetail = (CustomTextView) mEventBodyView
				.findViewById(R.id.event_body_details);
		mNewsBodyDetail.setOnTouchListener(new EventBodyOntouchListener());

		// add event view to Flipper
		mEventBodyFlipper = (ViewFlipper) findViewById(R.id.event_body_flipper);
		mEventBodyFlipper.addView(mEventBodyView, index);

	}

	// save event
	private void saveEvent() {
		// sdcard
		String dbPath = android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/database";
		File f = new File(dbPath + "/" + "event.db");
		try {
			for (HashMap<String, Object> hashMap : mBodyList) {
				
				String type = (String) hashMap.get("type");
				if (type.equals("text")) {
					mEventText = hashMap.get("value").toString();

				}
			}

			if (mNewsTitle.indexOf("'") != -1)// check if '
				mNewsTitle = mNewsTitle.replace("'", "''");

			if (mEventText.indexOf("'") != -1)// check if '
				mEventText = mEventText.replace("'", "''");

			mysql = SQLiteDatabase.openOrCreateDatabase(f, null);

			// check if existed
			String QUERY = "select * from saved_event where urltitle= '"
					+ mUrlTitle + "'; ";
			Cursor cur = mysql.rawQuery(QUERY, null);
			if (cur.getCount() != 0)
				Toast.makeText(EventDetailsActivity.this, "Saved already!",
						Toast.LENGTH_SHORT).show();
			else {
				// storage limitation
				String QUERYALL = "select * from saved_event; ";
				cur = mysql.rawQuery(QUERYALL, null);
				if (cur.getCount() > 500)
					Toast.makeText(EventDetailsActivity.this,
							"Too many saved event, please clear.",
							Toast.LENGTH_SHORT).show();
				else {
					String INSERT_DATA = "INSERT INTO saved_event values('"
							+ mNewsTitle + "', '" + mUrlTitle + "','"
							+ mEventFromDate + "', '" + mEventToDate + "','"
							+ mEventPhone + "', '" + mEventLocation + "','"
							+ mEventText + "')";
					mysql.execSQL(INSERT_DATA);
					Toast.makeText(EventDetailsActivity.this,
							"Saved successfully!", Toast.LENGTH_SHORT).show();
					System.out.println("saved successfully");
				}
			}
		} catch (Exception e) {
			System.out.println("operation failed！");
			Toast.makeText(EventDetailsActivity.this,
					"Saved failed, please check your SD card", Toast.LENGTH_SHORT).show();
		} finally {
			mysql.close();
		}

	}

	// 定义内部类--用于处理标题栏的按钮的触发事件
	private class EventDetailOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*
			 * switch (v.getId()) { //上一篇 case
			 * R.id.eventdetail_titlebar_previous: showPrevious(); break; //下一篇
			 * case R.id.eventdetail_titlebar_next: showNext(); break;
			 * 
			 * //收藏按钮 // case R.id.news_favorites_btn: //
			 * Toast.makeText(EventDetailActivity.this, "收藏成功",
			 * Toast.LENGTH_SHORT).show(); // break; }
			 */

			if (v.getId() == R.id.eventdetail_titlebar_previous)
				showPrevious();
			else if (v.getId() == R.id.eventdetail_titlebar_next)
				showNext();
			else if (v.getId() == R.id.eventdetail_titlebar_save) {
				saveEvent();
			}
		}
	}

	private class EventBodyOntouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			// TODO Auto-generated method stub
			switch (event.getAction()) {
			// 手指按下
			case MotionEvent.ACTION_DOWN:
				/*
				 * if(keyboardShow){
				 * mNewsReplyEditLayout.setVisibility(View.GONE);
				 * mNewsReplyImgLayout.setVisibility(View.VISIBLE); //隐藏输入法
				 * InputMethodManager m = (InputMethodManager)
				 * mNewsReplyEditText
				 * .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				 * m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				 * keyboardShow = false; }
				 */
				// 得到按下的横坐标的位置
				mStartX = event.getX();
				break;
			case MotionEvent.ACTION_UP:
				// 往左滑动
				if (event.getX() < mStartX) {
					showNext();
				}
				// 往右滑动
				else if (event.getX() > mStartX) {
					showPrevious();
				}
				break;
			}
			return true;
		}
	}

	/**
	 * 定义一个线程类，用来更新获取到新闻的信息
	 * 
	 * @author Administrator
	 * 
	 */
	private class UpdateNewsThread extends Thread {
		@Override
		public void run() {

			// TODO Auto-generated method stub
			// System.out.println("format news body in thread");
			ArrayList<HashMap<String, Object>> newsStr = getNewsBody();
			Message msg = mHandler.obtainMessage(); // 获取msg
			msg.arg1 = FINISH;
			msg.obj = newsStr;
			// System.out.println("news string=" + newsStr);
			mHandler.sendMessage(msg); // 给Handler发送信息
		}
	}

	/**
	 * 获取新闻详细信息
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getNewsBody() {
		// System.out.println("get news body");
		// String retStr = "网络连接失败,请稍后再试";
		ArrayList<HashMap<String, Object>> bodylist = new ArrayList<HashMap<String, Object>>();
		SyncHttp syncHttp = new SyncHttp();
		// 模拟器:url = "http://10.0.2.2:8080/web/getNews";
		// 本机:http://127.0.0.1:8080
		// wifi局域网:http://192.168.220.1:8080
		// String url = "http://10.0.2.2:8080/web/getNews";
		String url = "http://129.100.92.139:8080/web/getNews";
		// String params = "nid=" + mNid;
		// String params = "eventtitle=" + mEventTitle;
		String params = "urltitle=" + mUrlTitle;
		// System.out.println("urlTitle in getNews test=" + mUrlTitle);
		try {
			String retString = syncHttp.httpGet(url, params);
			// System.out.println("what we get from url? = " + retString);
			JSONObject jsonObject = new JSONObject(retString);
			// 获取返回码，0表示成功
			int retCode = jsonObject.getInt("ret");
			// System.out.println("ret code=" + retCode);
			if (retCode == 0) {
				JSONObject dataObject = jsonObject.getJSONObject("data");
				JSONObject newsObject = dataObject.getJSONObject("news");
				// retStr = newsObject.getString("body");
				// System.out.println("testing code=" + retStr);
				JSONArray bodyArray = newsObject.getJSONArray("body");
				for (int i = 0; i < bodyArray.length(); i++) {
					JSONObject object = (JSONObject) bodyArray.opt(i);
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("index", object.get("index"));
					hashMap.put("type", object.get("type"));
					hashMap.put("value", object.get("value"));
					// System.out.println("running?");
					// System.out.println("index=" + object.get("index") +
					// "type=" + object.get("type") + "value=" +
					// object.get("value"));
					bodylist.add(hashMap);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bodylist;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_datails, menu);
		return true;
	}

}
