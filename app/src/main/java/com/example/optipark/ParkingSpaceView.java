package com.example.optipark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkingSpaceView extends DrawerBaseActivity implements ParkingSpaceAdapter.OnItemClickListener {

    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view to the activity layout
        setContentView(R.layout.activity_layout);

        // Initialize views
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        backBtn = findViewById(R.id.back);

        // Set onClickListener for the back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                startActivity(new Intent(ParkingSpaceView.this, MainActivity.class));
                finish(); // Finish the current activity
            }
        });

        // Set up GridLayoutManager with span count 2 for alternating left and right sides
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize list to hold parking spaces
        List<ParkingSpace> parkingSpaces = new ArrayList<>();

        // Initialize adapter
        ParkingSpaceAdapter adapter = new ParkingSpaceAdapter(parkingSpaces, FirebaseFirestore.getInstance());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this); // Set item click listener

        // Retrieve all parking spaces from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("parking_spaces").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ParkingSpace parkingSpace = document.toObject(ParkingSpace.class);

                // Access the EV field
                boolean evChargingAvailable = parkingSpace.isEvChargingAvailable();
                // Log the value of availability
                Log.d("Firestore", "Availability: " + parkingSpace.getAvailability());

                // Add parking space to the list
                parkingSpaces.add(parkingSpace);
            }

            // Sort the parking spaces
            Collections.sort(parkingSpaces, new ParkingSpaceComparator());

            // Notify adapter of data change
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            // Handle failure to retrieve data
            Log.e("Firestore", "Error fetching parking spaces: " + e.getMessage());
        });
    }

    @Override
    public void onItemClick(ParkingSpace parkingSpace) {
        // Handle item click
        Intent intent = new Intent(this, DetailsScreenActivity.class);
        intent.putExtra("parking_space", parkingSpace);
        startActivity(intent);
    }
}