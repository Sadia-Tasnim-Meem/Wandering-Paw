package com.mygdx.wanderingpaw.main;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.wanderingpaw.handlers.*;

import static com.mygdx.wanderingpaw.handlers.B2DVars.PPM;


public class Game implements ApplicationListener {
    public static final int V_WIDTH = 1280; //320
    public static final int V_HEIGHT = 720;//240

    public static final int SCALE = 2;
    public static final float STEP = 1 / 60f;

    private SpriteBatch sb;
    private BoundedCamera cam;
    private OrthographicCamera hudCam;
    private GameStateManager gsm;
    public static Content res;


    @Override
    public void create() {

        Gdx.input.setInputProcessor(new CustomizedInputProcessor());

        res = new Content();
        //res.loadTexture("res/images/sprite all.png","cat");
        res.loadTexture("res/images/sprite.png","cat");
        res.loadTexture("res/images/sprite backward.png","catrev");
        res.loadTexture("res/images/sky_s1280x720.jpg","sky-image");
        res.loadTexture("res/images/fence.png","fence-image");
        res.loadTexture("res/images/home screen.jpg","menu");
        res.loadTexture("res/images/Buttons.png","Buttons");
        res.loadTexture("res/images/catnip 1 transparent 32x32.png","catnip");
        res.loadTexture("res/images/butterfly 96x96.png","butterfly");
        res.loadTexture("res/images/butterfly 2 32x32.png","butterfly_icon");
        res.loadTexture("res/images/spike.png","spike");
        res.loadTexture("res/images/escape-icon.png", "escape_button");




        //Gdx.input.setInputProcessor(new CustomizedInputProcessor());
        cam = new BoundedCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);


        sb = new SpriteBatch();

        gsm = new GameStateManager(this);
    }
    @Override
    public void render() {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();
        CustomizedInput.update();

    }



    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        res.removeAll();
    }
    @Override
    public void resize(int width, int height) {

    }


    public SpriteBatch getSpriteBatch() {
        return sb;
    }

    public BoundedCamera getCamera() {
        return cam;
    }

    public OrthographicCamera getHUDCamera() {
        return hudCam;
    }

}
