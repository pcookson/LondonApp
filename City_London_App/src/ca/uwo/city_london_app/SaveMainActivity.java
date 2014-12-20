package ca.uwo.city_london_app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

//import ca.uwo.city_london_app.util.StringUtil;

public class SaveMainActivity extends Activity {

	private final int SUCCESS = 0; 
	private final int NOEVENT = 1; 
	private final int LOADERROR = 3; 



	private ListView mEventlist; 

	private SimpleAdapter mEventlistAdapter; 
	private ArrayList<HashMap<String, Object>> mEventData; 
	private Button mTitleBarRefresh; 
	private Button mTitleBarClear; 
	private SQLiteDatabase mysql;
	String TABLE_NAME = "saved_event";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_main);

		mTitleBarRefresh = (Button) findViewById(R.id.titlebar_refresh);
		mTitleBarRefresh.setOnClickListener(loadmoreListener);

		mTitleBarClear = (Button) findViewById(R.id.savemain_titlebar_clear);
		mTitleBarClear.setOnClickListener(loadmoreListener);

		mEventData = new ArrayList<HashMap<String, Object>>();

		/*
		 * ======================================================================
		 * =======================================
		 */
		/*
		 * Get event list content from server
		 */

		getSpecCatEvent(mEventData);

		mEventlistAdapter = new SimpleAdapter(this, mEventData,
				R.layout.eventlist_item_layout, new String[] {
						"eventlist_item_title", "eventlist_item_date",
						"eventlist_item_location" }, new int[] {
						R.id.eventlist_item_title, R.id.eventlist_item_date,
						R.id.eventlist_item_location });
		mEventlist = (ListView) findViewById(R.id.event_list);

		mEventlist.setAdapter(mEventlistAdapter);

		mEventlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SaveMainActivity.this,
						SaveDetailsActivity.class);
				intent.putExtra("eventData", mEventData);
				intent.putExtra("position", position);
				startActivity(intent);
			}

		});

	}



	int getSpecCatEvent(List<HashMap<String, Object>> eventList) {

		eventList.clear();

		String dbPath = android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/database";

		File f = new File(dbPath + "/" + "event.db");
		try {
			mysql = SQLiteDatabase.openOrCreateDatabase(f, null);

			Cursor cur = mysql.rawQuery("SELECT * FROM " + TABLE_NAME, null);
			if (cur != null) {
				if (cur.getCount() == 0)
					Toast.makeText(SaveMainActivity.this, "No saved event",
							Toast.LENGTH_SHORT).show();

				while (cur.moveToNext()) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();

					hashMap.put("eventlist_item_title", cur.getString(0));
					hashMap.put("url_title", cur.getString(1));
					hashMap.put("eventlist_item_fromdate", cur.getString(2));
					hashMap.put("eventlist_item_todate", cur.getString(3));
					hashMap.put("eventlist_item_phone", cur.getString(4));
					hashMap.put("eventlist_item_location", cur.getString(5));
					hashMap.put("event_body", cur.getString(6));
					String eventDate = "From Date:" + cur.getString(2)
							+ "    To Date:" + cur.getString(3);
					hashMap.put("eventlist_item_date", eventDate);
					eventList.add(hashMap);
				}
				return SUCCESS;
			} else
				return NOEVENT;

		} catch (Exception e) {
			System.out.println("operation failed！");
			return LOADERROR; 
		} finally {
			mysql.close();
		}
	}

	private OnClickListener loadmoreListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.titlebar_refresh) {
				getSpecCatEvent(mEventData);

				mEventlistAdapter = new SimpleAdapter(SaveMainActivity.this,
						mEventData, R.layout.eventlist_item_layout,
						new String[] { "eventlist_item_title",
								"eventlist_item_date",
								"eventlist_item_location" }, new int[] {
								R.id.eventlist_item_title,
								R.id.eventlist_item_date,
								R.id.eventlist_item_location });
				mEventlist = (ListView) findViewById(R.id.event_list);
				mEventlist.setAdapter(mEventlistAdapter);
			} else if (v.getId() == R.id.savemain_titlebar_clear) {
				String dbPath = android.os.Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/database";

				File f = new File(dbPath + "/" + "event.db");

				try {
					
					mysql = SQLiteDatabase.openOrCreateDatabase(f, null);

					// read
					mysql.execSQL("DELETE FROM " + TABLE_NAME + ";");
					Toast.makeText(SaveMainActivity.this, "Save event cleared",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					System.out.println("operation failed！");
				} finally {
					mysql.close();
				}

				getSpecCatEvent(mEventData);

				mEventlistAdapter = new SimpleAdapter(SaveMainActivity.this,
						mEventData, R.layout.eventlist_item_layout,
						new String[] { "eventlist_item_title",
								"eventlist_item_date",
								"eventlist_item_location" }, new int[] {
								R.id.eventlist_item_title,
								R.id.eventlist_item_date,
								R.id.eventlist_item_location });
				mEventlist = (ListView) findViewById(R.id.event_list);
				mEventlist.setAdapter(mEventlistAdapter);
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_main, menu);
		return true;
	}

}
