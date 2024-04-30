package com.example.optipark;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ParkingSpace implements Parcelable {

    // Variables
    private String name;
    private boolean availability;
    private boolean evChargingAvailable; // New field for EV charging availability
    private int xCoordinate; // X coordinate of the parking space
    private int yCoordinate; // Y coordinate of the parking space
    private Reservation reservation;

    // Required empty public constructor for Firestore
    public ParkingSpace() {
    }

    // Constructor with parameters
    public ParkingSpace(String name, boolean availability, boolean evChargingAvailable, int xCoordinate, int yCoordinate) {
        this.name = name;
        this.availability = availability;
        this.evChargingAvailable = evChargingAvailable;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public boolean isEvChargingAvailable() {
        return evChargingAvailable;
    }

    public void setEvChargingAvailable(boolean evChargingAvailable) {
        this.evChargingAvailable = evChargingAvailable;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    // Nested Reservation class
    public static class Reservation implements Parcelable {
        private String userId;
        private Date startTime;
        private Date endTime;

        // Constructors
        public Reservation() {
        }

        public Reservation(String userId, Date startTime, Date endTime) {
            this.userId = userId;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        // Getters and setters
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        // Parcelable implementation for Reservation class
        protected Reservation(Parcel in) {
            userId = in.readString();
            startTime = new Date(in.readLong());
            endTime = new Date(in.readLong());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(userId);
            dest.writeLong(startTime.getTime());
            dest.writeLong(endTime.getTime());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
            @Override
            public Reservation createFromParcel(Parcel in) {
                return new Reservation(in);
            }

            @Override
            public Reservation[] newArray(int size) {
                return new Reservation[size];
            }
        };
    }

    // Parcelable implementation for ParkingSpace class
    protected ParkingSpace(Parcel in) {
        name = in.readString();
        availability = in.readByte() != 0;
        evChargingAvailable = in.readByte() != 0; // Read EV charging availability from parcel
        xCoordinate = in.readInt(); // Read X coordinate from parcel
        yCoordinate = in.readInt(); // Read Y coordinate from parcel
        reservation = in.readParcelable(Reservation.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (availability ? 1 : 0));
        dest.writeByte((byte) (evChargingAvailable ? 1 : 0)); // Write EV charging availability to parcel
        dest.writeInt(xCoordinate); // Write X coordinate to parcel
        dest.writeInt(yCoordinate); // Write Y coordinate to parcel
        dest.writeParcelable(reservation, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParkingSpace> CREATOR = new Creator<ParkingSpace>() {
        @Override
        public ParkingSpace createFromParcel(Parcel in) {
            return new ParkingSpace(in);
        }

        @Override
        public ParkingSpace[] newArray(int size) {
            return new ParkingSpace[size];
        }
    };
}

