package com.github.ogapants.loadstateview.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.ogapants.loadstateview.LoadState;
import com.github.ogapants.loadstateview.LoadStateView;
import com.github.ogapants.loadstateview.sample.databinding.ActivityDataBindingBinding;

import java.util.List;

// TODO: 2017/10/07 more data binding
public class DataBindingActivity extends AppCompatActivity {

    private static final String KEY_TYPE = "TYPE";
    private ListViewModel viewModel;
    private ActivityDataBindingBinding binding;

    public static Intent createIntent(Context context, LoadState loadState) {
        Intent intent = new Intent(context, DataBindingActivity.class);
        intent.putExtra(KEY_TYPE, loadState);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ListViewModel();
        viewModel.setCallback(new ListViewModel.Callback() {
            @Override
            public void onGetList(List<String> result) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        DataBindingActivity.this, android.R.layout.simple_list_item_1, result);
                binding.listView.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(DataBindingActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
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