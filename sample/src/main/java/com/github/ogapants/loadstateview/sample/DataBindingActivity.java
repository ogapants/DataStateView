package com.github.ogapants.loadstateview.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.ogapants.loadstateview.LoadState;
import com.github.ogapants.loadstateview.LoadStateView;
import com.github.ogapants.loadstateview.sample.databinding.ActivityDataBindingBinding;

// TODO: 2017/10/07 more data binding
public class DataBindingActivity extends AppCompatActivity {

    private static final String KEY_TYPE = "TYPE";
    private ListViewModel viewModel;

    public static Intent createIntent(Context context, LoadState loadState) {
        Intent intent = new Intent(context, DataBindingActivity.class);
        intent.putExtra(KEY_TYPE, loadState);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBindingBinding binding = ActivityDataBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ListViewModel();
        binding.setViewModel(viewModel);
        binding.loadStateView.setDataView(binding.listView);
        binding.loadStateView.setOnReloadClickListener(new LoadStateView.OnReloadClickListener() {
            @Override
            public void onReloadClick() {
                viewModel.load(LoadState.DISABLE);
            }
        });
        LoadState loadState = (LoadState) getIntent().getSerializableExtra(KEY_TYPE);

        viewModel.load(loadState);
    }

}