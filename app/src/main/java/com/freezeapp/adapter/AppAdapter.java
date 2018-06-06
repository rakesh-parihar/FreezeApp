package com.freezeapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freezeapp.R;
import com.freezeapp.model.AppModel;

import java.util.List;

/**
 * Adapter class for app list
 *
 * @author Rakesh
 * @since 10/11/2017.
 */

public class AppAdapter extends BaseAdapter {
    Context context;
    List<AppModel> rowItems;

    public AppAdapter(Context context, List<AppModel> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        RelativeLayout parent;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppModel rowItem = (AppModel) getItem(position);

        holder.txtDesc.setText(rowItem.getPkg());
        holder.txtTitle.setText(rowItem.getAppname());
        holder.imageView.setImageDrawable(rowItem.getIcon());

        if (rowItem.isDisabled()) {
            holder.parent.setAlpha(.4f);
        } else {
            holder.parent.setAlpha(1.0f);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}

