package com.example.istudyspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        applyButton = (Button) findViewById(R.id.apply);

        applyButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.apply) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}