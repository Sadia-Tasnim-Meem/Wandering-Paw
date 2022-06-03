package com.mygdx.wanderingpaw.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class CustomizedInputProcessor extends InputAdapter {


    //key pressed
    public boolean keyDown(int k) {

        if (k == Input.Keys.Z) {
            CustomizedInput.setKey(CustomizedInput.BUTTON1, true);
        }

        if (k == Input.Keys.X) {
            CustomizedInput.setKey(CustomizedInput.BUTTON2, true);
        }
        return true;
    }


    //key released
    public boolean keyUp(int k) {

        if (k == Input.Keys.Z) {
            CustomizedInput.setKey(CustomizedInput.BUTTON1, false);
        }

        if (k == Input.Keys.X) {
            CustomizedInput.setKey(CustomizedInput.BUTTON2, false);
        }

        return true;
    }


}
