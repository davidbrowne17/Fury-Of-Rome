package net.davidbrowne.furyofrome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Items.Item;
import net.davidbrowne.furyofrome.Items.ItemDef;
import net.davidbrowne.furyofrome.Items.Bullet;
import net.davidbrowne.furyofrome.Sprites.Enemy;
import net.davidbrowne.furyofrome.Sprites.Player;
import net.davidbrowne.furyofrome.Tools.B2WorldCreator;
import net.davidbrowne.furyofrome.Tools.Controller;
import net.davidbrowne.furyofrome.Tools.FixedOrthogonalTiledMapRenderer;
import net.davidbrowne.furyofrome.Tools.TransitionScreen;
import net.davidbrowne.furyofrome.Tools.WorldContactListener;
import net.davidbrowne.furyofrome.Scenes.Hud;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {
    private Game game;
    private TextureAtlas atlas;
    private Viewport gamePort;
    private OrthographicCamera gamecam;
    private Hud hud;
    private TiledMap map;
    private Boolean paused=false;
    private FixedOrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;
    private Preferences prefs;
    private AssetManager manager;
    private Controller controller;
    private B2WorldCreator creator;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    static final float STEP_TIME = 1f/60f;
    float accumulator = 0;
    private int level;
    private boolean superPause=false;
    private boolean dispose;

    public PlayScreen(Game game, AssetManager manager,int level){
        //Gdx.input.setCursorCatched(true);
        prefs = Gdx.app.getPreferences("RomeGamePrefs");
        prefs.flush();
        atlas = new TextureAtlas("sprites/playerandenemies.atlas");
        this.game=game;
        this.manager=manager;
        controller = new Controller();
        player=new Player();
        gamecam = new OrthographicCamera();
        gamePort = new FillViewport(Game.V_WIDTH/ Game.PPM,Game.V_HEIGHT/ Game.PPM,gamecam);
        hud = new Hud(game.batch,this);
        this.level=level;
        TmxMapLoader maploader = new TmxMapLoader();
        map = maploader.load(("maps/level"+level+".tmx"));
        renderer = new FixedOrthogonalTiledMapRenderer(map,1/ Game.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
        //create  Box2D world setting no gravity in X -80 gravity in Y and allow bodies to sleep
        world = new World(new Vector2(0,-12),true);
        //box2D
        b2dr = new Box2DDebugRenderer();
        creator =new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());
        /*game.music = manager.get("audio/music/music2.wav",Music.class);
        if(game.getVolume()!=0){
            game.music.setLooping(true);
            game.music.setVolume(game.getVolume());
            game.music.play();
        }
        */
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
        game.resetInputProcessor();

    }

    public Hud getHud() {
        return hud;
    }

    public void setPlayer(Player player){
        this.player=player;
    }
    public Player getPlayer() { return player; }
    public TextureAtlas getAtlas(){
        return atlas;
    }
    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, 6, 2);
        }
    }

    public Game getGame() {
        return game;
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type== Bullet.class){
                items.add(new Bullet(this,idef.position.x,idef.position.y,!player.getRunningRight()));
            }
        }
        itemsToSpawn.clear();
    }

    public int getLevel() {
        return level;
    }

    public void handleInput(float dt){

        //PC CONTROLS
        if((controller.getaPressed() || Gdx.input.isKeyJustPressed(Input.Keys.W)) && !player.isAttacking()){
            player.attack();
        }
        else if((controller.getaPressed() || Gdx.input.isKeyJustPressed(Input.Keys.F)) && !player.isBlocking()){
            player.block();
        }
        else if ((controller.getbPressed()|| Gdx.input.isKeyJustPressed(Input.Keys.E)) && ((!player.isAttacking())&&(player.isCanFire()))){
            player.fire();
        }
        else if((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || controller.getUpPressed() )&&player.b2body.getLinearVelocity().y==0){
            player.jump();
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.D)|| controller.getRightPressed()) && !player.isAttacking() &&(player.b2body.getLinearVelocity().y==0)){
            player.b2body.setLinearVelocity(new Vector2(1.5f, player.b2body.getLinearVelocity().y));
            player.setRunningRight(true);
        }
        else if ((controller.getLeftPressed()|| Gdx.input.isKeyPressed(Input.Keys.A)) && player.b2body.getLinearVelocity().x >= -2 && !player.isAttacking()&&(player.b2body.getLinearVelocity().y==0)){
            player.b2body.setLinearVelocity(new Vector2(-1.5f, player.b2body.getLinearVelocity().y));
            player.setRunningRight(false);
        }



    }

    public void handlePause(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)){
            player.interact();
        }
    }
    public void handleSuperPause(){
        if((Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))){
            if(superPause)
                superPause=false;
            else
                superPause=true;
        }
    }

    public Boolean getPaused() {
        return paused;
    }

    public void setPaused(Boolean paused) {
        this.paused = paused;
    }

    public void update(float dt){
        if(!superPause) {
            if (!paused) {
                handleInput(dt);
            }
            stepWorld();
            handleSpawningItems();
            for (int i = 0; i < items.size; i++)
                items.get(i).update(dt);
            for (Enemy enemy : creator.getEnemies())
                enemy.update(dt);
            handlePause();
            hud.update(dt);
            player.update(dt);
            gamecam.update();
            gamecam.position.x = player.b2body.getPosition().x;
            renderer.setView(gamecam);
        }
        handleSuperPause();

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if(game.getScreen()==this) {
            update(delta);
            //clear screen with black
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            game.batch.begin();
            game.batch.setProjectionMatrix(gamecam.combined);
            renderer.render();
            for (Enemy enemy : creator.getEnemies()) {
                enemy.draw(game.batch);
                if (enemy.getX() < player.getX() + 230 / Game.PPM && !enemy.b2body.isActive())
                    enemy.b2body.setActive(true);
            }
            for (Item item : items)
                item.draw(game.batch);
            player.draw(game.batch);
            game.batch.end();
            hud.stage.draw();
            controller.draw();
            //b2d debug lines
            //b2dr.render(world, gamecam.combined);
            System.out.println("Y "+player.b2body.getPosition().y);
            if (player.b2body.getPosition().y < 0 - (player.getFrame(delta).getTexture().getHeight() / Game.PPM) / 2) {
                float delay = 0.02f; // seconds
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        //set game over screen
                        //game.setScreen(new GameOverScreen(game,manager,level));
                        //Gdx.input.setCursorCatched(false);
                        dispose=true;
                    }
                }, delay);
            } else if (Player.State.DEAD == player.currentState) {
                float delay = 0.2f; // seconds
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        //set game over screen
                        //game.setScreen(new GameOverScreen(game,manager,level));
                        //Gdx.input.setCursorCatched(false);
                        dispose=true;
                    }
                }, delay);
            }
            if (dispose)
                dispose();
        }
    }


    public void finishLevel(){
        player.setLevel(player.getLevel()+1);
        //game.music.stop();
        game.setScreen(new TransitionScreen(this,new PlayScreen(game,manager,player.getLevel()),game));
    }
    public TiledMap getMap(){
        return map;
    }

    public World getWorld() {
        return world;
    }
    public AssetManager getManager(){
        return manager;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
        hud.getViewport().update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
