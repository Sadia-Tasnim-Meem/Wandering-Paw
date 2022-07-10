package com.mygdx.wanderingpaw.handlers;

import com.mygdx.wanderingpaw.main.Game;
import com.mygdx.wanderingpaw.states.GameState;
import com.mygdx.wanderingpaw.states.LevelSelect;
import com.mygdx.wanderingpaw.states.Menu;
import com.mygdx.wanderingpaw.states.Play;

import java.util.Stack;

public class GameStateManager {
    private Game game;

    private Stack<GameState> gameStates;
    public static final int MENU = 83774392;
    public static final int PLAY = 912837;
    public static final int LEVEL_SELECT = 9238732;


    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(MENU);

    }

    public Game game() {
        return game;
    }

    public void update(float dt) {
        gameStates.peek().update(dt);

    }

    public void render() {
        gameStates.peek().render();

    }

    private GameState getState(int state){
        if(state == MENU)return new Menu(this);
        if(state == PLAY) return new Play(this);
        if(state == LEVEL_SELECT){
            return new LevelSelect(this);
        }
        return null;
    }

    public void setState(int state){
        popState();
        pushState(state);
    }

    public void pushState(int state){

        gameStates.push(getState(state));
    }

    public void popState(){
        GameState g = gameStates.pop();
        g.dispose();
    }

}
