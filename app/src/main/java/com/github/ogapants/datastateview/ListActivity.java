package com.github.ogapants.datastateview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
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
        loadStateView.with(listView);
        loadStateView.setOnReloadClickListener(new LoadStateView.OnReloadClickListener() {
            @Override
            public void onReloadClick() {
                load(LoadState.DISABLE);
            }
        });
        LoadState loadState = (LoadState) getIntent().getSerializableExtra(KEY_TYPE);
        if (getIntent().getBooleanExtra(KEY_CUSTOM, false)) {
            setCustomView();
        }

        load(loadState);
    }

    private void setCustomView() {
        // TODO: 2017/09/18 hmm....
        Button button = new Button(this);
        button.setText("Now empty!");
        loadStateView.setEmptyTextView(button);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(android.R.drawable.ic_delete);
        loadStateView.setReloadButton(imageView);
        TextView textView = new TextView(this);
        textView.setText("loading...");
        loadStateView.setProgressView(textView);
    }

    private void load(LoadState loadState) {
        loadStateView.updateState(LoadState.LOADING);
        loadDummy(loadState, new Callback() {
            @Override
            public void onSuccess(List<String> result) {
                if (result.isEmpty()) {
                    loadStateView.updateState(LoadState.LOADED_EMPTY);
                    return;
                }
                loadStateView.updateState(LoadState.DISABLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, result);
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

    private void loadDummy(final LoadState loadState, final Callback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (loadState) {
                    case LOADED_EMPTY:
                        callback.onSuccess(Collections.<String>emptyList());
                        break;
                    case ERROR:
                        callback.onError();
                        break;
                    case DISABLE:
                        callback.onSuccess(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i"));
                        break;
                    case LOADING:
                    default:
                        //nop
                }
            }
        }, 1000);
    }

    private interface Callback {
        void onSuccess(List<String> result);

        void onError();
    }
}