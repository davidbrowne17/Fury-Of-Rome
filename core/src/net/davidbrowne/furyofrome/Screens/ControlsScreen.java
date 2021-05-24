package net.davidbrowne.furyofrome.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.furyofrome.Game;

public class ControlsScreen implements Screen {
    private Game game;
    private int V_WIDTH=1600,V_HEIGHT=1200;
    private AssetManager manager;
    private Viewport viewport;
    private Texture background;
    public Stage stage;
    private Label label,label1,label2,label3,label4;
    private Skin mySkin;
    public ControlsScreen(final Game game, final AssetManager manager){
        this.game=game;
        this.manager=manager;
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(V_WIDTH,V_HEIGHT,new OrthographicCamera());
        Gdx.input.setInputProcessor(stage);
        background=new Texture(Gdx.files.internal("background.png"));
        stage = new Stage(viewport,game.batch);
        Window window = new Window(" ",mySkin);
        if(Gdx.app.getType()== Application.ApplicationType.Android){
            label = new Label(String.format(" A to attack "), mySkin);
            label1 = new Label(String.format(" B to throw Spear "), mySkin);
            label2 = new Label(String.format(" F to block "), mySkin);
            label3 = new Label(String.format(" Left and Right to move "), mySkin);
            label4 = new Label(String.format(" Up to jump "), mySkin);
        }
        else{
            label = new Label(String.format(" W to attack "), mySkin);
            label1 = new Label(String.format(" E to throw Spear "), mySkin);
            label2 = new Label(String.format(" F to block "), mySkin);
            label3 = new Label(String.format(" A and D to move "), mySkin);
            label4 = new Label(String.format(" Space to jump "), mySkin);
        }
        label.setFontScale(3.6f);
        label1.setFontScale(3.6f);
        label2.setFontScale(3.6f);
        label3.setFontScale(3.6f);
        label4.setFontScale(3.6f);
        window.add(label);
        window.row();
        window.add(label1);
        window.row();
        window.add(label2);
        window.row();
        window.add(label3);
        window.row();
        window.add(label4);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // Text Button
        Button button3 = new TextButton("Exit",mySkin,"default");
        button3.setTransform(true);
        button3.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game, manager));
                dispose();
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });
        table.add(window).center().padTop(200).expandX();
        table.row();
        table.add(button3).center().padTop(200).expandX();

        stage.addActor(table);

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

    public void update(){

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
