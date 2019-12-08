package com.example.scores;

//A class for each item; used to display in buy page
public class Player {
    private String name;
    private Integer score;
    private String game_id;
    private String id;
    private String color;

    public Player() {
        //empty constructor needed
    }

    public Player(String name, Integer score, String game_id, String id, String color) {
        this.name = name;
        this.score = score;
        this.game_id=game_id;
        this.id=id;
        this.color = color;
    }
    //Get all things in item
    public String getName() {
        return name;
    }
    public Integer getScore(){return score;}
    public String getGame_id(){return game_id;}
    public String getId(){return id;}
    public String getColor(){return color;}
    //Set all things in item
    public void setName(String Name) {
        name = Name;
    }
    public void setScore(Integer Score) { score= Score;}
    public void setGame_id(String game_id) { this.game_id = game_id;}
    public void setId(String id) {this.id = id;}
    public void setColor(String color){this.color = color;}
}