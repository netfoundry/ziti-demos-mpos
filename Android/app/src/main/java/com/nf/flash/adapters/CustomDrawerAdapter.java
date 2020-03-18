package com.nf.flash.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nf.flash.R;
import com.nf.flash.activities.HelpActivity;
import com.nf.flash.activities.HomeActivity;
import com.nf.flash.activities.SaleTransactionActivity;
import com.nf.flash.models.DrawerItem;
import java.util.List;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

	private Context context;
	private List<DrawerItem> drawerItemList;
	private int layoutResourceID;
	private LayoutInflater inflater;

	public CustomDrawerAdapter(Context context, int layoutResourceID, List<DrawerItem> listItems, String activityName) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResourceID = layoutResourceID;
		this.inflater = ((Activity) context).getLayoutInflater();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DrawerItemHolder drawerHolder= new DrawerItemHolder();
		convertView = inflater.inflate(layoutResourceID, parent, false);
		drawerHolder.ItemName = convertView .findViewById(R.id.drawer_itemName);
		drawerHolder.icon = convertView.findViewById(R.id.drawer_icon);
		drawerHolder.mainLayout = convertView.findViewById(R.id.drawer_layout_main);

		convertView.setTag(drawerHolder);

		DrawerItem dItem = this.drawerItemList.get(position);
		drawerHolder.icon.setImageDrawable(convertView.getResources().getDrawable( dItem.getImgResID()));
		drawerHolder.ItemName.setText(dItem.getItemName());
		if(context instanceof HomeActivity && position == 0) {
			drawerHolder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
			drawerHolder.ItemName.setTextColor(context.getResources().getColor(R.color.flash_lime));
		} else if(( context instanceof SaleTransactionActivity) && position == 1) {
			drawerHolder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
			drawerHolder.ItemName.setTextColor(context.getResources().getColor(R.color.flash_lime));
		}else if(context instanceof HelpActivity && position == 2) {
			drawerHolder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.dark_grey));
			drawerHolder.ItemName.setTextColor(context.getResources().getColor(R.color.flash_lime));
		}
        else {
			drawerHolder.ItemName.setTextColor(context.getResources().getColor(R.color.dark_green));
		}
		return convertView;
	}

	private static class DrawerItemHolder {
		TextView ItemName;
		ImageView icon;
		RelativeLayout mainLayout;
	}
}