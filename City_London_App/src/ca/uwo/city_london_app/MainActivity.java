package ca.uwo.city_london_app;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button facility;
	Button event;
	Button saveItem;

	private SQLiteDatabase sql_lite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String dbPath = android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/database";
		File path = new File(dbPath);
		File f = new File(dbPath + "/" + "event.db");

		if (!path.exists()) {
			path.mkdirs();
		}

		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			
			sql_lite = SQLiteDatabase.openOrCreateDatabase(f, null);

			String TABLE_NAME = "saved_event";
			String TITLE = "title";
			String URLTITLE = "urltitle";
			String FROMDATE = "fromdate";
			String TODATE = "todate";
			String PHONE = "phone";
			String ADDRESS = "address";
			String DESCRIPTION = "description";

			String str_sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
					+ TITLE + "," + URLTITLE + "," + FROMDATE + "," + TODATE
					+ "," + ADDRESS + "," + PHONE + "," + DESCRIPTION + ","
					+ "PRIMARY KEY (" + URLTITLE + ")" + ");";
			sql_lite.execSQL(str_sql2);
			System.out.println("sql lite database created for saved events");

		} catch (Exception e) {
			System.out.println("An exception creating sql lite database has occurred");
		} finally {
			sql_lite.close();
		}

		facility = (Button) findViewById(R.id.facility);
		event = (Button) findViewById(R.id.event);
		saveItem = (Button) findViewById(R.id.saveItem);

		EventButtonListener eventyButtonListener = new EventButtonListener();
		event.setOnClickListener(eventyButtonListener);

		FacilityButtonListener facilityButtonListener = new FacilityButtonListener();
		facility.setOnClickListener(facilityButtonListener);

		SaveButtonListener saveButtonListener = new SaveButtonListener();
		saveItem.setOnClickListener(saveButtonListener);
	}

	class EventButtonListener implements OnClickListener {

		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setClass(MainActivity.this, EventMainActivity.class);
			startActivity(intent);
		}
	}

	class FacilityButtonListener implements OnClickListener {

		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setClass(MainActivity.this, FacilityMainActivity.class);
			startActivity(intent);
		}
	}

	class SaveButtonListener implements OnClickListener {

		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SaveMainActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
