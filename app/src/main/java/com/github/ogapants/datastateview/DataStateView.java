package com.github.ogapants.datastateview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DataStateView extends FrameLayout {

    private DataState currentDataState;
    private View progress;
    private View emptyTextView;
    private View retryButton;
    private View contentData;

    public DataStateView(Context context) {
        this(context, null);
    }

    public DataStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        if (isInEditMode()) {
            return;
        }
        changeState(DataState.SILENT);
    }

    protected void initViews() {
        setProgressView(progress);
        TextView emptyTextView = new TextView(getContext());
        emptyTextView.setText("data is empty");
        setEmptyTextView(emptyTextView);
        Button retryButton = new Button(getContext());
        retryButton.setText("retry");
        setRetryButton(retryButton);
    }

    public void changeState(DataState dataState) {
        currentDataState = dataState;
        switch (dataState) {
            case LOADING:
                appear(progress);
                setContentVisibility(View.GONE);
                break;
            case EMPTY:
                appear(emptyTextView);
                setContentVisibility(View.GONE);
                break;
            case ERROR:
                appear(retryButton);
                setContentVisibility(View.GONE);
                break;
            case SILENT:
                setVisibility(View.GONE);
                setContentVisibility(View.VISIBLE);
                break;
        }
    }

    private void appear(View visibleView) {
        setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);

        visibleView.setVisibility(View.VISIBLE);
    }

    public void setProgressView(View progress) {
        removeView(this.progress);
        addView(progress);
        this.progress = progress;
    }

    public void setEmptyTextView(View emptyTextView) {
        removeView(this.emptyTextView);
        addView(emptyTextView);
        this.emptyTextView = emptyTextView;
    }

    public void setEmptyText(String emptyText) {
        if (emptyTextView instanceof TextView) {
            ((TextView) emptyTextView).setText(emptyText);
        }
    }

    public void setRetryButton(View retryButton) {
        removeView(this.retryButton);
        addView(retryButton);
        this.retryButton = retryButton;
    }

    public void setRetryText(String retryText) {
        if (retryButton instanceof TextView) {
            ((TextView) retryButton).setText(retryText);
        }
    }

    public void setOnRetryClickListener(final OnRetryClickListener onRetryClickListener) {
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState(DataState.LOADING);
                onRetryClickListener.onRetryClick();
            }
        });
    }

    private void setContentVisibility(int visibility) {
        if (contentData != null) {
            contentData.setVisibility(visibility);
        }
    }

    public void setContentData(View contentData) {
        this.contentData = contentData;
    }

    public interface OnRetryClickListener {
        void onRetryClick();
    }
}
