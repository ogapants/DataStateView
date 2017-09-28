package com.github.ogapants.datastateview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DataStateView extends FrameLayout {

    private DataState currentDataState;
    private View progress;
    private View emptyTextView;
    private View reloadButton;
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
        setProgressView(new ProgressBar(getContext()));
        TextView emptyTextView = new TextView(getContext());
        emptyTextView.setText("data is empty");
        setEmptyTextView(emptyTextView);
        Button reloadButton = new Button(getContext());
        reloadButton.setText("reload");
        setReloadButton(reloadButton);
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
                appear(reloadButton);
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
        reloadButton.setVisibility(View.GONE);
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

    public void setReloadButton(View reloadButton) {
        removeView(this.reloadButton);
        addView(reloadButton);
        this.reloadButton = reloadButton;
    }

    public void setReloadText(String reloadText) {
        if (reloadButton instanceof TextView) {
            ((TextView) reloadButton).setText(reloadText);
        }
    }

    public void setOnReloadClickListener(final OnReloadClickListener onReloadClickListener) {
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState(DataState.LOADING);
                onReloadClickListener.onReloadClick();
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

    public DataState getCurrentDataState() {
        return currentDataState;
    }

    public interface OnReloadClickListener {
        void onReloadClick();
    }
}
