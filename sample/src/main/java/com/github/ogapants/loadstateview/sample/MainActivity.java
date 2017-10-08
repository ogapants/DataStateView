package com.github.ogapants.loadstateview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import com.github.ogapants.loadstateview.LoadState;
import com.github.ogapants.loadstateview.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private boolean custom;
    private boolean otherSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(LoadState.DISABLE);
            }
        });
        binding.empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(LoadState.LOADED_EMPTY);
            }
        });
        binding.error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(LoadState.ERROR);
            }
        });
        binding.customCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                custom = b;
            }
        });
        binding.otherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                otherSample = b;
            }
        });
    }

    private void go(LoadState loadState) {
        if (otherSample) {
            startActivity(DataBindingActivity.createIntent(this, loadState));
        } else {
            startActivity(ListActivity.createIntent(this, loadState, custom));
        }
    }
}
