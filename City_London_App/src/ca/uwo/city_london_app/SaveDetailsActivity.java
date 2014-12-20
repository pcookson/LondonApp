/*
 * android 3.2 在用showPrevious()是有bug;
 */

package ca.uwo.city_london_app;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class SaveDetailsActivity extends Activity {

	private LayoutInflater mEventbodyLayoutInflater;
	private ViewFlipper mEventBodyFlipper; 
	private ArrayList<HashMap<String, Object>> mNewsData;
	private SQLiteDatabase mysql;

	private int mPosition = 0; 
	private int mCursor = 0; 

	private String mUrlTitle; 
	private float mStartX; 
	private int mCounter = 1; 

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.savedetails_layout);

		Button eventDetailPrev = (Button) findViewById(R.id.eventdetail_titlebar_previous);
		Button eventDetailNext = (Button) findViewById(R.id.eventdetail_titlebar_next);
		Button mFavoritesButton = (Button) findViewById(R.id.eventdetail_titlebar_unsave);

		EventDetailOnClickListener eventDetailOnClickListener = new EventDetailOnClickListener();

		eventDetailPrev.setOnClickListener(eventDetailOnClickListener);
		eventDetailNext.setOnClickListener(eventDetailOnClickListener);
		mFavoritesButton.setOnClickListener(eventDetailOnClickListener);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		Serializable serializable = bundle.getSerializable("eventData");
		mNewsData = (ArrayList<HashMap<String, Object>>) serializable;

		mCursor = mPosition = bundle.getInt("position");

		mEventbodyLayoutInflater = getLayoutInflater();

		inflateView(0);
	}


	private void showPrevious() {
		if (mPosition > 0) {
			mPosition--;
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
			Toast.makeText(SaveDetailsActivity.this, "No more previous event",
					Toast.LENGTH_SHORT).show();
		}
	}


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
			// / inflateView(0);

			mEventBodyFlipper.showNext();

			mCounter++;// mCounter


		} else {
			Toast.makeText(SaveDetailsActivity.this, "No more next event",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void inflateView(int index) {
		HashMap<String, Object> hashMap = mNewsData.get(mPosition);
		mUrlTitle = (String) hashMap.get("url_title");

		View mEventBodyView = mEventbodyLayoutInflater.inflate(
				R.layout.savebody_layout, null);

		TextView newsTitle = (TextView) mEventBodyView
				.findViewById(R.id.event_body_title);
		newsTitle.setText(hashMap.get("eventlist_item_title").toString());
		System.out.println("title="
				+ hashMap.get("eventlist_item_title").toString());

		TextView eventDate = (TextView) mEventBodyView
				.findViewById(R.id.event_body_date);
		eventDate.setText("Date: "
				+ hashMap.get("eventlist_item_fromdate").toString() + " - "
				+ hashMap.get("eventlist_item_todate").toString()
				+ "        Phone: "
				+ hashMap.get("eventlist_item_phone").toString());

		TextView eventLocation = (TextView) mEventBodyView
				.findViewById(R.id.event_body_location);
		eventLocation
				.setText(hashMap.get("eventlist_item_location").toString());

		TextView eventDescription = (TextView) mEventBodyView
				.findViewById(R.id.event_body_details);
		eventDescription.setText(hashMap.get("event_body").toString());


		eventDescription.setOnTouchListener(new EventBodyOntouchListener());

		mEventBodyFlipper = (ViewFlipper) findViewById(R.id.event_body_flipper);
		mEventBodyFlipper.addView(mEventBodyView, index);
	}

	private void unSaveEvent() {
		String dbPath = android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/database";
		File f = new File(dbPath + "/" + "event.db");
		try {

			mysql = SQLiteDatabase.openOrCreateDatabase(f, null);

			String DELETE_DATA = "DELETE FROM saved_event WHERE urltitle= '"
					+ mUrlTitle + "'; ";
			mysql.execSQL(DELETE_DATA);
			Toast.makeText(SaveDetailsActivity.this, "Unsave successfully!",
					Toast.LENGTH_SHORT).show();
			System.out.println("delete successfully");
		} catch (Exception e) {
			System.out.println("operation failed！");
		} finally {
			mysql.close();
		}

	}

	private class EventDetailOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (v.getId() == R.id.eventdetail_titlebar_previous)
				showPrevious();
			else if (v.getId() == R.id.eventdetail_titlebar_next)
				showNext();
			else if (v.getId() == R.id.eventdetail_titlebar_unsave) {
				unSaveEvent();
			}
		}
	}

	private class EventBodyOntouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				mStartX = event.getX();
				break;
			case MotionEvent.ACTION_UP:
				if (event.getX() < mStartX) {
					showNext();
				}
				else if (event.getX() > mStartX) {
					showPrevious();
				}
				break;
			}
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_datails, menu);
		return true;
	}

}
