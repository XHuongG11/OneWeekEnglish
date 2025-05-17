package com.example.oneweekenglish.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.model.LetterItem;

import java.util.List;
import java.util.function.Consumer;

public class LetterAdapter extends BaseAdapter {

    private List<LetterItem> letterItems;
    private Consumer<Integer> onItemClickListener;

    public LetterAdapter(List<LetterItem> letterItems, Consumer<Integer> onItemClickListener) {
        this.letterItems = letterItems;
        this.onItemClickListener = onItemClickListener;
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
            letterText.setClickable(false);
            letterText.setFocusable(false);

            // Configure ImageButton
            button.setContentDescription(item.getLetter()); // For accessibility
            if (item.isSelected()) {
                button.setBackgroundResource(R.drawable.button_letter_red);
                button.setEnabled(false);
                button.setClickable(false);
                letterText.setTextColor(Color.WHITE);
            } else {
                button.setAlpha(1.0f);
                button.setBackgroundResource(R.drawable.button_letter_grey);
                button.setEnabled(true);
                button.setClickable(true);
                letterText.setTextColor(Color.parseColor("#EF3349"));
                // Set fallback click listener
                button.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.accept(position);
                    }
                });
            }
        }

        return convertView;
    }
}