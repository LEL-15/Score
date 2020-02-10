package com.example.scores;

import java.util.ArrayList;

//A class for each item; used to display in buy page
public class Game {
    private String name;
    private String id;
    private String type;
    ArrayList<String> Players = new ArrayList<String>();

    public Game() {
        //empty constructor needed
    }

    public Game(String name, String id, String type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }
    //Get all things in item
    public String getName() {
        return name;
    }
    public String getID(){return id;}
    public String getType(){return type;}
    public ArrayList<String> getPlayers(){return Players;}


    //Set all things in item
    public void setName(String Name) {
        name = Name;
    }
    public void setId(String Id) {
        id = Id;
    }
    public void setType(String Type) {
        type = Type;
    }
    public void addPlayer(String player){Players.add(player);}
}