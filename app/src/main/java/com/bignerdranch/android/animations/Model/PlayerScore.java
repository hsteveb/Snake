package com.bignerdranch.android.animations.Model;

public class PlayerScore {

    private String name, score;

    public PlayerScore(String name, String score)
    {
        this.score = score;
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
