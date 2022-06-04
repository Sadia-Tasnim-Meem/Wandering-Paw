package com.mygdx.wanderingpaw.handlers;

import com.badlogic.gdx.physics.box2d.*;

public class CustomizedContactListener implements ContactListener {

    private int numFootContacts;


    @Override
    public void beginContact(Contact contact) {
        //two fixtures start to collide
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null)return;

        //player on the ground

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
        }

    }

    @Override
    public void endContact(Contact contact) {
        //two fixtures no longer collide

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null)return;

        //no longer on ground

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
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
        return numFootContacts > 0;
    }
}
