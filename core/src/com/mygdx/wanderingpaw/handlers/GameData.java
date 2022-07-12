package com.mygdx.wanderingpaw.handlers;

import java.io.Serializable;

public class GameData implements Serializable {

    private static final long serialVersionUID = 1;

    private boolean[] levelUnlocked;

    public GameData() {
        levelUnlocked = new boolean[4];
    }

    // sets up an array that indicates all level are locked except level 1
    public void init() {
        levelUnlocked[0] = true;
        for(int i = 1; i < 4; i++) {
            levelUnlocked[i] = false;
        }
    }

    public boolean[] getlevelUnlocked(){
        return levelUnlocked;
    }
    public void setlevelUnlocked(int num){
        levelUnlocked[num] = true;
    }

}

















