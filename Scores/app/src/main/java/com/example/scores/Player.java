package com.example.scores;

//A class for each item; used to display in buy page
public class Player {
    private String name;
    private Double score;

    public Player() {
        //empty constructor needed
    }

    public Player(String name, Double score) {
        this.name = name;
        this.score = score;
    }
    //Get all things in item
    public String getName() {
        return name;
    }
    public Double getScore(){return score;}
    //Set all things in item
    public void setName(String Name) {
        name = Name;
    }
    public void setScore(Double Score) { score= Score;}
}