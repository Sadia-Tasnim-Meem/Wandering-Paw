package com.mygdx.wanderingpaw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.wanderingpaw.main.Game;

public class Player extends B2DSprite {
    public Player(Body body) {
        super(body);

        Texture tex = Game.res.getTexture("cat");
        TextureRegion[] sprites = new TextureRegion[4];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, 0, 0, 32, 32);
            /*if (i == 0) {
                sprites[i] = new TextureRegion(tex, 0, 0, 32, 32);
            }

            if (i == 1) {
                sprites[i] = new TextureRegion(tex, 32, 0, 37, 32);
            }

            if (i == 2) {
                sprites[i] = new TextureRegion(tex, 32 + 37, 0, 39, 32);
            }

            if (i == 3) {
                sprites[i] = new TextureRegion(tex, 32 + 37 + 39, 0, 38, 32);
            }*/
        }

        animation.setFrames(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
}
