package ca.uwo.city_london_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import ca.uwo.city_london_app.Service.SyncHttp;
import ca.uwo.city_london_app.custom.CustomSimpleAdapterFacility;
import ca.uwo.city_london_app.model.Category;
import ca.uwo.city_london_app.util.DensityUtil;

//import ca.uwo.city_london_app.util.StringUtil;

public class FacilityMainActivity extends Activity {

	//private final int COLUMNWIDTH_PX = 180; // width of each GridView
	private final int COLUMNWIDTH_PX = 350; //width of each GridView android
	// 4.3
	private final int FLINGVELOCITY_PX = 1200; // distance of ViewFilper per
												// time
	private final int EVENTCOUNT = 10; 
	private final int SUCCESS = 0; 
	private final int NOEVENT = 1; 
	private final int NOMOREEVENT = 2; 
	private final int LOADERROR = 3; 

	private String mCid; 
	private String mCategoryTitle; 
	private int mFlingVelocity_dip;
	private ListView mFacilitylist; 

	private SimpleAdapter mFacilitylistAdapter; 
	private ArrayList<HashMap<String, Object>> mFacilityData; 
	private LayoutInflater mInflater; 

	private HorizontalScrollView categoryScrollView = null;
	private Button category_Button = null; 

	private Button mTitleBarRefresh; 
	private ProgressBar mTitleBarProgress; 
	private Button mLoadmoreButton; 

	private LoadNewsAsyncTack mLoadNewsAsyncTack; 

