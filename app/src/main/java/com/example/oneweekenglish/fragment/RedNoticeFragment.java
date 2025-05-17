package com.example.oneweekenglish.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.oneweekenglish.R;

public class RedNoticeFragment extends Fragment {
    private ImageButton tryAgainButton;
    private ImageView shareIcon, reportIcon;
    private TextView textViewTryAgain;

    private String answer;

    public void setAnswer(String answer) {
        this.answer = answer;
        if (textViewTryAgain != null) {
            textViewTryAgain.setText(answer);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.red_notice, container, false);

        tryAgainButton = view.findViewById(R.id.tryAgainButton);
        shareIcon = view.findViewById(R.id.shareIcon);
        reportIcon = view.findViewById(R.id.reportIcon);
        textViewTryAgain = view.findViewById(R.id.tryAgainText);
        setAnswer(answer);
        tryAgainButton.setOnClickListener(v -> {
            // Xử lý sự kiện nút CONTINUE được bấm
            // Ví dụ gọi hàm trong Activity:
            if (getActivity() instanceof OnTryAgainListener) {
                ((OnTryAgainListener) getActivity()).onTryAgainClicked();
            }
        });

        shareIcon.setOnClickListener(v -> {
            // Xử lý sự kiện share icon bấm
            if (getActivity() instanceof OnShareListener) {
                ((OnShareListener) getActivity()).onShareClicked();
            }
        });

        reportIcon.setOnClickListener(v -> {
            // Xử lý sự kiện report icon bấm
            if (getActivity() instanceof OnReportListener) {
                ((OnReportListener) getActivity()).onReportClicked();
            }
        });

        return view;
    }

    // Định nghĩa interface để Activity implement
    public interface OnTryAgainListener {
        void onTryAgainClicked();
    }
    public interface OnShareListener {
        void onShareClicked();
    }
    public interface OnReportListener {
        void onReportClicked();
    }
}