package com.mygdx.wanderingpaw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.wanderingpaw.main.Game;

public class Spike extends B2DSprite {

    public Spike(Body body) {

        super(body);

        Texture tex = Game.res.getTexture("spike");
        TextureRegion[] sprites = TextureRegion.split(tex, 96, 96)[0];
        animation.setFrames(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

    }

}
