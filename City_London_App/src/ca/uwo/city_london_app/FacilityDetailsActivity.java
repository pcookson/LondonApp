package ca.uwo.city_london_app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import ca.uwo.city_london_app.Service.SyncHttp;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilityDetailsActivity extends Activity {

	private ArrayList<HashMap<String, Object>> mFacilityData;
	private int mPosition = 0; 
	private String facilityName;
	private String facilityLocation;
	private String facilityCategory;
	private String facilityXcoordStr;
	private String facilityYcoordStr;
	private double facilityXcoord = 0.0;
	private double facilityYcoord = 0.0;
	private final int FINISH = 0; 
	private int busCount = 0; 
	GoogleMap mMap;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub


			switch (msg.arg1) {
			case FINISH:
				ArrayList<HashMap<String, Object>> busList = (ArrayList<HashMap<String, Object>>) msg.obj;
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				if (busList.size() > 0) {

					if (busList.size() < 6)
						busCount = busList.size();
					else
						busCount = 6;

					for (int i = 0; i < busCount; i++) {
						hashMap = busList.get(i);
						String stopNumber = (String) hashMap.get("stop_number");
						String stopName = (String) hashMap.get("stop_name");
						String stopBus = (String) hashMap.get("bus");
						double stopXcoord = (Double) hashMap.get("stop_xcoord");
						double stopYcoord = (Double) hashMap.get("stop_ycoord");
	

						// add marker
						MarkerOptions markerOpt = new MarkerOptions();
						markerOpt.position(new LatLng(stopYcoord, stopXcoord));
						markerOpt.title("Stop "
								+ stopNumber.replaceAll(".0", "") + ": "
								+ stopName);
						markerOpt.snippet("Bus: "
								+ stopBus.replaceAll(".0", ""));
						markerOpt
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
						mMap.addMarker(markerOpt);

					}
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facility_details);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		String categoryName = bundle.getString("categoryTitle");
		TextView titleBarTitle = (TextView) findViewById(R.id.facilitydetail_titlebar_title);
		titleBarTitle.setText(categoryName);

		mPosition = bundle.getInt("position");

		Serializable serializable = bundle.getSerializable("facilityData");
		mFacilityData = (ArrayList<HashMap<String, Object>>) serializable;

		HashMap<String, Object> hashMap = mFacilityData.get(mPosition);

		facilityName = (String) hashMap.get("facilitylist_item_title");
		facilityLocation = (String) hashMap.get("facilitylist_item_location");
		facilityCategory = (String) hashMap.get("category");
		facilityXcoordStr = (String) hashMap.get("xcoord");
		facilityYcoordStr = (String) hashMap.get("ycoord");

		facilityXcoord = Double.parseDouble(facilityXcoordStr);
		facilityYcoord = Double.parseDouble(facilityYcoordStr);


		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		LatLng nkut = new LatLng(facilityYcoord, facilityXcoord);
		mMap.moveCamera(CameraUpdateFactory.newLatLng(nkut));

		MarkerOptions markerOpt = new MarkerOptions();
		markerOpt.position(new LatLng(facilityYcoord, facilityXcoord));
		markerOpt.title(facilityName);
		markerOpt.snippet(facilityLocation);
		markerOpt.draggable(true);
		markerOpt.visible(true);

		mMap.addMarker(markerOpt);

		new GetBusThread().start();

	}


	private class GetBusThread extends Thread {
		@Override
		public void run() {

			// TODO Auto-generated method stub
			ArrayList<HashMap<String, Object>> busStr = getBusInfo();
			Message msg = mHandler.obtainMessage(); 
			msg.arg1 = FINISH;
			msg.obj = busStr;
			mHandler.sendMessage(msg); 
		}
	}


	private ArrayList<HashMap<String, Object>> getBusInfo() {
		ArrayList<HashMap<String, Object>> busList = new ArrayList<HashMap<String, Object>>();
		SyncHttp syncHttp = new SyncHttp();

		String url = "http://10.0.2.2:8080/web/getBus";

		String params = "facilityx=" + facilityXcoordStr + "&facilityy="
				+ facilityYcoordStr;
		try {
			String retString = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retString);
			int retCode = jsonObject.getInt("ret");
			if (retCode == 0) {
				JSONObject dataObj = jsonObject.getJSONObject("data");
				int totalNum = dataObj.getInt("totalnum");
				if (totalNum > 0) {
					JSONArray buslistArray = dataObj.getJSONArray("newslist");
					for (int i = 0; i < buslistArray.length(); i++) {
						JSONObject busObject = (JSONObject) buslistArray.opt(i);
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						// get info from JSON
						hashMap.put("stop_number",
								busObject.getString("stopNumber"));
						hashMap.put("stop_name",
								busObject.getString("stopName"));
						hashMap.put("bus", busObject.getString("bus"));
						hashMap.put("stop_xcoord",
								busObject.getDouble("stopXcoord"));
						hashMap.put("stop_ycoord",
								busObject.getDouble("stopYcoord"));

						busList.add(hashMap);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return busList;
	}
}
