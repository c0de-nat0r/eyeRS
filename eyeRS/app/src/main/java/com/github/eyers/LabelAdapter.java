package com.github.eyers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public final class LabelAdapter extends ArrayAdapter<ItemLabel> {

    public LabelAdapter(Context context, ArrayList<ItemLabel> stocks) {
        super(context, R.layout.item_label, stocks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemLabel label = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_label, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.lblName);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(label.getName());
        viewHolder.image.setImageBitmap(label.getImage());

        return convertView;
    }

    /**
     * View lookup cache.
     */
    private class ViewHolder {

        TextView name;
        ImageView image;
    }
} //end class LabelAdapter
