package net.davidbrowne.furyofrome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Tools.TransitionScreen;


public class MainMenuScreen implements Screen {
    private Game game;
    private int V_WIDTH=1600,V_HEIGHT=1200;
    private int LOGO_W=400,LOGO_H=400;
    private AssetManager manager;
    private Viewport viewport;
    private int level;
    private Texture background;
    private Texture logo;
    public Stage stage;
    private Preferences prefs;
    private Skin mySkin;
    private OrthographicCamera cam;
    public MainMenuScreen(final Game game, final AssetManager manager){
        this.game=game;
        this.manager=manager;
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        cam = new OrthographicCamera();
        viewport = new StretchViewport(V_WIDTH,V_HEIGHT,cam);
        Gdx.input.setInputProcessor(stage);
        logo =new Texture(Gdx.files.internal("logo.png"));
        prefs = Gdx.app.getPreferences("FuryOfRomeGamePrefs");
        level = prefs.getInteger("level");
        background=new Texture(Gdx.files.internal("background.png"));
        stage = new Stage(viewport,game.batch);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // Text Button
        Button button = new TextButton("Continue Game",mySkin,"default");
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(level!=0){
                game.setScreen(new TransitionScreen(game.getScreen(),new PlayScreen(game,manager,level),game));
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                dispose();
                }
                return true;
            }
        });
        // Text Button
        Button button1 = new TextButton("New Game",mySkin,"default");
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, final int pointer, int button) {
                if(level<=1){
                game.setScreen(new TransitionScreen(game.getScreen(),new PlayScreen(game,manager,1),game));
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                dispose();

                }
                else{
                   Dialog dialog = new Dialog("", mySkin) {
                       @Override
                       protected void result(Object object) {
                           if ((Boolean) object) {
                               //accept
                               game.setScreen(new TransitionScreen(game.getScreen(),new PlayScreen(game,manager,1),game));
                               manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                               dispose();
                           }else{
                               //rejected
                               //do nothing
                           }
                       }
                   };
                   dialog.text("Starting a new game will delete your previous save. Are you sure?");
                   dialog.button("New Game",true);
                   dialog.button("Cancel",false);
                   dialog.show(stage);
                }
                return true;
            }
        });
        Button button2 = new TextButton("Settings",mySkin,"default");
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game,manager));
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                dispose();
                return true;
            }
        });
        Button button3 = new TextButton("Controls",mySkin,"default");
        button3.setTransform(true);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ControlsScreen(game,manager));
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                dispose();
                return true;
            }
        });

        Button button4 = new TextButton("Exit",mySkin,"default");
        button4.setTransform(true);
        button4.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                Gdx.app.exit();
                return true;
            }
        });

        table.add(button).center().padTop(500).expandX();
        table.row();
        table.add(button1).center().padTop(25).expandX();
        table.row();
        table.add(button2).center().padTop(25).expandX();
        table.row();
        table.add(button3).center().padTop(25).expandX();
        table.row();
        table.add(button4).center().padTop(25).expandX();
        stage.addActor(table);
/*
        game.music = manager.get("audio/music/music1.wav", Music.class);
        if(game.getVolume()!=0){
            game.music.setLooping(true);
            game.music.setVolume(game.getVolume());
            game.music.play();
        }
*/
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0, V_WIDTH,V_HEIGHT);
        game.batch.draw(logo,(V_WIDTH/2)-LOGO_W/2,V_HEIGHT-(V_HEIGHT/3+(V_HEIGHT/13)), LOGO_W,LOGO_H);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        background.dispose();
        mySkin.dispose();
        logo.dispose();
    }
}
