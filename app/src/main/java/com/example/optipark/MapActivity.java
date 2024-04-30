package com.example.optipark;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.optipark.databinding.ActivityMapBinding;

// Activity to display a map and draw lines to designated parking spaces.
public class MapActivity extends DrawerBaseActivity {

    // Variables
    ActivityMapBinding activityMapBinding;
    private EditText inputField;
    private Button submitButton;
    private LineView lineView;
    private GridLayout imageContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout using ViewBinding
        activityMapBinding = ActivityMapBinding.inflate(getLayoutInflater());
        setTitle("Map");
        setContentView(activityMapBinding.getRoot());

        // Initialize views
        inputField = findViewById(R.id.inputField);
        submitButton = findViewById(R.id.submitButton);
        lineView = findViewById(R.id.lineView);
        imageContainer = findViewById(R.id.imageContainer);

        // Set onClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputField.getText().toString().trim();

                if (input.isEmpty()) {
                    // Show a toast message or an alert dialog to prompt the user
                    Toast.makeText(MapActivity.this, "Please enter a parking space ID", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the height of the toolbar
                    int toolbarHeight = getSupportActionBar().getHeight();

                    // Determine the destination ImageView based on the input
                    ImageView destinationView = null;

                    switch (input) {
                        case "A1":
                            destinationView = findViewById(R.id.A1);
                            break;
                        case "A2":
                            destinationView = findViewById(R.id.A2);
                            break;
                        case "A3":
                            destinationView = findViewById(R.id.A3);
                            break;
                        case "A4":
                            destinationView = findViewById(R.id.A4);
                            break;
                        case "A5":
                            destinationView = findViewById(R.id.A5);
                            break;
                        case "A6":
                            destinationView = findViewById(R.id.A6);
                            break;
                        case "A7":
                            destinationView = findViewById(R.id.A7);
                            break;
                        case "A8":
                            destinationView = findViewById(R.id.A8);
                            break;
                        case "A9":
                            destinationView = findViewById(R.id.A9);
                            break;
                        case "A10":
                            destinationView = findViewById(R.id.A10);
                            break;
                        case "A11":
                            destinationView = findViewById(R.id.A11);
                            break;
                        case "A12":
                            destinationView = findViewById(R.id.A12);
                            break;
                        case "A13":
                            destinationView = findViewById(R.id.A13);
                            break;
                        case "A14":
                            destinationView = findViewById(R.id.A14);
                            break;

                        case "A15":
                            destinationView = findViewById(R.id.A15);
                            break;

                        case "A16":
                            destinationView = findViewById(R.id.A16);
                            break;

                        case "A17":
                            destinationView = findViewById(R.id.A17);
                            break;

                        case "A18":
                            destinationView = findViewById(R.id.A18);
                            break;

                        case "A19":
                            destinationView = findViewById(R.id.A19);
                            break;

                        case "A20":
                            destinationView = findViewById(R.id.A20);
                            break;
                        // Add more cases for other input values as needed
                        default:
                            // If the input doesn't match any known parking space IDs
                            // Prompt the user with a toast message
                            Toast.makeText(MapActivity.this, "Incorrect parking space ID", Toast.LENGTH_SHORT).show();
                            break;
                    }


                    if (destinationView != null) {
                        // Get the coordinates of the destination ImageView
                        int[] location = new int[2];
                        destinationView.getLocationOnScreen(location);
                        int destinationX = location[0] + destinationView.getWidth() / 2; // X coordinate at the center of the view
                        int destinationY = location[1] - toolbarHeight; // Adjust Y coordinate by subtracting toolbar height
                        // Pass the coordinates to the LineView to draw the line
                        lineView.drawToParkingSpace(destinationX, destinationY);

                    }

                }

            }
        });
    }
}
