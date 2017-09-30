package com.github.ogapants.datastateview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadStateView extends FrameLayout {

    private LoadState currentLoadState;
    private View progress;
    private View emptyTextView;
    private View reloadButton;
    private View dataView;

    public LoadStateView(Context context) {
        this(context, null);
    }

    public LoadStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        if (isInEditMode()) {
            return;
        }
        updateState(LoadState.DISABLE);
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

    public void updateState(LoadState loadState) {
        currentLoadState = loadState;
        switch (loadState) {
            case LOADING:
                appear(progress);
                break;
            case LOADED_EMPTY:
                appear(emptyTextView);
                break;
            case ERROR:
                appear(reloadButton);
                break;
            case DISABLE:
                setVisibility(View.GONE);
                setDataVisibility(View.VISIBLE);
                break;
        }
    }

    private void appear(View visibleView) {
        setDataVisibility(View.GONE);

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

    public void setProgressView(@LayoutRes int resId) {
        setProgressView(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setEmptyTextView(View emptyTextView) {
        removeView(this.emptyTextView);
        addView(emptyTextView);
        this.emptyTextView = emptyTextView;
    }

    public void setEmptyTextView(@LayoutRes int resId) {
        setEmptyTextView(LayoutInflater.from(getContext()).inflate(resId, null));
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

    public void setReloadButton(@LayoutRes int resId) {
        setReloadButton(LayoutInflater.from(getContext()).inflate(resId, null));
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
                updateState(LoadState.LOADING);
                onReloadClickListener.onReloadClick();
            }
        });
    }

    private void setDataVisibility(int visibility) {
        if (dataView != null) {
            dataView.setVisibility(visibility);
        }
    }

    public void with(View contentData) {
        this.dataView = contentData;
    }

    public LoadState getCurrentLoadState() {
        return currentLoadState;
    }

    public interface OnReloadClickListener {
        void onReloadClick();
    }
}
