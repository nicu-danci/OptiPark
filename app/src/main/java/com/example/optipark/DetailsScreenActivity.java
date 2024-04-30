package com.example.optipark;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.optipark.databinding.ActivityDetailsScreenBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class DetailsScreenActivity extends DrawerBaseActivity {

    // Variables
    ActivityDetailsScreenBinding activityDetailsScreenBinding;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final String[] PERMISSIONS = {Manifest.permission.VIBRATE};
    private Button backBtn;
    private TextView nameTextView;
    private TextView availabilityTextView;
    private Button selectButton;
    private ParkingSpace parkingSpace;
    private int selectedDurationMinutes = 0;

    private static final long NOTIFICATION_OFFSET_MS = 30 * 1000; // 30 seconds before end time
    private static final int NOTIFICATION_REQUEST_CODE = 1001;

    private PendingIntent notificationIntent;
    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout using ViewBinding
        activityDetailsScreenBinding = ActivityDetailsScreenBinding.inflate(getLayoutInflater());
        setTitle("Details");
        setContentView(activityDetailsScreenBinding.getRoot());

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView);
        availabilityTextView = findViewById(R.id.availabilityTextView);
        selectButton = findViewById(R.id.selectButton);
        backBtn = findViewById(R.id.back2); // Initialize the back button

        // Initialize AlarmManager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Retrieve parking space details from intent
        parkingSpace = getIntent().getParcelableExtra("parking_space");

        // Populate UI with parking space details
        if (parkingSpace != null) {
            nameTextView.setText(parkingSpace.getName());

            String availabilityText = parkingSpace.getAvailability() ? "Available" : "Not Available";
            availabilityTextView.setText(availabilityText);
        }

        // Set onClickListener for duration selection buttons
        findViewById(R.id.btn1Hour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton.setEnabled(true); // Enable the select button
                selectButton.setText("Pay £2");
                selectedDurationMinutes = 1; // 1 minute for testing (1 hour)
            }
        });

        findViewById(R.id.btn2Hours).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton.setEnabled(true); // Enable the select button
                selectButton.setText("Pay £3.5");
                selectedDurationMinutes = 2; // 2 minutes for testing (2 hours)
            }
        });

        findViewById(R.id.btn4Hours).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton.setEnabled(true); // Enable the select button
                selectButton.setText("Pay £6");
                selectedDurationMinutes = 4; // 4 minutes for testing (4 hours)
            }
        });

        findViewById(R.id.btn8Hours).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton.setEnabled(true); // Enable the select button
                selectButton.setText("Pay £10");
                selectedDurationMinutes = 8; // 8 minutes for testing (8 hours)
            }
        });

        findViewById(R.id.btn12Hours).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton.setEnabled(true); // Enable the select button
                selectButton.setText("Pay £14");
                selectedDurationMinutes = 12; // 12 minutes for testing (12 hours)
            }
        });

        findViewById(R.id.btn24Hours).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton.setEnabled(true); // Enable the select button
                selectButton.setText("Pay £25");
                selectedDurationMinutes = 24; // 24 minutes for testing (24 hours)
            }
        });

        // Set onClickListener for pay button
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDurationMinutes > 0) {
                    if (checkPermissions()) {
                        updateParkingSpaceAvailability(selectedDurationMinutes);
                    } else {
                        requestPermissions();
                    }
                } else {
                    Toast.makeText(DetailsScreenActivity.this, "Please select a duration first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set onClickListener for the back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                startActivity(new Intent(DetailsScreenActivity.this, MainActivity.class));
                finish(); // Finish the current activity
            }
        });
    }

    // Function to check if the required permission is granted
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED;
    }

    // Function to request the required permission
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with updating parking space availability
                updateParkingSpaceAvailability(selectedDurationMinutes);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied. Cannot proceed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Function to update parking space availability
    private void updateParkingSpaceAvailability(int durationInMinutes) {
        if (parkingSpace != null) {
            // Calculate end time for the reservation
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, durationInMinutes);
            Date endTime = calendar.getTime();

            // Create a new reservation
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Date startTime = new Date(); // Current time
            ParkingSpace.Reservation reservation = new ParkingSpace.Reservation(userId, startTime, endTime);

            // Update the availability status of the selected parking space in Firebase Firestore
            FirebaseFirestore.getInstance().collection("parking_spaces").document(parkingSpace.getName()).update("availability", false, "reservation", reservation).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    scheduleNotification();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("parking_space_name", parkingSpace.getName()); // Pass parking space ID
                    intent.putExtra("selected_duration", durationInMinutes); // Pass selected duration
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle failure to update availability in Firestore
                    Toast.makeText(DetailsScreenActivity.this, "Failed to update availability", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // Function to schedule a notification
    private void scheduleNotification() {
        Log.d("Notification", "scheduleNotification() method called");
        if (parkingSpace != null && parkingSpace.getReservation() != null) {
            Date endTime = parkingSpace.getReservation().getEndTime();
            long notificationTime = endTime.getTime() - NOTIFICATION_OFFSET_MS;

            Log.d("Notification", "Notification time: " + new Date(notificationTime));

            Intent intent = new Intent(this, AlarmReceiver.class); // Use your AlarmReceiver class
            intent.putExtra("reservation_name", parkingSpace.getName());
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Schedule the alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, alarmIntent);

            Log.d("Notification", "Alarm scheduled");
        } else {
            Log.e("Notification", "Failed to schedule alarm: Parking space or reservation is null");
        }
    }
}
