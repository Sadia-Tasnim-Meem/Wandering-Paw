package com.mygdx.wanderingpaw.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.wanderingpaw.states.Play;

public class CustomizedContactListener implements ContactListener {

    private int numFootContacts;
    private Array<Body> bodiesToRemove; // for catnips
    private Array<Body> bodiesToRemove2; // for butterflies

    private boolean catnip_received;
    private boolean butterfly_received;
    private boolean spike_collision;

    public CustomizedContactListener() {
        super();

        bodiesToRemove = new Array<Body>();
        bodiesToRemove2 = new Array<Body>();

    }


    @Override
    public void beginContact(Contact contact) {
        //two fixtures start to collide
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        //player on the ground

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
        }

        if (fa.getUserData() != null && fa.getUserData().equals("catnip")) {
            //remove catnip
            bodiesToRemove.add(fa.getBody());
            catnip_received = true;

        }

        if (fb.getUserData() != null && fb.getUserData().equals("catnip")) {
            //remove catnip
            bodiesToRemove.add(fb.getBody());
            catnip_received = true;

        }

        if (fa.getUserData() != null && fa.getUserData().equals("butterfly")) {
            //remove butterfly
            bodiesToRemove2.add(fa.getBody());
            butterfly_received = true;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("butterfly")) {
            //remove butterfly
            bodiesToRemove2.add(fb.getBody());
            butterfly_received = true;
        }
        if (fa.getUserData() != null && fa.getUserData().equals("spike")) {
            spike_collision = true;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("spike")) {
            spike_collision = true;
        }


    }

    @Override
    public void endContact(Contact contact) {
        //two fixtures no longer collide

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        //no longer on ground

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }

    }

    public boolean playerCanJump() {
        return numFootContacts >= 0;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isPlayerOnGround() {
        // checks if player, mainly foot is on ground or not
        return numFootContacts > 0;
    }

    public Array<Body> getBodiesToRemove() {
        return bodiesToRemove;
    }

    public Array<Body> getBodiesToRemove2() {
        return bodiesToRemove2;
    }

    public void update_scores()
    {
        if(catnip_received){
            catnip_received = false;
            Play.catnip_score++;
        }
        if(butterfly_received){
            butterfly_received = false;
            Play.butterfly_score++;
        }
        if(spike_collision){
            spike_collision = false;
            Play.lives--;
            if(Play.lives == 0)Play.playerDead = true;
        }
    }
}
