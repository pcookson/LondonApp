package ca.uwo.city_london_app.custom;
/*
 * rewrite View, in order to set the color of default category
 */
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import ca.uwo.city_london_app.R;

public class CustomSimpleAdapterFacility extends SimpleAdapter {

	public CustomSimpleAdapterFacility(Context context,List<? extends Map<String, ?>> data, int resource, String[] from,int[] to) {
		super(context, data, resource, from, to);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);

		if(position == 0) {
			TextView categoryTitle = (TextView) v;
			categoryTitle.setBackgroundResource(R.drawable.image_categorybar_item_selected_background);
			categoryTitle.setTextColor(0xFFFFFFFF);
		}
		return v;
	}
	
}