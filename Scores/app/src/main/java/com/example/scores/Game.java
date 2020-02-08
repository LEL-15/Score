package com.example.scores;

//A class for each item; used to display in buy page
public class Game {
    private String name;
    private String id;

    public Game() {
        //empty constructor needed
    }

    public Game(String name, String id) {
        this.name = name;
        this.id = id;
    }
    //Get all things in item
    public String getName() {
        return name;
    }
    public String getID(){return id;}
    //Set all things in item
    public void setName(String Name) {
        name = Name;
    }
    public void setId(String Id) {
        id = Id;
    }
}