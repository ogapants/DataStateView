package com.github.ogapants.loadstateview;

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
    private View emptyView;
    private View reloadView;
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
        ProgressBar progress = new ProgressBar(getContext());
        TextView emptyView = new TextView(getContext());
        emptyView.setText("data is empty");
        Button reloadView = new Button(getContext());
        reloadView.setText("reload");
        initViews(progress, emptyView, reloadView);
    }

    public void initViews(View progressView, View emptyView, View reloadView) {
        setProgressView(progressView);
        setEmptyView(emptyView);
        setReloadView(reloadView);
    }

    public void updateState(LoadState loadState) {
        currentLoadState = loadState;
        switch (loadState) {
            case LOADING:
                appear(progress);
                break;
            case LOADED_EMPTY:
                appear(emptyView);
                break;
            case ERROR:
                appear(reloadView);
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
        reloadView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
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

    public void setEmptyView(View emptyTextView) {
        removeView(this.emptyView);
        addView(emptyTextView);
        this.emptyView = emptyTextView;
    }

    public void setEmptyView(@LayoutRes int resId) {
        setEmptyView(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setEmptyText(String emptyText) {
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void setReloadView(View reloadButton) {
        removeView(this.reloadView);
        addView(reloadButton);
        this.reloadView = reloadButton;
    }

    public void setReloadView(@LayoutRes int resId) {
        setReloadView(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setReloadText(String reloadText) {
        if (reloadView instanceof TextView) {
            ((TextView) reloadView).setText(reloadText);
        }
    }

    public void setOnReloadClickListener(final OnReloadClickListener onReloadClickListener) {
        reloadView.setOnClickListener(new OnClickListener() {
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

    public void setDataView(View dataView) {
        this.dataView = dataView;
    }

    public LoadState getCurrentLoadState() {
        return currentLoadState;
    }

    public interface OnReloadClickListener {
        void onReloadClick();
    }
}
