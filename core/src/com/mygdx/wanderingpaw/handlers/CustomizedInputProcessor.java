package com.mygdx.wanderingpaw.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class CustomizedInputProcessor extends InputAdapter {


    //key pressed
    public boolean keyDown(int k) {

        if (k == Input.Keys.W) {
            CustomizedInput.setKey(CustomizedInput.BUTTON1, true);
        }

        if (k == Input.Keys.S) {
            CustomizedInput.setKey(CustomizedInput.BUTTON2, true);
        }

        if (k == Input.Keys.D) {
            CustomizedInput.setKey(CustomizedInput.BUTTON3, true);
        }

        if (k == Input.Keys.A) {
            CustomizedInput.setKey(CustomizedInput.BUTTON4, true);
        }

        return true;
    }


    //key released
    public boolean keyUp(int k) {

        if (k == Input.Keys.W) {
            CustomizedInput.setKey(CustomizedInput.BUTTON1, false);
        }

        if (k == Input.Keys.S) {
            CustomizedInput.setKey(CustomizedInput.BUTTON2, false);
        }

        if (k == Input.Keys.D) {
            CustomizedInput.setKey(CustomizedInput.BUTTON3, false);
        }

        if (k == Input.Keys.A) {
            CustomizedInput.setKey(CustomizedInput.BUTTON4, false);
        }

        return true;
    }


}
