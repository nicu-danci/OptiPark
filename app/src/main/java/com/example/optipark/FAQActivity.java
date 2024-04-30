package com.example.optipark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.optipark.databinding.ActivityFaqactivityBinding;

public class FAQActivity extends DrawerBaseActivity {

    ActivityFaqactivityBinding activityFaqactivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout using ViewBinding
        activityFaqactivityBinding = ActivityFaqactivityBinding.inflate(getLayoutInflater());
        setTitle("FAQ");

        setContentView(activityFaqactivityBinding.getRoot());
    }
}