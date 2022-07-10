package com.mygdx.wanderingpaw.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.mygdx.wanderingpaw.handlers.*;
import com.mygdx.wanderingpaw.main.Game;


public class Menu extends GameState {

    private boolean debug = false;

    private Background bg;

    private World world;
    private Box2DDebugRenderer b2dRenderer;


    public Menu(GameStateManager gsm) {

        super(gsm);

        Texture tex = Game.res.getTexture("menu");
        bg = new Background(new TextureRegion(tex), cam, 1f);
        bg.setVector(-20, 0);


        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        world = new World(new Vector2(0, -9.8f * 5), true);
        //world = new World(new Vector2(0, 0), true);
        b2dRenderer = new Box2DDebugRenderer();
    }



    public void handleInput() {

        // mouse/touch input
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            gsm.setState(GameStateManager.PLAY);
        }

    }

    public void update(float dt) {

        handleInput();

        world.step(dt / 5, 8, 3);

    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);

        // draw background
        bg.render(sb);
    }

    public void dispose() {
        // everything is in the resource manager com.neet.blockbunny.handlers.Content
    }

}
