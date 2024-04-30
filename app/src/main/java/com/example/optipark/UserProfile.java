package com.example.optipark;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.optipark.databinding.ActivityUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserProfile extends DrawerBaseActivity {

    // Variables
    ActivityUserProfileBinding activityUserProfileBinding;
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;
    private TextView userRegistrationNumber;
    private TextView email;
    private TextView name;

    private TextView user_name_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout using ViewBinding
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setTitle("Account");
        setContentView(activityUserProfileBinding.getRoot());

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();
        user = auth.getCurrentUser();

        // Initialize Views
        userRegistrationNumber = findViewById(R.id.user_regNumber);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        user_name_header = findViewById(R.id.user_name_header);

        // Check if user is logged in
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            email.setText(user.getEmail());

            // Retrieve user data from Firestore
            DocumentReference documentReference = fStore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        // Extract user data from DocumentSnapshot
                        String regNumber = documentSnapshot.getString("regNumber");
                        String userName = documentSnapshot.getString("name");

                        // Set registration number if available
                        if (regNumber != null) {
                            userRegistrationNumber.setText(regNumber);
                        }

                        // Set user name if available
                        if (userName != null) {
                            name.setText(userName);
                            user_name_header.setText(userName); // Set user name in header as well
                        }
                    } else {
                        // Handle the case where DocumentSnapshot is null
                        Toast.makeText(UserProfile.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
