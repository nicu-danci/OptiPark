package com.example.optipark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    // Initializes the toolbar and navigation drawer.
    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }


    }

    // Performs navigation based on the selected item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(DrawerBaseActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (menuItem.getItemId() == R.id.nav_available) {
            // Handle the available case
            Intent intent = new Intent(DrawerBaseActivity.this, ParkingSpaceView.class);
            startActivity(intent);
            return true;
        } else if (menuItem.getItemId() == R.id.nav_map) {
            Intent intent = new Intent(DrawerBaseActivity.this, MapActivity.class);
            startActivity(intent);
            return true;
        } else if (menuItem.getItemId() == R.id.nav_account) {
            Intent intent = new Intent(DrawerBaseActivity.this, UserProfile.class);
            startActivity(intent);
            return true;
        } else if (menuItem.getItemId() == R.id.nav_login) {
            Intent intent = new Intent(DrawerBaseActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (menuItem.getItemId() == R.id.nav_logout) {
            Intent intent = new Intent(DrawerBaseActivity.this, LaunchActivity.class);
            startActivity(intent);
            return true;
        } else if (menuItem.getItemId() == R.id.nav_faq) {
            Intent intent = new Intent(DrawerBaseActivity.this, FAQActivity.class);
            startActivity(intent);
            return true;
        } else if (menuItem.getItemId() == R.id.nav_contact) {
            Intent intent = new Intent(DrawerBaseActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    // Sets the title of the activity.
    protected void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}