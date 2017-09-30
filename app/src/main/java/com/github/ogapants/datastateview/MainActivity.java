package com.github.ogapants.datastateview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import com.github.ogapants.datastateview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private boolean custom;

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
        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                custom = b;
            }
        });
    }

    private void go(LoadState loadState) {
        startActivity(ListActivity.createIntent(this, loadState, custom));
    }
}
