package com.mygdx.wanderingpaw.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUD {
    private Player player;

    public HUD(Player player){
        this.player = player;
    }

    public void render(SpriteBatch sb){
        sb.begin();
        sb.end();
    }

}
