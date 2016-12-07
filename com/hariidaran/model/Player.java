package com.hariidaran.model;

import com.hariidaran.io.Assistant;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {
    private static final long serialVersionUID = 1L;

    private String mFirstName;
    private String mLastName;
    private float mHeightInInches;
    private boolean mHasPreviousExperience;

    public Player(String firstName, String lastName, float heightInInches, boolean hasPreviousExperience) {
        mFirstName = Assistant.capitalize(firstName);
        mLastName = Assistant.capitalize(lastName);
        mHeightInInches = heightInInches;
        mHasPreviousExperience = hasPreviousExperience;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public float getHeightInInches() {
        return mHeightInInches;
    }

    public boolean hasPreviousExperience() {
        return mHasPreviousExperience;
    }

    public String details() {
        return String.format("%nName                : %s%n" +
                             "Height              : %.2f inches%n" +
                             "Previous experience : %s",
                             mFirstName + " " + mLastName,
                             mHeightInInches, mHasPreviousExperience ? "yes" : "none");
    }

    @Override
    public int compareTo(Player other) {
        // We always want to sort by last name then first name
        if (equals(other)) return 0;
        return mLastName.compareTo(other.mLastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player other = (Player) o;

        return mFirstName.equalsIgnoreCase(other.getFirstName()) && mLastName.equalsIgnoreCase(other.getLastName());
    }

    @Override
    public String toString() {
        return String.format("%s, %s (%.2f inches)", mLastName, mFirstName, mHeightInInches);
    }
}