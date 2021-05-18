package net.davidbrowne.furyofrome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Models.Script;
import net.davidbrowne.furyofrome.Tools.TransitionScreen;

import java.util.ArrayList;
import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class CutsceneScreen implements Screen {
    private Game game;
    private int V_WIDTH=1600,V_HEIGHT=1200;
    private AssetManager manager;
    private Viewport viewport;
    private Texture background;
    private ArrayList<Script> scriptList,scripts;
    private Texture logo;
    public Stage stage;
    private Preferences prefs;
    private int logoId;
    private Skin mySkin;
    private boolean next,finished;
    private Label text;
    private int sceneId;
    private String testString;
    private int scriptIterator,tempIterator;
    private Image logoImg;
    private Window window,window2;
    private boolean transition=false;
    private Button button,button2,button3;

    public CutsceneScreen(final Game game, final AssetManager manager, final int sceneId){
        game.music.stop();
        this.game=game;
        this.manager=manager;
        this.sceneId=sceneId;
        scriptList= json.fromJson(ArrayList.class, Script.class, Gdx.files.internal("scripts/script.json"));
        dialogLoading();
        tempIterator=0;
        logo = new Texture(Gdx.files.internal("portraits/NPC2.png"));
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(V_WIDTH,V_HEIGHT,new OrthographicCamera());
        Gdx.input.setInputProcessor(stage);
        prefs = Gdx.app.getPreferences("ValhallaGamePrefs");
        if(sceneId >= prefs.getInteger("level")){
            prefs.putInteger("level",sceneId);
        }
        stage = new Stage(viewport,game.batch);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        window = new Window("",mySkin);
        window2 = new Window("",mySkin);
        text = new Label(String.format("test"), mySkin);
        text.setFontScale(1.6f);
        logoImg = new Image(logo);
        updateDialog();
        textUpdate();
        window2.add(logoImg);
        window.add(window2);
        window.add(text);
        // Text Button
        button = new TextButton("Next",mySkin,"default");
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(game.getScreen().getClass()==CutsceneScreen.class){
                if(finished){
                    if(sceneId==21){
                        game.setScreen(new TransitionScreen(game.getScreen(),new EndScreen(game,manager),game));
                    }else{
                        game.setScreen(new TransitionScreen(game.getScreen(),new PlayScreen(game,manager,sceneId),game));}
                    transition=true;
                }
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                next=true;
                }
                return true;
            }
        });
        button2 = new TextButton("Skip",mySkin,"default");
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(game.getScreen().getClass()==CutsceneScreen.class){
                    if(sceneId==21){
                        game.setScreen(new TransitionScreen(game.getScreen(),new EndScreen(game,manager),game));
                    }else{
                game.setScreen(new TransitionScreen(game.getScreen(),new PlayScreen(game,manager,sceneId),game));}
                transition=true;}
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });
        button3 = new TextButton("Exit To Menu",mySkin,"default");
        button3.setTransform(true);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(game.getScreen().getClass()==CutsceneScreen.class){
                    game.setScreen(new MainMenuScreen(game,manager));
                    transition=true;}
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });
        table.add(window).padTop(150);
        table.row();
        table.add(button).padTop(200).center().expandX();
        table.row();
        table.add(button2).padTop(25).expandX();
        table.row();
        table.add(button3).padTop(25).expandX();
        table.row();
        stage.addActor(table);
        background=new Texture(Gdx.files.internal("background.png"));
        window2.setVisible(false);
        window.setVisible(false);
        button.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
    }

    public void dialogLoading(){
        scripts = new ArrayList<Script>();
        for(int i=0;i<scriptList.size();i++){
            if(scriptList.get(i).id==sceneId){
               scripts.add(scriptList.get(i));
            }
        }
        scriptIterator=scripts.size();
    }
    public void updateDialog(){
        if(tempIterator<=(scriptIterator-1)){
                testString=scripts.get(tempIterator).line;
                logoId=scripts.get(tempIterator).logo;
        }else{
            finished=true;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0, V_WIDTH,V_HEIGHT);
        game.batch.end();
        stage.draw();
    }
    public void textUpdate(){
        text.setText(String.format(testString));
        background=new Texture(Gdx.files.internal("background.png"));
        logo = new Texture(Gdx.files.internal("portraits/NPC"+scripts.get(tempIterator).logo+".png"));
        logoImg.setDrawable(new TextureRegionDrawable(logo));
        if (tempIterator < scriptIterator) {
            if(!finished)
                tempIterator++;
        }
        updateDialog();

    }

    public void update(){
        if(game.getScreen().getClass()==CutsceneScreen.class && !transition){
            window2.setVisible(true);
            window.setVisible(true);
            button.setVisible(true);
            button2.setVisible(true);
            button3.setVisible(true);
        }
        if(!finished) {
            updateDialog();
            if (next) {
                textUpdate();
                next = false;
            }
        }
         if(transition){
            window2.setVisible(false);
            window.setVisible(false);
            button.setVisible(false);
            button2.setVisible(false);
            button3.setVisible(false);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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
    }
}


