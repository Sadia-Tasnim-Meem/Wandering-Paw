package com.mygdx.wanderingpaw.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.wanderingpaw.handlers.GameButton;
import com.mygdx.wanderingpaw.handlers.GameStateManager;
import com.mygdx.wanderingpaw.main.Game;


public class LevelSelect extends GameState {

  private TextureRegion reg;

    private GameButton[][] buttons;

    public LevelSelect(GameStateManager gsm) {

        super(gsm);

        reg = new TextureRegion(Game.res.getTexture("sky-image"), 0, 0, 1280, 720);


        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gsm.setState(GameStateManager.MENU);
        }
    }

    public void update(float dt) {

        handleInput();


    }

    public void render() {

        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(reg, 0, 0);
        sb.end();


    }

    @Override
    public void dispose() {

    }
}