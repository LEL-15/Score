package com.example.scores;

//A class for each item; used to display in buy page
public class Game {
    private String name;

    public Game() {
        //empty constructor needed
    }

    public Game(String name) {
        this.name = name;
    }
    //Get all things in item
    public String getName() {
        return name;
    }

    //Set all things in item
    public void setName(String Name) {
        name = Name;
    }
}