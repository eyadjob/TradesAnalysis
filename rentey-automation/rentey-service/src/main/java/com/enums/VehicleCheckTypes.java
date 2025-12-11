package com.enums;

/**
 * Enum representing different vehicle check type IDs used in the application.
 */
public enum VehicleCheckTypes {
    RECEIVE_VEHICLE(6);  // Receive New Vehicle check type

    private final int id;

    VehicleCheckTypes(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
