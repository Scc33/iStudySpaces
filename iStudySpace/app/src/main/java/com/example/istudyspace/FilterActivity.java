package com.example.istudyspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.button.MaterialButtonToggleGroup;

import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button anyButton;
    private Button quietButton;
    private Button ambientButton;
    private Button loudButton;
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
        }
        setContentView(R.layout.activity_filter);

        toggleGroup = (MaterialButtonToggleGroup) findViewById(R.id.toggleButton);
        applyButton = (Button) findViewById(R.id.apply);
        anyButton = (Button) findViewById(R.id.anyButton);
        quietButton = (Button) findViewById(R.id.quietButton);
        ambientButton = (Button) findViewById(R.id.ambientButton);
        loudButton = (Button) findViewById(R.id.loudButton);
        groupButton = (CheckBox) findViewById(R.id.groupCheck);
        coffeeButton = (CheckBox) findViewById(R.id.coffeeCheck);
        foodButton = (CheckBox) findViewById(R.id.foodCheck);

        if (noiseLevel.equals("any")) {
            toggleGroup.check(R.id.anyButton);
        } else if (noiseLevel.equals("quiet")) {
            toggleGroup.check(R.id.quietButton);
        } else if (noiseLevel.equals("ambient")) {
            toggleGroup.check(R.id.ambientButton);
        } else {
            toggleGroup.check(R.id.loudButton);
        }
        groupButton.setChecked(groupWork);
        coffeeButton.setChecked(coffee);
        foodButton.setChecked(food);

        applyButton.setOnClickListener(this);
        anyButton.setOnClickListener(this);
        quietButton.setOnClickListener(this);
        ambientButton.setOnClickListener(this);
        loudButton.setOnClickListener(this);
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
            startActivity(intent);
        } else if (v.getId() == R.id.anyButton) {
            noiseLevel = "any";
        } else if (v.getId() == R.id.quietButton) {
            noiseLevel = "quiet";
        } else if (v.getId() == R.id.ambientButton) {
            noiseLevel = "ambient";
        } else if (v.getId() == R.id.loudButton) {
            noiseLevel = "loud";
        } else if (v.getId() == R.id.groupCheck) {
            groupWork = !groupWork;
        } else if (v.getId() == R.id.coffeeCheck) {
            coffee = !coffee;
        } else if (v.getId() == R.id.foodCheck) {
            food = !food;
        }
    }
}