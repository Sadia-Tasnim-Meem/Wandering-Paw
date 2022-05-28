package com.mygdx.wandaringpaw.main;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Game implements ApplicationListener {

	public static final String TITLE = "Block Bunny";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;



	@Override
	public void create () {
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
	}
}
