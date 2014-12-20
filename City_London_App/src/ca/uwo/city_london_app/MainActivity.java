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
import android.widget.ImageButton;

public class MainActivity extends Activity {

	ImageButton facility;
	ImageButton event;
	ImageButton saveItem;

	private SQLiteDatabase mysql;

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
			
			mysql = SQLiteDatabase.openOrCreateDatabase(f, null);

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
			mysql.execSQL(str_sql2);
			System.out.println("新建表成功！点击查看数据库查询");

		} catch (Exception e) {
			System.out.println("操作失败！");
		} finally {
			mysql.close();
		}

		facility = (ImageButton) findViewById(R.id.facility);
		event = (ImageButton) findViewById(R.id.event);
		saveItem = (ImageButton) findViewById(R.id.saveItem);

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
