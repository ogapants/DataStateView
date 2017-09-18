package com.github.ogapants.datastateview;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ogapants.datastateview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadOk();
            }
        });
        binding.empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEmpty();
            }
        });
        binding.error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadError();
            }
        });
        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) setCustomView();
                else reloadActivity();
            }
        });
        binding.dataStateView.setContentData(binding.data);
        binding.dataStateView.setOnRetryClickListener(new DataStateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                loadOk();
            }
        });
    }

    private void setCustomView() {
        // TODO: 2017/09/18 hmm....
        Button button = new Button(this);
        button.setText("Now empty!");
        binding.dataStateView.setEmptyTextView(button);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(android.R.drawable.ic_delete);
        binding.dataStateView.setRetryButton(imageView);
        TextView textView = new TextView(this);
        textView.setText("loading...");
        binding.dataStateView.setProgressView(textView);
    }

    private void reloadActivity() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void loadOk() {
        binding.dataStateView.changeState(DataState.LOADING);
        load(new Runnable() {
            @Override
            public void run() {
                binding.dataStateView.changeState(DataState.SILENT);
                binding.data.setText("load success");
            }
        });
    }

    private void loadEmpty() {
        binding.dataStateView.changeState(DataState.LOADING);
        load(new Runnable() {
            @Override
            public void run() {
                binding.dataStateView.changeState(DataState.EMPTY);
            }
        });
    }

    private void loadError() {
        binding.dataStateView.changeState(DataState.LOADING);
        load(new Runnable() {
            @Override
            public void run() {
                binding.dataStateView.changeState(DataState.ERROR);
                Toast.makeText(MainActivity.this, "error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void load(Runnable runnable) {
        new Handler().postDelayed(runnable, 1000);
    }
}
