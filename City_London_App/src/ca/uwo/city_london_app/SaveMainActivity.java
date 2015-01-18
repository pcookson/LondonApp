package ca.uwo.city_london_app;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
	private SQLiteDatabase sql_lite;
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
			sql_lite = SQLiteDatabase.openOrCreateDatabase(f, null);

			Cursor cur = sql_lite.rawQuery("SELECT * FROM " + TABLE_NAME, null);
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
					String eventDate = null;

					String yearFrom = ((String) hashMap.get("eventlist_item_fromdate")).substring(0, 4);
					String monthFrom = ((String) hashMap.get("eventlist_item_fromdate")).substring(4, 6);
					String dayFrom =((String) hashMap.get("eventlist_item_fromdate")).substring(6);
					
					
					/*Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(Integer.valueOf(yearFrom), Integer.valueOf(monthFrom) - 1, Integer.valueOf(dayFrom));
					
					Intent intent = new Intent(Intent.ACTION_EDIT);
					intent.setType("vnd.android.cursor.item/event");
					intent.putExtra("beginTime", cal.getTimeInMillis());
					intent.putExtra("allDay", true);
					//intent.putExtra("rrule", "FREQ=YEARLY");
					//intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
					intent.putExtra("title", (String)hashMap.get("eventlist_item_title"));
					startActivity(intent);*/
					
					String yearTo = null;


					if(((String) hashMap.get("eventlist_item_todate")).substring(4, 8).equals("null")){
						yearTo = ((String) hashMap.get("eventlist_item_todate")).substring(0, 4);
						eventDate = cur.getString(2);
					}else{
						yearTo = ((String) hashMap.get("eventlist_item_todate")).substring(0, 4);
						eventDate = "From Date:" + cur.getString(2)
								+ " To Date: " + cur.getString(3);
					}


					hashMap.put("eventlist_item_date", eventDate);
					eventList.add(hashMap);
				}
				return SUCCESS;
			} else
				return NOEVENT;

		} catch (Exception e) {
			System.out.println("operation failed");
			return LOADERROR; 
		} finally {
			sql_lite.close();
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

					sql_lite = SQLiteDatabase.openOrCreateDatabase(f, null);

					// read
					sql_lite.execSQL("DELETE FROM " + TABLE_NAME + ";");
					Toast.makeText(SaveMainActivity.this, "Saved events cleared",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					System.out.println("operation failed");
				} finally {
					sql_lite.close();
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
