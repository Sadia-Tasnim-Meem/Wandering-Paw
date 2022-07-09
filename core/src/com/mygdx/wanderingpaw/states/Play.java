package com.mygdx.wanderingpaw.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.wanderingpaw.entities.HUD;
import com.mygdx.wanderingpaw.entities.Player;
import com.mygdx.wanderingpaw.handlers.*;
import com.mygdx.wanderingpaw.main.Game;

import static com.mygdx.wanderingpaw.handlers.B2DVars.PPM;

public class Play extends GameState {
    public boolean debug = false;
    private World world;
    private Box2DDebugRenderer b2dr;
    private CustomizedContactListener contactListener;

    private BoundedCamera b2dCam;
    private Player player;
    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private float tileSize;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private HUD hud;

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

        ((BoundedCamera) cam).setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);

        //create backgrounds
        Texture bgs = Game.res.getTexture("background-image.png");

//        BodyDef bdef = new BodyDef();
//        FixtureDef fdef = new FixtureDef();
//        PolygonShape shape = new PolygonShape();

        //create hud
        hud = new HUD(player);

        // set up box2d cam
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
        b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);

    }

    /**
     * Sets up the tile map collidable tiles.
     * Reads in tile map layers and sets up box2d bodies.
     */
    private void createTiles() {
        //TileMap

        tileMap = new TmxMapLoader().load("res/images/tile with grass.tmx");
        tileMapWidth = Integer.parseInt(tileMap.getProperties().get("width").toString());
        tileMapHeight = Integer.parseInt(tileMap.getProperties().get("height").toString());
        tileSize = Integer.parseInt(tileMap.getProperties().get("tilewidth").toString());
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        //tileSize = (int) tileMap.getProperties().get("tilewidth");

        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("Tile Layer 1");
        createLayers(layer, B2DVars.BIT_GROUND);
    }

    /**
     * Creates box2d bodies for all non-null tiles
     * in the specified layer and assigns the specified
     * category bits.
     *
     * @param layer the layer being read
     * @param bits  category bits assigned to fixtures
     */
    private void createLayers(TiledMapTileLayer layer, short bits) {
        //tilesize
        tileSize = layer.getTileWidth();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        // go through all cells in layer
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
                //fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);
                chainShape.dispose();

            }
        }
    }

    /**
     * Creates the player.
     * Sets up the box2d body and sprites.
     */
    public void createPlayer() {
        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(60 / PPM, 120 / PPM);
        bdef.fixedRotation = true;
        //bdef.linearVelocity.set(1, 0);


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

    private void renderBackground(){

    }

    public void handleInput() {
        // player jump

        if (CustomizedInput.isPressed(CustomizedInput.BUTTON1)) {
            if (contactListener.isPlayerOnGround()) {
                player.getBody().applyForceToCenter(0, 160, true);
            }
        }

        if (CustomizedInput.isPressed(CustomizedInput.BUTTON3)) {

            player.getBody().applyForceToCenter(50, 0, true);

//            if (player.getBody().getLinearVelocity().equals(new Vector2(new Vector2(-1, 0))))
//                player.getBody().setLinearVelocity(new Vector2(new Vector2(0, 0)));
//
//            else if (player.getBody().getLinearVelocity().equals(new Vector2(new Vector2(-1, 1))))
//                player.getBody().setLinearVelocity(new Vector2(new Vector2(0, 0)));
//
//            else if (player.getBody().getLinearVelocity().equals(new Vector2(new Vector2(0, 0))))
//                player.getBody().setLinearVelocity(new Vector2(new Vector2(1, 0)));
//
//            else if (player.getBody().getLinearVelocity().equals(new Vector2(new Vector2(0, 1))))
//                player.getBody().setLinearVelocity(new Vector2(new Vector2(1, 0)));

        }

        if (CustomizedInput.isPressed(CustomizedInput.BUTTON4)) {

            player.getBody().applyForceToCenter(-50, 0, true);
//            if (player.getBody().getLinearVelocity().equals(new Vector2(new Vector2(0, 0))))
//                player.getBody().setLinearVelocity(new Vector2(new Vector2(-1, 0)));
//            else if (player.getBody().getLinearVelocity().equals(new Vector2(new Vector2(0, 1))))
//                player.getBody().setLinearVelocity(new Vector2(new Vector2(-1, 0)));
//
//            else if (player.getBody().getLinearVelocity().equals(new Vector2(new Vector2(1, 0))))
//                player.getBody().setLinearVelocity(new Vector2(new Vector2(0, 0)));
//
//            else if (player.getBody().getLinearVelocity().equals(new Vector2(new Vector2(1, 1))))
//                player.getBody().setLinearVelocity(new Vector2(new Vector2(0, 0)));

        }

    }

    public void update(float dt) {
        //check input
        handleInput();

        //world.step(dt, 6, 2);

        // update box2d world
        world.step(Game.STEP, 1, 1);

        //update player
        this.player.update(dt);


    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // camera follow player
        ((BoundedCamera) cam).setPosition(player.getPosition().x * PPM + Game.V_WIDTH / 4, Game.V_HEIGHT / 2);
        cam.update();
        // draw tilemap
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();
        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);
        // draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);


        // debug draw box2d
        if (debug) {
            b2dCam.setPosition(player.getPosition().x + Game.V_WIDTH / 4 / PPM, Game.V_HEIGHT / 2 / PPM);
            b2dCam.update();
            b2dr.render(world, b2dCam.combined);
        }

    }

    public void dispose() {

    }


}