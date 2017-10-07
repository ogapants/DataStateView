package com.github.ogapants.loadstateview.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.ogapants.loadstateview.LoadState;
import com.github.ogapants.loadstateview.LoadStateView;

import java.util.List;

// TODO: 2017/10/07 more data binding
public class DataBindingActivity extends AppCompatActivity {

    private static final String TAG = DataBindingActivity.class.getSimpleName();
    private static final String KEY_TYPE = "TYPE";
    private ListView listView;
    private LoadStateView loadStateView;

    public static Intent createIntent(Context context, LoadState loadState) {
        Intent intent = new Intent(context, DataBindingActivity.class);
        intent.putExtra(KEY_TYPE, loadState);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_binding);
        listView = findViewById(R.id.list_view);
        loadStateView = findViewById(R.id.dataStateView);
        loadStateView.setDataView(listView);
        loadStateView.setOnReloadClickListener(new LoadStateView.OnReloadClickListener() {
            @Override
            public void onReloadClick() {
                load(LoadState.DISABLE);
            }
        });
        LoadState loadState = (LoadState) getIntent().getSerializableExtra(KEY_TYPE);

        load(loadState);
    }

    private void load(LoadState expectStatus) {
        loadStateView.updateState(LoadState.LOADING);
        new SampleApi().load(expectStatus, new SampleApi.Callback() {
            @Override
            public void onSuccess(List<String> result) {
                if (result.isEmpty()) {
                    loadStateView.updateState(LoadState.LOADED_EMPTY);
                    return;
                }
                loadStateView.updateState(LoadState.DISABLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        DataBindingActivity.this, android.R.layout.simple_list_item_1, result);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError() {
                Log.e(TAG, "onError: ");
                loadStateView.updateState(LoadState.ERROR);
                Toast.makeText(DataBindingActivity.this, "error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}