package com.example.oneweekenglish.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.oneweekenglish.R;

public class CardAdapter extends BaseAdapter {

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TOTAL_ITEMS = 6;

    @Override
    public int getCount() {
        return TOTAL_ITEMS;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_IMAGE : TYPE_TEXT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (convertView == null) {
            if (viewType == TYPE_IMAGE) {
                convertView = inflater.inflate(R.layout.card_image, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.card_text, parent, false);
            }
        }

        return convertView;
    }
}
