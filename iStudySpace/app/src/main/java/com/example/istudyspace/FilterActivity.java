package com.example.istudyspace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.button.MaterialButtonToggleGroup;

import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox groupButton;
    private CheckBox coffeeButton;
    private CheckBox foodButton;

    private MaterialButtonToggleGroup toggleGroup;
    private Button applyButton;

    private String tabOn;
    private String noiseLevel;
    private Boolean groupWork;
    private Boolean coffee;
    private Boolean food;
    private String zoomInteraction = "any";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tabOn = extras.getString("tab");
            noiseLevel = extras.getString("noiseLevel");;
            groupWork = extras.getBoolean("groupWork");;
            coffee = extras.getBoolean("coffee");
            food = extras.getBoolean("food");
            zoomInteraction = extras.getString("zoom");
        }
        setContentView(R.layout.activity_filter);

        applyButton = (Button) findViewById(R.id.apply);
        groupButton = (CheckBox) findViewById(R.id.groupCheck);
        coffeeButton = (CheckBox) findViewById(R.id.coffeeCheck);
        foodButton = (CheckBox) findViewById(R.id.foodCheck);

        groupButton.setChecked(groupWork);
        coffeeButton.setChecked(coffee);
        foodButton.setChecked(food);

        applyButton.setOnClickListener(this);
        groupButton.setOnClickListener(this);
        coffeeButton.setOnClickListener(this);
        foodButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.apply) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("tab", tabOn);
            intent.putExtra("noiseLevel", noiseLevel);
            intent.putExtra("groupWork", groupWork);
            intent.putExtra("coffee", coffee);
            intent.putExtra("food", food);
            intent.putExtra("zoom", zoomInteraction);
            // Save Filters in Intent and return to main page
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (v.getId() == R.id.groupCheck) {
            groupWork = !groupWork;
        } else if (v.getId() == R.id.coffeeCheck) {
            coffee = !coffee;
        } else if (v.getId() == R.id.foodCheck) {
            food = !food;
        }
    }
}