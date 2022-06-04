package com.mygdx.wanderingpaw.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.wanderingpaw.entities.Player;
import com.mygdx.wanderingpaw.handlers.*;
import com.mygdx.wanderingpaw.main.Game;

import static com.mygdx.wanderingpaw.handlers.B2DVars.PPM;

public class Play extends GameState {
    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dCam;

    private Body playerBody;
    private CustomizedContactListener contactListener;

    private Player player;




    public Play(GameStateManager gsm) {

        super(gsm);

        //set up box2d world and contact listener
        world = new World(new Vector2(0, -9.81f), true);
        contactListener = new CustomizedContactListener();
        world.setContactListener(contactListener);
        b2dr = new Box2DDebugRenderer();

        //create player
        createPlayer();

        //create backgrounds
        Texture bgs = Game.res.getTexture("background-image.png");

        //PLATFORM

        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / PPM, 0 / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(300 / PPM, 5 / PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_GROUND;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        body.createFixture(fdef).setUserData("ground");

        // PLAYER



        //create foot sensor
/*        shape.setAsBox(2/ PPM, 2 / PPM, new Vector2(0, -5 /PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");*/



        // set up box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

    }

    public void createPlayer(){
        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(60 / PPM, 120 / PPM);
        bdef.fixedRotation = true;


        // create body from bodydef
        Body body = world.createBody(bdef);

        // create box shape for player collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(13 / PPM, 13 / PPM);

        // create fixturedef for player collision box
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1;
        fdef.friction = 0;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;


        // create player collision box fixture
        body.createFixture(fdef);
        shape.dispose();

        // create box shape for player foot
        shape = new PolygonShape();
        shape.setAsBox(13 / PPM, 3 / PPM, new Vector2(0, -13 / PPM), 0);

        // create fixturedef for player foot
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;

        // create player foot fixture
        body.createFixture(fdef).setUserData("foot");
        shape.dispose();

        // create new player
        player = new Player(body);
        body.setUserData(player);

        // final tweaks, manually set the player body mass to 1 kg
        MassData md = body.getMassData();
        md.mass = 1;
        body.setMassData(md);


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
