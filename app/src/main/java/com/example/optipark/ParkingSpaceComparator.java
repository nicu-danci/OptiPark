package com.example.optipark;

import java.util.Comparator;

public class ParkingSpaceComparator implements Comparator<ParkingSpace> {
    @Override
    public int compare(ParkingSpace space1, ParkingSpace space2) {
        String name1 = space1.getName();
        String name2 = space2.getName();

        // Split the names into alphabetical and numeric parts
        String alphaPart1 = name1.replaceAll("[^A-Za-z]+", "");
        String alphaPart2 = name2.replaceAll("[^A-Za-z]+", "");
        int result = alphaPart1.compareToIgnoreCase(alphaPart2);

        // If the alphabetical parts are equal, compare the numeric parts
        if (result == 0) {
            int numPart1 = Integer.parseInt(name1.replaceAll("\\D+", ""));
            int numPart2 = Integer.parseInt(name2.replaceAll("\\D+", ""));
            result = Integer.compare(numPart1, numPart2);
        }

        return result;
    }
}
