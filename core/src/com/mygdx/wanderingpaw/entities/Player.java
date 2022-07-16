package com.mygdx.wanderingpaw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.wanderingpaw.main.Game;
import com.mygdx.wanderingpaw.states.Play;

public class Player extends B2DSprite {
    public Player(Body body) {
        super(body);

        Texture tex = Game.res.getTexture("cat");
        // TextureRegion[] sprites = new TextureRegion[4];
        TextureRegion[] sprites = TextureRegion.split(tex, 96, 96)[0];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 96, 0, 96, 96);
        }
        animation.setFrames(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
}

// didn't work
//        if (Play.getPlayer().getBody().getLinearVelocity().x >= 0 ) {
//            Texture tex = Game.res.getTexture("cat");
//            // TextureRegion[] sprites = new TextureRegion[4];
//            TextureRegion[] sprites = TextureRegion.split(tex, 96, 96)[0];
//            for (int i = 0; i < sprites.length; i++) {
//                sprites[i] = new TextureRegion(tex, i * 96, 0, 96, 96);
//            }
//            animation.setFrames(sprites, 1 / 12f);
//
//            width = sprites[0].getRegionWidth();
//            height = sprites[0].getRegionHeight();
//        }
//
//        else if (Play.getPlayer().getBody().getLinearVelocity().x < 0) {
//            Texture tex = Game.res.getTexture("catrev");
//            // TextureRegion[] sprites = new TextureRegion[4];
//            TextureRegion[] sprites = TextureRegion.split(tex, 96, 96)[0];
//            for (int i = 0; i < sprites.length; i++) {
//                sprites[i] = new TextureRegion(tex, i * 96, 0, 96, 96);
//            }
//            animation.setFrames(sprites, 1 / 12f);
//
//            width = sprites[0].getRegionWidth();
//            height = sprites[0].getRegionHeight();
//        }



