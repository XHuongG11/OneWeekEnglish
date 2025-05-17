package com.example.oneweekenglish.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.model.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WordAdapter extends BaseAdapter {

    private final List<Word> wordItems;
    private final Map<Word, Boolean> selectionState;
    private final Consumer<Integer> onItemClickListener;

    public WordAdapter(List<Word> wordItems, Consumer<Integer> onItemClickListener) {
        this.wordItems = wordItems;
        this.onItemClickListener = onItemClickListener;
        this.selectionState = new HashMap<>();
        // Initialize selection state for all words as false
        for (Word word : wordItems) {
            selectionState.put(word, false);
        }
    }

    // Method to update selection state
    public void setSelected(Word word, boolean selected) {
        selectionState.put(word, selected);
        notifyDataSetChanged();
    }

    // Method to check selection state
    public boolean isSelected(Word word) {
        return selectionState.getOrDefault(word, false);
    }

    @Override
    public int getCount() {
        return wordItems.size();
    }

    @Override
    public Object getItem(int position) {
        return wordItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Word item = wordItems.get(position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.button_word, parent, false);
        }

        ImageButton button = convertView.findViewById(R.id.letterButton);
        TextView wordText = convertView.findViewById(R.id.letterText);

        if (button != null && wordText != null) {
            wordText.setText(item.getContent());
            wordText.setClickable(false);
            wordText.setFocusable(false);

            // Configure ImageButton
            button.setContentDescription(item.getContent()); // For accessibility
            if (isSelected(item)) {
                button.setBackgroundResource(R.drawable.button_letter_red);
                button.setEnabled(false);
                button.setClickable(false);
                wordText.setTextColor(Color.WHITE);
            } else {
                button.setAlpha(1.0f);
                button.setBackgroundResource(R.drawable.button_letter_grey);
                button.setEnabled(true);
                button.setClickable(true);
                wordText.setTextColor(Color.parseColor("#EF3349"));
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