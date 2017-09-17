package com.github.ogapants.datastateview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.github.ogapants.datastateview.databinding.ViewDataSateBinding;

public class DataStateView extends FrameLayout {

    private ViewDataSateBinding binding;
    private DataState currentDataState;

    public DataStateView(Context context) {
        this(context, null);
    }

    public DataStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            inflate(getContext(), R.layout.view_data_sate, this);
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.view_data_sate, this, true);
        changeState(DataState.SILENT);
    }

    public void changeState(DataState dataState) {
        currentDataState = dataState;
        switch (dataState) {
            case LOADING:
                setViewVisibility(binding.progress);
                break;
            case EMPTY:
                setViewVisibility(binding.emptyText);
                break;
            case ERROR:
                setViewVisibility(binding.retryButton);
                break;
            case SILENT:
                binding.getRoot().setVisibility(View.GONE);
                break;
        }
    }

    private void setViewVisibility(View visibleView) {
        binding.getRoot().setVisibility(View.VISIBLE);
        binding.retryButton.setVisibility(View.GONE);
        binding.emptyText.setVisibility(View.GONE);
        binding.progress.setVisibility(View.GONE);

        visibleView.setVisibility(View.VISIBLE);
    }

    public void setOnRetryClickListener(final OnRetryClickListener onRetryClickListener) {
        binding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState(DataState.LOADING);
                onRetryClickListener.onRetryClick();
            }
        });
    }

    public interface OnRetryClickListener {
        void onRetryClick();
    }
}
