package com.example.optipark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

    // Adapter class for populating ParkingSpace objects into a RecyclerView.
public class ParkingSpaceAdapter extends RecyclerView.Adapter<ParkingSpaceAdapter.ViewHolder> {

    // Variables
    private List<ParkingSpace> parkingSpaces;
    private FirebaseFirestore db;
    private OnItemClickListener listener;

    // Constructor to initialize the ParkingSpaceAdapter.
    public ParkingSpaceAdapter(List<ParkingSpace> parkingSpaces, FirebaseFirestore db) {
        this.parkingSpaces = parkingSpaces;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_parkin_space, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingSpace parkingSpace = parkingSpaces.get(position);
        holder.textName.setText(parkingSpace.getName());

        // Set text color based on availability
        if (parkingSpace.getAvailability()) {
            holder.textAvailability.setText("Available");
            holder.textAvailability.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.availableColor));
            holder.itemView.setClickable(true); // Enable click listener
        } else {
            holder.textAvailability.setText("Not Available");
            holder.textAvailability.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.notAvailableColor));
            holder.itemView.setClickable(false); // Disable click listener
        }

        // Set EV charging availability text and visibility
        if (parkingSpace.isEvChargingAvailable()) {
            holder.textEvCharging.setVisibility(View.VISIBLE);
        } else {
            holder.textEvCharging.setVisibility(View.GONE);
        }

        // Set click listener for each parking space if available
        if (parkingSpace.getAvailability()) {
            holder.itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(parkingSpace);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return parkingSpaces.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ParkingSpace parkingSpace);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textAvailability, textEvCharging;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_parking_space_name);
            textAvailability = itemView.findViewById(R.id.text_parking_space_availability);
            textEvCharging = itemView.findViewById(R.id.textEvCharging);
        }
    }
}
