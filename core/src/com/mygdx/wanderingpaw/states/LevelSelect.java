package com.mygdx.wanderingpaw.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.wanderingpaw.handlers.GameButton;
import com.mygdx.wanderingpaw.handlers.GameStateManager;
import com.mygdx.wanderingpaw.handlers.Save;
import com.mygdx.wanderingpaw.main.Game;


public class LevelSelect extends GameState {

  private TextureRegion reg;

    private GameButton[][] buttons;

    private GameButton Level1;
    private GameButton Level2;
    private GameButton Level3;
    private GameButton Level4;
    private GameButton escape;

    private int levelSelected;
    private Sprite lock1;
    private Sprite lock2;
    private Sprite lock3;
    private Sprite lock4;

    public LevelSelect(GameStateManager gsm) {

        super(gsm);

        reg = new TextureRegion(Game.res.getTexture("sky-image"), 0, 0, 1280, 720);

        Texture tex = Game.res.getTexture("Buttons");
        Level1 = new GameButton(new TextureRegion(tex, 233, 0, 233, 96), (Game.V_WIDTH/2), (Game.V_HEIGHT/10)*8, cam);
        Level2 = new GameButton(new  TextureRegion(tex, 233, 96, 233, 96),(Game.V_WIDTH/2), (Game.V_HEIGHT/10)*6,cam);
        Level3 = new GameButton(new  TextureRegion(tex, 233, 96*2, 233, 96),(Game.V_WIDTH/2), (Game.V_HEIGHT/10)*4,cam);
        Level4 = new GameButton(new  TextureRegion(tex, 233, 96*3, 233, 96),(Game.V_WIDTH/2), (Game.V_HEIGHT/10)*2,cam);

        Texture escape_tex = Game.res.getTexture("escape_button");
        escape = new GameButton(new TextureRegion(escape_tex, 0,0,40,40), (Game.V_WIDTH/30)*1, (Game.V_HEIGHT/20)*19, cam);

        Texture life_icon_tex = Game.res.getTexture("lock");
        lock1 = new Sprite(life_icon_tex, 0, 0, 90, 90);
        lock1.setPosition(750, (Game.V_HEIGHT/10)*8);
        lock2 = new Sprite(life_icon_tex, 0, 0, 90, 90);
        lock2.setPosition(750, (Game.V_HEIGHT/10)*6);
        lock3 = new Sprite(life_icon_tex, 0, 0, 90, 90);
        lock3.setPosition(750, (Game.V_HEIGHT/10)*4);
        lock4 = new Sprite(life_icon_tex, 0, 0, 90, 90);
        lock4.setPosition(750, (Game.V_HEIGHT/10)*2);


        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        Save.load();
        Play.levelunlocked = Save.gd.getlevelUnlocked();



    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || escape.isClicked()) {
            gsm.setState(GameStateManager.MENU);
        }
        if(Level1.isClicked()){
            Play.level = 1;
            gsm.setState(GameStateManager.PLAY);
        }
        if(Level2.isClicked()){
            if(Play.levelunlocked[1]){
                Play.level = 2;
                gsm.setState(GameStateManager.PLAY);
            }
        }
        if(Level3.isClicked()){
            if(Play.levelunlocked[2]){
                Play.level = 3;
                gsm.setState(GameStateManager.PLAY);
            }
        }
        if(Level4.isClicked()){
            if(Play.levelunlocked[3]){
                Play.level = 4;
                gsm.setState(GameStateManager.PLAY);
            }
        }
    }

    public void update(float dt) {

        handleInput();
        Level1.update(dt);
        Level2.update(dt);
        Level3.update(dt);
        Level4.update(dt);
        escape.update(dt);
    }

    public void render() {

        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(reg, 0, 0);
        if(!Play.levelunlocked[0])lock1.draw(sb);
        if(!Play.levelunlocked[1])lock2.draw(sb);
        if(!Play.levelunlocked[2])lock3.draw(sb);
        if(!Play.levelunlocked[3])lock4.draw(sb);
        sb.end();

        Level1.render(sb);
        Level2.render(sb);
        Level3.render(sb);
        Level4.render(sb);
        escape.render(sb);



    }

    @Override
    public void dispose() {

    }
}