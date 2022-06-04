package com.mygdx.wanderingpaw.main;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.wanderingpaw.handlers.Content;
import com.mygdx.wanderingpaw.handlers.CustomizedInput;
import com.mygdx.wanderingpaw.handlers.CustomizedInputProcessor;
import com.mygdx.wanderingpaw.handlers.GameStateManager;


public class Game implements ApplicationListener {


    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final int SCALE = 2;
    public static final float STEP = 1 / 60f;
    private float accum;
    public static Content res;

    private GameStateManager gsm;



    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    public SpriteBatch getSpriteBatch() {
        return sb;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public OrthographicCamera getHUDCamera() {
        return hudCam;
    }


    @Override
    public void create() {

        Gdx.input.setInputProcessor(new CustomizedInputProcessor());

        res = new Content();
        //res.loadTexture("res/images/background_image.jpg");
        res.loadTexture("res/images/cat_sprite.jpg","cat");


        Gdx.input.setInputProcessor(new CustomizedInputProcessor());

        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);


        gsm = new GameStateManager(this);
    }

    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void render() {

        accum += Gdx.graphics.getDeltaTime();

        while(accum >= STEP){
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
            CustomizedInput.update();
        }
        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        sb.draw(res.getTexture("cat"),0,0);
        sb.end();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }
}
