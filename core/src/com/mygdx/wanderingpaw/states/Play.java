package com.mygdx.wanderingpaw.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.wanderingpaw.entities.Butterfly;
import com.mygdx.wanderingpaw.entities.Catnip;
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
    private Array<Catnip> catnips;
    private Array<Butterfly> butterflies;
    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private float tileSize;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Background[] backgrounds;

    private HUD hud;
    private int JumpCounter = 0;

    public static int level;

    public static boolean[] levelunlocked = new boolean[4];

    public Play(GameStateManager gsm) {

        super(gsm);

        //set up box2d world and contact listener
        world = new World(new Vector2(0, -25f), true);
        contactListener = new CustomizedContactListener();
        world.setContactListener(contactListener);
        b2dr = new Box2DDebugRenderer();

        //create player
        createPlayer();

        createTiles();
        ((BoundedCamera) cam).setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);

        // create catnip
        createCatnip();
        createButterfly();

        //create backgrounds
        TextureRegion sky = new TextureRegion(Game.res.getTexture("sky-image"), 0, 0, 1280, 720);
        TextureRegion fence = new TextureRegion(Game.res.getTexture("fence-image"), 0, 0, 320, 240);
        backgrounds = new Background[1];
        backgrounds[0] = new Background(sky, cam, 0f);

        //create hud
        hud = new HUD(player);

        // set up box2d cam
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
        b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);

        //level done initialization
