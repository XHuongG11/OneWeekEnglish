package com.example.oneweekenglish.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;

public class CardAdapter extends BaseAdapter {

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TOTAL_ITEMS = 10;

    // Giả sử bạn có một mảng chứa URL hình ảnh
    private String[] imageUrls = {
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJxo2NFiYcR35GzCk5T3nxA7rGlSsXvIfJwg&s",
            // Thêm các link hình ảnh tiếp theo...
    };

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

        if (viewType == TYPE_IMAGE) {
            ImageView imageView = convertView.findViewById(R.id.imageViewCardImage);
            if (position < imageUrls.length) {
                Glide.with(parent.getContext())
                        .load(imageUrls[position])
                        .placeholder(R.drawable.image_card) // optional
                        .error(R.drawable.image_card) // optional
                        .into(imageView);
            }
        } else {
            TextView textView = convertView.findViewById(R.id.textViewCardText);
            textView.setText("Text item " + position);
        }

        return convertView;
    }
}
