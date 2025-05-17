package com.example.oneweekenglish.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.oneweekenglish.R;

public class GreenNoticeFragment extends Fragment {
    public interface OnContinueClickListener {
        void onContinueClicked();
    }

    private OnContinueClickListener callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnContinueClickListener) {
            callback = (OnContinueClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnContinueClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.green_notice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton continueButton = view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> {
            if (callback != null) {
                callback.onContinueClicked();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}