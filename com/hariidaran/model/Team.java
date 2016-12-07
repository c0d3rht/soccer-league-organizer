package com.hariidaran.model;

import com.hariidaran.io.Assistant;

import java.util.ArrayList;
import java.util.List;

public class Team implements Comparable<Team> {
    public static final int MAX_PLAYERS = 11;

    private String mName;
    private String mCoachName;
    private List<Player> mPlayers = new ArrayList<>();

    public Team(String name, String coachName) {
        mName = name;
        mCoachName = Assistant.capitalize(coachName);
    }

    public boolean isExperienced() {
        double number = 0;

        for (Player player : mPlayers) {
            if (player.hasPreviousExperience()) {
                number += 1;
            }
        }

        return number >= mPlayers.size() / 2;
    }

    public long getExperienceLevel() {
        double number = 0;

        for (Player player : mPlayers) {
            if (player.hasPreviousExperience()) {
                number += 1;
            }
        }

        return Math.round(number / (double) mPlayers.size() * 100);
    }

    public String getName() {
        return mName;
    }

    public String getCoachName() {
        return mCoachName;
    }

    public Player get(int i) {
        return mPlayers.get(i);
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public int size() {
        return mPlayers.size();
    }

    public void add(Player player) {
        mPlayers.add(player);
    }

    public void remove(Player player) {
        mPlayers.remove(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team other = (Team) o;

        return other.mName.equalsIgnoreCase(mName) || other.mCoachName.equalsIgnoreCase(mCoachName);

    }

    @Override
    public int compareTo(Team other) {
        if (equals(other)) return 0;
        return mName.compareTo(other.mName);
    }

    @Override
    public String toString() {
        return mName + ", managed by " + mCoachName;
    }
}