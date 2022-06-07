package com.mygdx.wanderingpaw.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.wanderingpaw.entities.Player;
import com.mygdx.wanderingpaw.handlers.*;
import com.mygdx.wanderingpaw.main.Game;

import static com.mygdx.wanderingpaw.handlers.B2DVars.PPM;

public class Play extends GameState {
    private World world;
    private Box2DDebugRenderer b2dr;
    private BoundedCamera b2dCam;
    private CustomizedContactListener contactListener;

    private TiledMap tileMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
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

        createTiles();

        //create backgrounds
        Texture bgs = Game.res.getTexture("background-image.png");

        //PLATFORM

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // PLAYER


        //create foot sensor
/*        shape.setAsBox(2/ PPM, 2 / PPM, new Vector2(0, -5 /PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");*/


        // set up box2d cam
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

        /////////////////

        //going through all the cells in the layer


    }

    private void createTiles() {
        //TileMap

        tileMap = new TmxMapLoader().load("res/images/untitled.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        tileSize = (int) tileMap.getProperties().get("tilewidth");

        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("Layer");
        createLayers(layer, B2DVars.BIT_GROUND);
    }

    private void createLayers(TiledMapTileLayer layer, short bits) {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                //getting cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                //check if cells exist
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                //creating a body-fixture for a valid cell
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(
                        (col + 0.5f) * tileSize / PPM,
                        (row + 0.5f) * tileSize / PPM);

                ChainShape chainShape = new ChainShape();
                Vector2[] vector2s = new Vector2[3];

                vector2s[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
                vector2s[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
                vector2s[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);

                chainShape.createChain(vector2s);
                fdef.friction = 0;
                fdef.shape = chainShape;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);


            }
        }
    }

    public void createPlayer() {
        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(60 / PPM, 120 / PPM);
        bdef.fixedRotation = true;
        bdef.linearVelocity.set(1, 0);


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
        shape.setAsBox(13 / PPM, 2 / PPM, new Vector2(0, -13 / PPM), 0);

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

        if (CustomizedInput.isPressed(CustomizedInput.BUTTON1)) {
            if (contactListener.isPlayerOnGround()) {
                player.getBody().applyForceToCenter(0, 160, true);
            }

        }

    }

    public void update(float dt) {

        handleInput();
        world.step(dt, 6, 2);
        player.update(dt);

    }

    public void render() {


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();

        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        b2dr.render(world, cam.combined);

//        //camera following player
//
//        b2dCam.setPosition(player.getPosition().x * PPM + Game.V_WIDTH / 4, Game.V_HEIGHT / 2);
//        b2dCam.update();
    }

    public void dispose() {

    }


}
