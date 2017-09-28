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
    private final List<String> items = Arrays.asList("a", "b", "c", "d");
    private ListView listView;
    private DataStateView dataStateView;

    public static Intent createIntetnt(Context context, DataState dataState, boolean custom) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(KEY_TYPE, dataState);
        intent.putExtra(KEY_CUSTOM, custom);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = findViewById(R.id.list_view);
        dataStateView = findViewById(R.id.dataStateView);
        dataStateView.setContentData(listView);
        dataStateView.setOnReloadClickListener(new DataStateView.OnReloadClickListener() {
            @Override
            public void onReloadClick() {
                load(DataState.SILENT);
            }
        });
        DataState dataState = (DataState) getIntent().getSerializableExtra(KEY_TYPE);
        boolean customized = getIntent().getBooleanExtra(KEY_CUSTOM, false);
        if (customized) {
            setCustomView();
        }

        load(dataState);
    }

    private void setCustomView() {
        // TODO: 2017/09/18 hmm....
        Button button = new Button(this);
        button.setText("Now empty!");
        dataStateView.setEmptyTextView(button);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(android.R.drawable.ic_delete);
        dataStateView.setReloadButton(imageView);
        TextView textView = new TextView(this);
        textView.setText("loading...");
        dataStateView.setProgressView(textView);
    }

    private void load(DataState dataState) {
        dataStateView.changeState(DataState.LOADING);
        loadDummy(dataState, new Callback() {
            @Override
            public void onSuccess(List<String> result) {
                if (result.isEmpty()) {
                    dataStateView.changeState(DataState.EMPTY);
                    return;
                }
                dataStateView.changeState(DataState.SILENT);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, result);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError() {
                Log.e(TAG, "onError: ");
                dataStateView.changeState(DataState.ERROR);
                Toast.makeText(ListActivity.this, "error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDummy(final DataState silent, final Callback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (silent) {
                    case EMPTY:
                        callback.onSuccess(Collections.<String>emptyList());
                        break;
                    case ERROR:
                        callback.onError();
                        break;
                    case SILENT:
                        callback.onSuccess(items);
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