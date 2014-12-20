package ca.uwo.city_london_app.custom;
/*
 * rewrite View, in order to set the color of current month
 */
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import ca.uwo.city_london_app.R;

public class CustomSimpleAdapter extends SimpleAdapter {

	public CustomSimpleAdapter(Context context,List<? extends Map<String, ?>> data, int resource, String[] from,int[] to) {
		super(context, data, resource, from, to);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		Calendar c = Calendar.getInstance();

		if(position == c.get(Calendar.MONTH)) {
			TextView categoryTitle = (TextView) v;
			categoryTitle.setBackgroundResource(R.drawable.image_categorybar_item_selected_background);
			categoryTitle.setTextColor(0xFFFFFFFF);
		}
		return v;
	}
	
}
