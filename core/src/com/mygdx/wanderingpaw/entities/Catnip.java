package com.mygdx.wanderingpaw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.wanderingpaw.main.Game;

public class Catnip extends B2DSprite {
    public Catnip(Body body) {
        super(body);

        Texture tex = Game.res.getTexture("catnip");
        TextureRegion[] sprites = TextureRegion.split(tex, 32,32)[0];

        setAnimation(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
}




