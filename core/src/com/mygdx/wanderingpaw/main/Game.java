package com.mygdx.wanderingpaw.main;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.wanderingpaw.handlers.*;

import static com.mygdx.wanderingpaw.handlers.B2DVars.PPM;


public class Game implements ApplicationListener {


    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;

    public static final int SCALE = 3;
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
        //res.loadTexture("res/images/background_image.jpg");
        res.loadTexture("res/images/cat sprite 128x32.png","cat");


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
