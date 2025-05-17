package com.example.oneweekenglish.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.oneweekenglish.R;

public class YellowNoticeFragment extends Fragment {
    private ImageButton tryAgainButton, nextButton;
    private ImageView cautionIcon, shareIcon, reportIcon;
    private TextView titleText, tryAgainText;

    private String answer;

    public void setAnswer(String answer) {
        this.answer = answer;
        if (tryAgainText != null) {
            tryAgainText.setText(answer);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yellow_notice, container, false);

        // Khởi tạo các view
        tryAgainButton = view.findViewById(R.id.tryAgainButton);
        nextButton = view.findViewById(R.id.nextButton);
        cautionIcon = view.findViewById(R.id.cautionIcon);
        shareIcon = view.findViewById(R.id.shareIcon);
        reportIcon = view.findViewById(R.id.reportIcon);
        titleText = view.findViewById(R.id.notBadText);
        tryAgainText = view.findViewById(R.id.tryAgainText);
        setAnswer(answer);

        // Xử lý sự kiện nút Try Again
        tryAgainButton.setOnClickListener(v -> {
            if (getActivity() instanceof OnTryAgainListener) {
                ((OnTryAgainListener) getActivity()).onTryAgainClicked();
            }
        });

        // Xử lý sự kiện nút Next
        nextButton.setOnClickListener(v -> {
            if (getActivity() instanceof OnNextListener) {
                ((OnNextListener) getActivity()).onNextClicked();
            }
        });

        // Xử lý sự kiện share icon (nếu cần)
        shareIcon.setOnClickListener(v -> {
            if (getActivity() instanceof OnShareListener) {
                ((OnShareListener) getActivity()).onShareClicked();
            }
        });

        // Xử lý sự kiện report icon (nếu cần)
        reportIcon.setOnClickListener(v -> {
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

    public interface OnNextListener {
        void onNextClicked();
    }

    public interface OnShareListener {
        void onShareClicked();
    }

    public interface OnReportListener {
        void onReportClicked();
    }
}