/*        for(int i=1; i<3; i++){
            levelunlocked[i] = false;
        }*/
        Save.load();
        levelunlocked = Save.gd.getlevelUnlocked();
    }

    /**
     * Sets up the tile map collidable tiles.
     * Reads in tile map layers and sets up box2d bodies.
     */
    private void createTiles() {
        TiledMapTileLayer layer;
        //TileMap

        //tileMap = new TmxMapLoader().load("res/images/Test1.tmx");
        if (level == 1) {
            tileMap = new TmxMapLoader().load("res/images/This is level 1.tmx"); // grass
            tileMapWidth = Integer.parseInt(tileMap.getProperties().get("width").toString());
            tileMapHeight = Integer.parseInt(tileMap.getProperties().get("height").toString());
            tileSize = Integer.parseInt(tileMap.getProperties().get("tilewidth").toString());
            tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
            layer = (TiledMapTileLayer) tileMap.getLayers().get("Tile Layer 1");
            createLayers(layer, B2DVars.BIT_GROUND);
        } else if (level == 2) {
            tileMap = new TmxMapLoader().load("res/images/This is level 2.tmx"); // sand
            tileMapWidth = Integer.parseInt(tileMap.getProperties().get("width").toString());
            tileMapHeight = Integer.parseInt(tileMap.getProperties().get("height").toString());
            tileSize = Integer.parseInt(tileMap.getProperties().get("tilewidth").toString());
            tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
            layer = (TiledMapTileLayer) tileMap.getLayers().get("Tile Layer 1");
            createLayers(layer, B2DVars.BIT_GROUND);
        } else if (level == 3) {
            tileMap = new TmxMapLoader().load("res/images/This is level 3.tmx"); //ice
            tileMapWidth = Integer.parseInt(tileMap.getProperties().get("width").toString());
            tileMapHeight = Integer.parseInt(tileMap.getProperties().get("height").toString());
            tileSize = Integer.parseInt(tileMap.getProperties().get("tilewidth").toString());
            tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
            layer = (TiledMapTileLayer) tileMap.getLayers().get("Tile Layer 1");
            createLayers(layer, B2DVars.BIT_GROUND);
        } else if (level == 4) {
            tileMap = new TmxMapLoader().load("res/images/This is level 4.tmx"); // mountain
            tileMapWidth = Integer.parseInt(tileMap.getProperties().get("width").toString());
            tileMapHeight = Integer.parseInt(tileMap.getProperties().get("height").toString());
            tileSize = Integer.parseInt(tileMap.getProperties().get("tilewidth").toString());
            tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
            layer = (TiledMapTileLayer) tileMap.getLayers().get("Tile Layer 1");
            createLayers(layer, B2DVars.BIT_GROUND);
        }


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
        bdef.position.set(60 * 3 / PPM, 120 * 3 / PPM);
        bdef.fixedRotation = true;
        //bdef.linearVelocity.set(1, 0);


        // create body from bodydef
        Body body = world.createBody(bdef);

        // create box shape for player collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(13 * 3 / PPM, 13 * 3 / PPM);

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
        shape.setAsBox(13 * 3 / PPM, 2 * 3 / PPM, new Vector2(0, -13 * 3 / PPM), 0);

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

    private void createCatnip() {
        catnips = new Array<Catnip>();
        MapLayer layer = tileMap.getLayers().get("catnips");
        if (layer == null) return;

        for (MapObject mapObject : layer.getObjects()) {
            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            float x = Float.parseFloat(mapObject.getProperties().get("x").toString()) / PPM;
            float y = Float.parseFloat(mapObject.getProperties().get("y").toString()) / PPM;
            bdef.position.set(x, y);
            Body body = world.createBody(bdef);

            CircleShape cshape = new CircleShape();
            cshape.setRadius(16 / PPM);
            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = B2DVars.BIT_CATNIP;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(fdef).setUserData("catnip");

            Catnip c = new Catnip(body);
            body.setUserData(c);
            catnips.add(c);
            cshape.dispose();
        }

    }

    private void createButterfly() {
        butterflies = new Array<Butterfly>();
        MapLayer layer = tileMap.getLayers().get("butterflies");
        if (layer == null) return;

        for (MapObject mapObject : layer.getObjects()) {
            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            float x = Float.parseFloat(mapObject.getProperties().get("x").toString()) / PPM;
            float y = Float.parseFloat(mapObject.getProperties().get("y").toString()) / PPM;
            bdef.position.set(x, y);
            Body body = world.createBody(bdef);

            CircleShape cshape = new CircleShape();
            cshape.setRadius(48 / PPM);
            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = B2DVars.BIT_BUTTERFLY;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(fdef).setUserData("butterfly");

            Butterfly b = new Butterfly(body);
            body.setUserData(b);
            butterflies.add(b);
            cshape.dispose();
        }

    }

    private void renderBackground() {

    }


    public void handleInput() {

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.getBody().setLinearVelocity(2, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.getBody().setLinearVelocity(-2, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && JumpCounter < 2) {
            float force = player.getBody().getMass() * 8;
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyLinearImpulse(new Vector2(0, force), player.getBody().getPosition(), true);
            JumpCounter++;
        }
        if (player.getBody().getLinearVelocity().y == 0) {
            JumpCounter = 0;
        }
    }

    public void update(float dt) {
        //check input
        handleInput();


        // update box2d world
        world.step(Game.STEP, 1, 1);

        //remove catnip and butterfly
        Array<Body> bodies = contactListener.getBodiesToRemove();
        Array<Body> bodies2 = contactListener.getBodiesToRemove2();


        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            catnips.removeValue((Catnip) b.getUserData(), true);
            world.destroyBody(bodies.get(i));
        }
        bodies.clear();
        for (int i = 0; i < bodies2.size; i++) {
            Body b = bodies2.get(i);
            butterflies.removeValue((Butterfly) b.getUserData(), true);
            world.destroyBody(bodies2.get(i));
        }
        bodies2.clear();

        //update player
        this.player.update(dt);

        // update catnips
        for (int i = 0; i < catnips.size; i++) {
            catnips.get(i).update(dt);
        }

        //update butterflieswd
        for (int j = 0; j < butterflies.size; j++) {
            catnips.get(j).update(dt);
        }


        // check player win
        if (player.getBody().getPosition().x * B2DVars.PPM > tileMapWidth * tileSize) {

            //levelunlocked[level] = true;
            Save.gd.setlevelUnlocked(level);
            if (level == 3) {
                Save.gd.init();
                Save.save();
            }
            Save.save();
            gsm.setState(GameStateManager.LEVEL_SELECT);
        }

    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // camera follow player
        ((BoundedCamera) cam).setPosition(player.getPosition().x * PPM + Game.V_WIDTH / 4, Game.V_HEIGHT / 2);
        cam.update();

        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for (Background background : backgrounds) {
            background.render(sb);
        }
        // draw tilemap
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();
        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        //draw catnip
        for (int i = 0; i < catnips.size; i++) {
            catnips.get(i).render(sb);
        }

        //draw butterflies
        for (int j = 0; j < butterflies.size; j++) {
            butterflies.get(j).render(sb);
        }

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