package net.davidbrowne.furyofrome.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.furyofrome.Events.EventLoader;
import net.davidbrowne.furyofrome.Models.Script;
import net.davidbrowne.furyofrome.Screens.PlayScreen;
import net.davidbrowne.furyofrome.Sprites.FriendlyNPC;

import java.util.ArrayList;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;


public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private int V_WIDTH=1600,V_HEIGHT=1300;
    public static int score=0;
    private Preferences prefs;
    private int logoId;
    private ArrayList<Script> scriptList,scripts;
    private static Label scoreLabel;
    private Label spikeLabel;
    private Skin mySkin;
    private int scriptIterator,tempIterator;
    private boolean next,finished;
    private PlayScreen screen;
    private Label text;
    private Window window,window2;
    private Texture logo;
    private Image logoImg;
    private int scriptId=1;
    private FriendlyNPC npc;
    private String testString;
    private EventLoader eventLoader;
    public Hud(SpriteBatch sb, PlayScreen screen){
        this.screen=screen;
        eventLoader = new EventLoader();
        prefs = Gdx.app.getPreferences("RomeGamePrefs");
        score = prefs.getInteger("score");
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        scriptList= json.fromJson(ArrayList.class, Script.class, Gdx.files.internal("scripts/script.json"));
        viewport = new StretchViewport(V_WIDTH, V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);
        dialogLoading();
        updateDialog();
        logo =new Texture(Gdx.files.internal("npcs/npc"+logoId+".png"));
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        window = new Window("",mySkin);
        window2 = new Window("",mySkin);
        text = new Label(String.format("test"), mySkin);
        text.setFontScale(1.6f);
        scoreLabel = new Label(String.format("Gold: %04d",screen.getPlayer().getGold()), mySkin);
        scoreLabel.setFontScale(1.5f);
        spikeLabel = new Label(String.format("Spears: %03d", screen.getPlayer().getSpears()), mySkin);
        spikeLabel.setFontScale(1.5f);
        table.add(scoreLabel).center().padTop(100).padRight(200);
        table.add(spikeLabel).center().padTop(100);
        Table table1 = new Table();
        table1.top();
        table1.setFillParent(true);
        logoImg = new Image(logo);
        window2.add(logoImg);
        window.add(window2);
        window.add(text);
        table1.add(window).center().padTop(100);
        stage.addActor(table);
        stage.addActor(table1);
        window.setVisible(false);

    }
    public void flipVisibilityDialogWindow(){
        if(window.isVisible()){
            window.setVisible(false);
            if(npc.getLevel()!=2)
                npc.setLevel(2);
            screen.setPaused(false);
        }
        else{
            window.setVisible(true);

        }
    }

    public void loadScript(int scriptId){
        this.scriptId=scriptId;
        dialogLoading();
    }


    public void dialogLoading(){
        scripts = new ArrayList<Script>();
        for(int i=0;i<scriptList.size();i++){
            if(scriptList.get(i).id==scriptId){
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

    public void textUpdate(){
        text.setText(String.format(testString));
        logoImg.setDrawable(new TextureRegionDrawable(logo));
        if (tempIterator < scriptIterator) {
            if(!finished)
                tempIterator++;
        }
        updateDialog();

    }

    public Viewport getViewport() {
        return viewport;
    }

    public void update(float dt){
        spikeLabel.setText(String.format("Spears: %03d", screen.getPlayer().getSpears()));
        scoreLabel.setText(String.format("Gold: %04d", screen.getPlayer().getGold()));
        text.setText(testString);
        if(!finished) {
            updateDialog();
            if (next) {
                textUpdate();
                next = false;
            }
        }
        if(window.isVisible()&&finished){
            flipVisibilityDialogWindow();
            for(int i=0;i<eventLoader.getEvents().size();i++){
                if(eventLoader.getEvents().get(i).getEventId()==scriptId){
                    eventLoader.getEvents().get(i).run(screen);
                }
            }
        }
    }

    public Window getWindow() {
        return window;
    }

    @Override
    public void dispose() {
        logo.dispose();
        stage.dispose();
    }

    public void updateDialogWindow(int scriptId, FriendlyNPC npc) {
        this.npc=npc;

        if(!window.isVisible()){
            finished=false;
            tempIterator=0;
            loadScript(scriptId);
            updateDialog();
            logoId=scripts.get(tempIterator).logo;
            logo =new Texture(Gdx.files.internal("npcs/NPC"+logoId+".png"));
            logoImg.setDrawable(new TextureRegionDrawable(logo));
            flipVisibilityDialogWindow();
        }else{
            next=true;
        }


    }
}
