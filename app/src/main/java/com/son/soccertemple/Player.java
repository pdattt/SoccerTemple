package com.son.soccertemple;

public class Player {
    private String ID;
    private String Name;

    public Player() {}

    public Player(String ID, String Name){
        this.ID = ID;
        this.Name = Name;
    }

    public String getID(){
        return this.ID;
    }

    public String getName(){
        return this.Name;
    }
}

