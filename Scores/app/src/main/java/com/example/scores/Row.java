package com.example.scores;

//A class for each item; used to display in buy page
public class Row {
    private String name;
    private Integer multiplier;

    public Row() {
        //empty constructor needed
    }

    public Row(String name, Integer multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }
    //Get all things in item
    public String getName() {
        return name;
    }
    public Integer getMultiplier(){return multiplier;}

    //Set all things in item
    public void setName(String Name) {
        name = Name;
    }
    public void setMultiplier(Integer Multiplier) { multiplier= Multiplier;}
}