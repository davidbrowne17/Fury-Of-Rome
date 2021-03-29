package net.davidbrowne.furyofrome.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.furyofrome.Screens.PlayScreen;


public class Hud implements Disposable {
    private int width;
    private final int totalHealth=100;
    private final int totalBarWidth=50;
    private Texture heart;
    private Texture emptyHeart;
    public Stage stage;
    private Viewport viewport;
    private int V_WIDTH=600,V_HEIGHT=300;
    public static int score=0;
    private Preferences prefs;
    private static Label scoreLabel;
    private Label spikeLabel;
    private Skin mySkin;
    private PlayScreen screen;

    public Hud(SpriteBatch sb, PlayScreen screen){
        this.screen=screen;
        prefs = Gdx.app.getPreferences("RomeGamePrefs");
        score = prefs.getInteger("score");
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new FitViewport(V_WIDTH, V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        scoreLabel = new Label(String.format("Gold: %04d",screen.getPlayer().getGold()), mySkin);
        scoreLabel.setFontScale(0.5f);
        spikeLabel = new Label(String.format("Spears: %03d", screen.getPlayer().getSpears()), mySkin);
        spikeLabel.setFontScale(0.5f);
        table.add(scoreLabel).center().padTop(10).padRight(20);
        table.add(spikeLabel).center().padTop(10);
        stage.addActor(table);

    }

    public void update(float dt){
        spikeLabel.setText(String.format("Spears: %03d", screen.getPlayer().getSpears()));
        scoreLabel.setText(String.format("Gold: %04d", screen.getPlayer().getGold()));

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
