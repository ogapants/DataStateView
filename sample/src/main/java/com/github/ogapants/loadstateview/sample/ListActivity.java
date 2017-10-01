package com.github.ogapants.loadstateview.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ogapants.loadstateview.LoadState;
import com.github.ogapants.loadstateview.LoadStateView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = ListActivity.class.getSimpleName();
    private static final String KEY_TYPE = "TYPE";
    private static final String KEY_CUSTOM = "CUSTOM";
    private ListView listView;
    private LoadStateView loadStateView;

    public static Intent createIntent(Context context, LoadState loadState, boolean custom) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(KEY_TYPE, loadState);
        intent.putExtra(KEY_CUSTOM, custom);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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
        if (getIntent().getBooleanExtra(KEY_CUSTOM, false)) setCustomView();

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
                        ListActivity.this, android.R.layout.simple_list_item_1, result);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError() {
                Log.e(TAG, "onError: ");
                loadStateView.updateState(LoadState.ERROR);
                Toast.makeText(ListActivity.this, "error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCustomView() {
        // TODO: 2017/09/18 hmm....
        Button button = new Button(this);
        button.setText("Now empty!");
        loadStateView.setEmptyView(button);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(android.R.drawable.ic_delete);
        loadStateView.setReloadView(imageView);
        TextView textView = new TextView(this);
        textView.setText("loading...");
        loadStateView.setProgressView(textView);
    }
}