package com.mygdx.wanderingpaw.handlers;

import com.badlogic.gdx.physics.box2d.*;

public class CustomizedContactListener implements ContactListener {

    private boolean  playerOnGround;


    @Override
    public void beginContact(Contact contact) {
        //two fixtures start to collide
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        //player on the ground

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround = true;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround = true;
        }

    }

    @Override
    public void endContact(Contact contact) {
        //two fixtures no longer collide

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        //no longer on ground

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround = false;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround = false;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isPlayerOnGround(){
        // checks if player, mainly foot is on ground or not
        return playerOnGround;
    }
}
