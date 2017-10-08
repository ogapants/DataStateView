package com.github.ogapants.loadstateview.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;

import com.github.ogapants.loadstateview.LoadState;
import com.github.ogapants.loadstateview.LoadStateView;

import java.util.List;

public class ListViewModel extends BaseObservable {

    private static final String TAG = DataBindingActivity.class.getSimpleName();

    private LoadState loadState;

    public ListViewModel() {
    }

    @BindingAdapter("loadState")
    public static void updateState(LoadStateView loadStateView, LoadState loadState) {
        loadStateView.updateState(loadState);
    }

    @Bindable
    public LoadState getLoadState() {
        return loadState;
    }

    private void setLoadState(LoadState loadState) {
        this.loadState = loadState;
        notifyPropertyChanged(BR.loadState);
    }

    public void load(LoadState expectStatus) {
        setLoadState(LoadState.LOADING);
        new SampleApi().load(expectStatus, new SampleApi.Callback() {
            @Override
            public void onSuccess(List<String> result) {
                if (result.isEmpty()) {
                    setLoadState(LoadState.LOADED_EMPTY);
                    return;
                }
                setLoadState(LoadState.DISABLE);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                        DataBindingActivity.this, android.R.layout.simple_list_item_1, result);
//                binding.listView.setAdapter(adapter);
            }

            @Override
            public void onError() {
                Log.e(TAG, "onError: ");
                setLoadState(LoadState.ERROR);
//                Toast.makeText(DataBindingActivity.this, "error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
