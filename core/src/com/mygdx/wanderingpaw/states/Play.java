package com.mygdx.wanderingpaw.states;

import com.mygdx.wanderingpaw.handlers.GameStateManager;

public class Play extends GameState {

    public Play(GameStateManager gsm) {
        super(gsm);
    }

    public void handleInput() {

    }

    public void update(float dt) {
    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.end();
    }

    public void dispose() {

    }


}
