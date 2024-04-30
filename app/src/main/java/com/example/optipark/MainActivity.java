package com.example.optipark;

import androidx.annotation.Nullable;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.optipark.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainActivity extends DrawerBaseActivity {

    ActivityMainBinding activityMainBinding;

    // Variables
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    TextView userRegistrationNumber;
    RelativeLayout accountLayout;
    RelativeLayout availableLayout;
    RelativeLayout contactLayout;
    RelativeLayout logoutLayout;
    RelativeLayout mapLayout;
    RelativeLayout reservedLayout;
    TextView txtSpace;
    TextView txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout using ViewBinding
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setTitle("Dashboard");
        setContentView(activityMainBinding.getRoot());

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();
        user = auth.getCurrentUser();

        // Initialize views
        userRegistrationNumber = findViewById(R.id.user_regNumber);
        accountLayout = findViewById(R.id.account);
        availableLayout = findViewById(R.id.available);
        contactLayout = findViewById(R.id.contact);
        logoutLayout = findViewById(R.id.logout);
        mapLayout = findViewById(R.id.map);
        reservedLayout = findViewById(R.id.reserved);
        txtSpace = findViewById(R.id.txtSpace);
        txtTime = findViewById(R.id.txtTime);

        // Retrieve data from previous activity if available
        String parkingSpaceName = getIntent().getStringExtra("parking_space_name");
        String endTime = getIntent().getStringExtra("end_time");
        int selectedDuration = getIntent().getIntExtra("selected_duration", 0); // Retrieve selected duration
        txtSpace.setText(" No parking space reserved");
        if (selectedDuration > 0) {

            // Update TextViews with selected duration and parking space name
            txtTime.setVisibility(View.VISIBLE);
            txtSpace.setText("Parking space " + "\n" + parkingSpaceName + " for");
            String durationText = selectedDuration + " hour"; // Concatenate "hour" to the duration
            if (selectedDuration != 1) {
                durationText += "s"; // Add "s" for plural if duration is not 1
            }
            txtTime.setText(durationText); // Display selected duration with "hour"
        }

        // Create notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificationChanel";
            String description = "notificationChanel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Check if user is authenticated
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            DocumentReference documentReference = fStore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        String regNumber = documentSnapshot.getString("regNumber");
                        if (regNumber != null) {
                            userRegistrationNumber.setText(regNumber);
                        } else {
                            // Handle the case where "regNumber" is null
                        }
                    } else {
                        // Handle the case where documentSnapshot is null
                    }
                }
            });

        }

        // Set onClickListeners for various layouts
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        availableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ParkingSpaceView.class);
                startActivity(intent);
                finish();
            }
        });

        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                finish();
            }
        });

        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
