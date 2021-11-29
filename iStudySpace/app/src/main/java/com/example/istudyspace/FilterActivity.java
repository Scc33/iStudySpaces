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
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox groupButton;
    private CheckBox coffeeButton;
    private CheckBox foodButton;
    private RangeSlider maxDistSlider;
    private RangeSlider noiseSlider;
    private RangeSlider interactionSlider;

    private MaterialButtonToggleGroup toggleGroup;
    private Button applyButton;

    private String tabOn;
    private String noiseLevel;
    private Boolean groupWork;
    private Boolean coffee;
    private Boolean food;
    private float maxDist;
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
            maxDist = extras.getFloat("maxDist");
        }
        setContentView(R.layout.activity_filter);

        applyButton = (Button) findViewById(R.id.apply);
        groupButton = (CheckBox) findViewById(R.id.groupCheck);
        coffeeButton = (CheckBox) findViewById(R.id.coffeeCheck);
        foodButton = (CheckBox) findViewById(R.id.foodCheck);

        maxDistSlider = findViewById(R.id.maxDistSlider);
        noiseSlider = findViewById(R.id.noiseSlider);
        interactionSlider = findViewById(R.id.interactionSlider);

        maxDistSlider.setTickVisible(true);
        noiseSlider.setTickVisible(true);
        interactionSlider.setTickVisible(true);

        maxDistSlider.setValues(maxDist);
        if (noiseLevel.equals("quiet")) {
            noiseSlider.setValues(1.0f);
        } else if (noiseLevel.equals("ambient")) {
            noiseSlider.setValues(2.0f);
        } else if (noiseLevel.equals("loud")) {
            noiseSlider.setValues(3.0f);
        }
        if (zoomInteraction.equals("minimal")) {
            interactionSlider.setValues(1.0f);
        } else if (zoomInteraction.equals("moderate")) {
            interactionSlider.setValues(2.0f);
        } else if (zoomInteraction.equals("maximal")) {
            interactionSlider.setValues(3.0f);
        }
        noiseSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                if (value == 0) {
                    noiseLevel = "any";
                } else if (value == 1.0) {
                    noiseLevel = "quiet";
                } else if (value == 2.0) {
                    noiseLevel = "ambient";
                } else if (value == 3.0) {
                    noiseLevel = "loud";
                }
            }
        });
        interactionSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                if (value == 0) {
                    zoomInteraction = "any";
                } else if (value == 1.0) {
                    zoomInteraction = "minimal";
                } else if (value == 2.0) {
                    zoomInteraction = "moderate";
                } else if (value == 3.0) {
                    zoomInteraction = "maximal";
                }
            }
        });
        maxDistSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                maxDist = value;
            }
        });
        noiseSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                if (value == 0.0f)
                    return "Any";
                if (value == 1.0f)
                    return "Quiet";
                if (value == 2.0f)
                    return "Ambient";
                if (value == 3.0f)
                    return "Loud";
                return String.format(Locale.US, "%.0f", value);
            }
        });
        interactionSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                if (value == 0.0f)
                    return "Any";
                if (value == 1.0f)
                    return "Minimal";
                if (value == 2.0f)
                    return "Moderate";
                if (value == 3.0f)
                    return "Maximal";
                return String.format(Locale.US, "%.0f", value);
            }
        });

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
            intent.putExtra("maxDist", maxDist);
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