	private int mColumnWidth_dip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facility_main);

		/*
		 * Temporary fix: main tread access network is not support in Andorid
		 * version > 4.0
		 */

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		/*
		 * Variable initialization
		 */

		mTitleBarRefresh = (Button) findViewById(R.id.titlebar_refresh);
		mTitleBarProgress = (ProgressBar) findViewById(R.id.titlebar_progress);

		mTitleBarRefresh.setOnClickListener(loadmoreListener);

		mColumnWidth_dip = DensityUtil.px2dip(this, COLUMNWIDTH_PX);
		mFlingVelocity_dip = DensityUtil.px2dip(this, FLINGVELOCITY_PX);

		mFacilityData = new ArrayList<HashMap<String, Object>>();


		// get Array value (array.xml) and store into hashMap categories
		String[] categoryArray = getResources().getStringArray(
				R.array.facilityCategories);

		// Define an array list to store Hash Map object (event category from
		// array.xml)
		final List<HashMap<String, Category>> categories = new ArrayList<HashMap<String, Category>>();
		// Remark: List is interface, which needs to be implemented; ArrayList
		// is a class which can be new.

		// divide the event category string
		// for each category
		for (int i = 0; i < categoryArray.length; i++) {
			// Remark: .length<- number of items in array; .length()<- length of
			// a String
			// split the value into two parts
			String temp[] = categoryArray[i].split("[|]");
			if (temp.length == 2) {
				// first part id
				// int cid = StringUtil.string2Int(temp[0]);
				String cid = temp[0];
				// second part title
				String title = temp[1];
				// data structure Category
				Category type = new Category(cid, title);
				HashMap<String, Category> hashMap = new HashMap<String, Category>();
				hashMap.put("category_title", type);
				categories.add(hashMap);
			}
		}

		mCid = "01";
		mCategoryTitle = "Arenas";

		mInflater = getLayoutInflater();

		// Create category adapter to fill in the category title grip view
		CustomSimpleAdapterFacility categoryAdapter = new CustomSimpleAdapterFacility(
				this, categories, R.layout.category_item_layout,
				new String[] { "category_title" },
				new int[] { R.id.category_title });

		/*
		 * 1. Create a grid view 2. Get category information from strings 3. Add
		 * to the category_layout (LinearLayout)
		 */

		// Create new grid view to contain event categories
		GridView category = new GridView(this);
		// Set background color of each grid to transparent
		category.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// Set width of each event category (Constant)
		category.setColumnWidth(mColumnWidth_dip);
		// Set number of columns
		category.setNumColumns(GridView.AUTO_FIT);
		// Set the position in each grid view
		category.setGravity(Gravity.CENTER);
		// Calculate the with of the of the grid view
		int width = mColumnWidth_dip * categories.size();
		// get params for layout
		LayoutParams params = new LayoutParams(width, LayoutParams.MATCH_PARENT);
		category.setLayoutParams(params);
		// set value for grid view category via adapter
		category.setAdapter(categoryAdapter);
		// add grid view to category layout
		LinearLayout categoryLayout = (LinearLayout) findViewById(R.id.category_layout);
		categoryLayout.addView(category);


		// add grid view listener
		category.setOnItemClickListener(new OnItemClickListener() {
			TextView categoryTitle;

			@SuppressWarnings("deprecation")
			@Override
			// Remark: parms will be gotten while user clicking the item
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				// Initiate all the category title background color
				for (int i = 0; i < parent.getCount(); i++) {
					categoryTitle = (TextView) parent.getChildAt(i);
					categoryTitle.setTextColor(0XFFADB2AD);
					categoryTitle.setBackgroundDrawable(null);
				}

				// Format backgournd color for the chosen category
				categoryTitle = (TextView) view;
				categoryTitle.setTextColor(0xFFFFFFFF);
				categoryTitle
						.setBackgroundResource(R.drawable.image_categorybar_item_selected_background);

				Toast.makeText(FacilityMainActivity.this,
						categoryTitle.getText(), Toast.LENGTH_SHORT).show();

				mCid = categories.get(position).get("category_title").getCid();
				mCategoryTitle = categories.get(position).get("category_title")
						.getTitle();


				mLoadNewsAsyncTack = new LoadNewsAsyncTack();
				mLoadNewsAsyncTack.execute(0, true);
			}
		});

		/*
		 * ======================================================================
		 * =======================================
		 */
		/*
		 * Get event list content from server
		 */


		getSpecCatFacility(mCid, mFacilityData, 0, true);

		categoryScrollView = (HorizontalScrollView) findViewById(R.id.categorybar_scrollView);
		category_Button = (Button) findViewById(R.id.category_arrow_right);
		category_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				categoryScrollView.fling(mFlingVelocity_dip);
			}
		});

		mFacilitylistAdapter = new SimpleAdapter(this, mFacilityData,
				R.layout.facilitylist_item_layout,
				new String[] { "facilitylist_item_title",
						"facilitylist_item_location" }, new int[] {
						R.id.facilitylist_item_title,
						R.id.facilitylist_item_location });
		mFacilitylist = (ListView) findViewById(R.id.facility_list);

		View footerView = mInflater.inflate(R.layout.loadmore_layout, null);
		mFacilitylist.addFooterView(footerView);


		mFacilitylist.setAdapter(mFacilitylistAdapter);

		mFacilitylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FacilityMainActivity.this,
						FacilityDetailsActivity.class);
				intent.putExtra("categoryTitle", mCategoryTitle);
				intent.putExtra("facilityData", mFacilityData);
				intent.putExtra("position", position);
				// System.out.println("postion=" + position);
				startActivity(intent);
			}

		});

		mLoadmoreButton = (Button) findViewById(R.id.loadmore_btn);
		mLoadmoreButton.setOnClickListener(loadmoreListener);

	}


	int getSpecCatFacility(String cid,
			List<HashMap<String, Object>> facilityList, int startid,
			boolean firstTime) {

		if (firstTime) {
			facilityList.clear();
		}


		String url = "http://192.168.0.13:8080/web/getSpecifyCategoryFacility";
		// String params = "fromDate=" + fromDate;
		String params = "cid=" + cid + "&startid=" + startid + "&count="
				+ EVENTCOUNT;
		SyncHttp syncHttp = new SyncHttp();

		try {
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			int retCode = jsonObject.getInt("ret");

			if (retCode == 0) {
				JSONObject dataObj = jsonObject.getJSONObject("data");
				int totalNum = dataObj.getInt("totalnum");
				if (totalNum > 0) {
					JSONArray newslistArray = dataObj.getJSONArray("newslist");
					for (int i = 0; i < newslistArray.length(); i++) {
						JSONObject newsObject = (JSONObject) newslistArray
								.opt(i);
						HashMap<String, Object> hashMap = new HashMap<String, Object>();

						hashMap.put("facilitylist_item_title",
								newsObject.getString("name"));
						hashMap.put("facilitylist_item_location",
								newsObject.getString("address"));

						hashMap.put("category",
								newsObject.getString("category"));
						hashMap.put("xcoord", newsObject.getString("xcoord"));
						hashMap.put("ycoord", newsObject.getString("ycoord"));

						facilityList.add(hashMap);
					}
					return SUCCESS;
				} else {
					if (firstTime) {
						return NOEVENT; 
					} else {
						return NOMOREEVENT; 
					}
				}
			} else {
				return LOADERROR; 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LOADERROR; 
		}
	}

	/*
	 * ==========================================================================
	 * ===============
	 */

	private OnClickListener loadmoreListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mLoadNewsAsyncTack = new LoadNewsAsyncTack();


			if (v.getId() == R.id.loadmore_btn)
				mLoadNewsAsyncTack.execute(mFacilityData.size(), false);
			else if (v.getId() == R.id.titlebar_refresh)
				mLoadNewsAsyncTack.execute(0, true);
		}
	};


	private class LoadNewsAsyncTack extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected void onPreExecute() {
			mTitleBarRefresh.setVisibility(View.GONE);
			mTitleBarProgress.setVisibility(View.VISIBLE);
			mLoadmoreButton.setText(R.string.loadmore_text);
		}

		@Override
		protected Integer doInBackground(Object... params) {
			return getSpecCatFacility(mCid, mFacilityData, (Integer) params[0],
					(Boolean) params[1]);
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case NOEVENT:
				Toast.makeText(FacilityMainActivity.this, R.string.nofacility,
						Toast.LENGTH_SHORT).show();
				break;
			case NOMOREEVENT:
				Toast.makeText(FacilityMainActivity.this,
						R.string.nomorefacility, Toast.LENGTH_SHORT).show();
				break;
			case LOADERROR:
				Toast.makeText(FacilityMainActivity.this,
						R.string.loadfacilityerror, Toast.LENGTH_SHORT).show();
				break;
			}
			mTitleBarRefresh.setVisibility(View.VISIBLE); 
			mTitleBarProgress.setVisibility(View.GONE); 
			mLoadmoreButton.setText(R.string.loadmore_btn); 
			mFacilitylistAdapter.notifyDataSetChanged(); 
		}
	}

	// set the initial location of scroll view.
	public void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				HorizontalScrollView sv = (HorizontalScrollView) findViewById(R.id.categorybar_scrollView);
				int location = Integer.valueOf(mCid) * 30;
				sv.smoothScrollTo(location, 0);
			}
		}, 100);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facility_main, menu);
		return true;
	}

}
