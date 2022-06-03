package com.mygdx.wanderingpaw.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.wanderingpaw.handlers.*;
import com.mygdx.wanderingpaw.main.Game;

import static com.mygdx.wanderingpaw.handlers.B2DVars.PPM;

public class Play extends GameState {
    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dCam;

    private Body playerBody;
    private CustomizedContactListener contactListener;



    public Play(GameStateManager gsm) {

        super(gsm);
        world = new World(new Vector2(0, -9.81f), true);

        contactListener = new CustomizedContactListener();
        world.setContactListener(contactListener);

        b2dr = new Box2DDebugRenderer();

        //PLATFORM

        BodyDef bdef = new BodyDef();
        bdef.position.set(160 / PPM, 120 / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 5 / PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_GROUND;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        body.createFixture(fdef).setUserData("ground");

        // PLAYER

        bdef.position.set(160 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bdef);

        shape.setAsBox(5 / PPM, 5 / PPM);
        fdef.shape = shape;
        fdef.restitution = 0.2f;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;
        playerBody.createFixture(fdef).setUserData("player");

        //create foot sensor
        shape.setAsBox(2/ PPM, 2 / PPM, new Vector2(0, -5 /PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");



        // set up box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

    }

    public void handleInput() {
        // player jump

        if(CustomizedInput.isPressed(CustomizedInput.BUTTON1)){
            if(contactListener.isPlayerOnGround()){
                playerBody.applyForceToCenter(0, 160, true );
            }

        }

    }

    public void update(float dt) {

        handleInput();
        world.step(dt, 6, 2);

    }

    public void render() {
// /*       sb.setProjectionMatrix(cam.combined);
//        sb.begin();
//        sb.end();*/

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        b2dr.render(world, b2dCam.combined);
    }

    public void dispose() {

    }


}
