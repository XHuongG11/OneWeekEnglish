package com.example.oneweekenglish.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.model.LetterItem;

import java.util.List;

public class LetterAdapter extends BaseAdapter {

    private List<LetterItem> letterItems;

    public LetterAdapter(List<LetterItem> letterItems) {
        this.letterItems = letterItems;
    }

    @Override
    public int getCount() {
        return letterItems.size();
    }

    @Override
    public Object getItem(int position) {
        return letterItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LetterItem item = letterItems.get(position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.button_letter, parent, false);
        }

        ImageButton button = convertView.findViewById(R.id.letterButton);
        TextView letterText = convertView.findViewById(R.id.letterText);

        if (button != null && letterText != null) {
            letterText.setText(item.getLetter());
        }

        return convertView;
    }
}