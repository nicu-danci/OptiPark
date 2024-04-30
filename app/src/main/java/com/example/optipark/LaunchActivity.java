package com.example.optipark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.optipark.databinding.ActivityLaunchBinding;
import com.google.firebase.FirebaseApp;

public class LaunchActivity extends DrawerBaseActivity {

    // Variables
    ActivityLaunchBinding activityLaunchBinding;
    Button buttonReg;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout using ViewBinding
        activityLaunchBinding = ActivityLaunchBinding.inflate(getLayoutInflater());
        setTitle("OptiPark");
        setContentView(activityLaunchBinding.getRoot());

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize views
        buttonReg = findViewById(R.id.btnRegister);
        buttonLogin = findViewById(R.id.btnLogin);

        // Set onClickListeners for the register and login buttons
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}