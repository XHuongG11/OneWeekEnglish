package com.example.oneweekenglish.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oneweekenglish.R;
import com.example.oneweekenglish.model.Word;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends BaseAdapter {
    //interface
    public interface OnCardClickListener {
        void onCardClick(int position, Word word, int viewType);
    }
    private OnCardClickListener listener;

    public void setOnCardClickListener(OnCardClickListener listener) {
        this.listener = listener;
    }

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_TEXT = 1;

    private List<Word> wordList;

    public CardAdapter(List<Word> wordList) {
        this.wordList = wordList;
    }

    @Override
    public int getCount() {
        return wordList != null ? wordList.size() * 2 : 0;
    }

    @Override
    public Object getItem(int position) {
        return wordList.get(position / 2);
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

    // Thêm danh sách vị trí đã matched
    private List<Integer> matchedPositions = new ArrayList<>();

    public void setMatched(int pos1, int pos2) {
        if (!matchedPositions.contains(pos1)) matchedPositions.add(pos1);
        if (!matchedPositions.contains(pos2)) matchedPositions.add(pos2);
        notifyDataSetChanged();
    }

    public boolean isMatched(int position) {
        return matchedPositions.contains(position);
    }
    public int getMatchedCount() {
        return matchedPositions.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Word word = wordList.get(position / 2);

        if (convertView == null) {
            if (viewType == TYPE_IMAGE) {
                convertView = inflater.inflate(R.layout.card_image, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.card_text, parent, false);
            }
        }

        // Nếu đã matched thì ẩn view đi
        if (isMatched(position)) {
            convertView.setVisibility(View.INVISIBLE);
            convertView.setOnClickListener(null); // bỏ listener để không xử lý click nữa
            return convertView;
        } else {
            convertView.setVisibility(View.VISIBLE);
        }

        if (viewType == TYPE_IMAGE) {
            ImageView imageView = convertView.findViewById(R.id.imageViewCardImage);
            Glide.with(parent.getContext())
                    .load(word.getImageUrl())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(imageView);
        } else {
            TextView textView = convertView.findViewById(R.id.textViewCardText);
            textView.setText(word.getContent());
        }

        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCardClick(position, word, viewType);
            }
        });
        if (matchedPositions.contains(position)) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }

        if (position == selectedPosition) {
            convertView.setBackgroundResource(R.drawable.card_selected_border);
        } else {
            convertView.setBackgroundResource(R.drawable.card_default_border);
        }

        return convertView;
    }
    private int selectedPosition = -1;
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

}
