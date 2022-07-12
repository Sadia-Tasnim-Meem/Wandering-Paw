package com.mygdx.wanderingpaw.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.wanderingpaw.handlers.GameButton;
import com.mygdx.wanderingpaw.handlers.GameStateManager;
import com.mygdx.wanderingpaw.handlers.Save;
import com.mygdx.wanderingpaw.main.Game;


public class LevelSelect extends GameState {

  private TextureRegion reg;

    private GameButton[][] buttons;

    private GameButton newGame;
    private GameButton resume;
    private GameButton exit;

    private int levelSelected;

    public LevelSelect(GameStateManager gsm) {

        super(gsm);

        reg = new TextureRegion(Game.res.getTexture("sky-image"), 0, 0, 1280, 720);

        Texture tex = Game.res.getTexture("Menu Buttons");
        newGame = new GameButton(new TextureRegion(tex, 0, 0, 233, 96), (Game.V_WIDTH/10)*8, (Game.V_HEIGHT/7)*3, cam);
        resume = new GameButton(new  TextureRegion(tex, 0, 96, 233, 96),(Game.V_WIDTH/10)*8, (Game.V_HEIGHT/7)*2,cam);
        exit = new GameButton(new  TextureRegion(tex, 0, 96*2, 233, 96),(Game.V_WIDTH/10)*8, (Game.V_HEIGHT/7),cam);


        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        Save.load();
        Play.levelunlocked = Save.gd.getlevelUnlocked();

        for(int i=0; i<4; i++){
            System.out.println(Play.levelunlocked[i]);
        }

    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gsm.setState(GameStateManager.MENU);
        }
        if(newGame.isClicked()){
            Play.level = 1;
            gsm.setState(GameStateManager.PLAY);
        }
        if(resume.isClicked()){
            if(Play.levelunlocked[1]){
                Play.level = 2;
                gsm.setState(GameStateManager.PLAY);
            }
        }
        if(exit.isClicked()){
            if(Play.levelunlocked[2]){
                Play.level = 3;
                gsm.setState(GameStateManager.PLAY);
            }
        }
    }

    public void update(float dt) {

        handleInput();
        newGame.update(dt);
        resume.update(dt);
        exit.update(dt);

    }

    public void render() {

        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(reg, 0, 0);
        sb.end();

        newGame.render(sb);
        resume.render(sb);
        exit.render(sb);

    }

    @Override
    public void dispose() {

    }
}