package com.example.oneweekenglish.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneweekenglish.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private final List<String> wordItems;
    private final Map<String, Boolean> selectionState;
    private final Consumer<Integer> onItemClickListener;

    public WordAdapter(List<String> wordItems, Consumer<Integer> onItemClickListener) {
        this.wordItems = wordItems;
        this.onItemClickListener = onItemClickListener;
        this.selectionState = new HashMap<>();
        for (String word : wordItems) {
            selectionState.put(word, false);
        }
    }

    public void setSelected(String word, boolean selected) {
        selectionState.put(word, selected);
        notifyDataSetChanged();
    }

    public boolean isSelected(String word) {
        return Boolean.TRUE.equals(selectionState.getOrDefault(word, false));
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.button_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        String word = wordItems.get(position);
        holder.bind(word, position);
    }

    @Override
    public int getItemCount() {
        return wordItems.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {

        ImageButton button;
        TextView wordText;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.letterButton);
            wordText = itemView.findViewById(R.id.letterText);
        }

        void bind(String word, int position) {
            wordText.setText(word);
            wordText.setClickable(false);
            wordText.setFocusable(false);

            button.setContentDescription(word);

            if (isSelected(word)) {
                button.setBackgroundResource(R.drawable.button_letter_red);
                button.setEnabled(false);
                button.setClickable(false);
                wordText.setTextColor(Color.WHITE);
                button.setAlpha(1.0f);
            } else {
                button.setBackgroundResource(R.drawable.button_letter_grey);
                button.setEnabled(true);
                button.setClickable(true);
                wordText.setTextColor(Color.parseColor("#EF3349"));
                button.setAlpha(1.0f);

                button.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.accept(position);
                    }
                });
            }
        }
    }
}
