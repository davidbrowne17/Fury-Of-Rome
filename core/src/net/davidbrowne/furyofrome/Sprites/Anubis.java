package net.davidbrowne.furyofrome.Sprites;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.EndScreen;
import net.davidbrowne.furyofrome.Screens.PlayScreen;
import net.davidbrowne.furyofrome.Tools.TransitionScreen;

import static java.lang.Integer.parseInt;

public class Anubis extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation,deadAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy,dead=false;
    public boolean attacking=false;
    private PlayScreen screen;
    private boolean schedule=false;
    private Fixture fix;
    private int spriteNum;
    private FixtureDef attackdef = new FixtureDef();
    public boolean runningRight=true;
    public Anubis(PlayScreen screen, float x, float y, MapObject object) {
        super(screen, x, y);
        spriteNum= 9;
        this.screen = screen;
        frames = new Array<TextureRegion>();
        for(int i=0; i<3;i++){
            frames.add(screen.getAtlas().findRegion("npc"+spriteNum));
        }
        walkAnimation = new Animation(.2f,frames);
        frames = new Array<TextureRegion>();
        frames.add(screen.getAtlas().findRegion("npc"+spriteNum+"_dead_1"));
        frames.add(screen.getAtlas().findRegion("npc"+spriteNum+"_dead_2"));
        frames.add(screen.getAtlas().findRegion("npc"+spriteNum+"_dead_3"));

        deadAnimation = new Animation(.2f,frames);
        stateTime= 0;
        setBounds(getX(),getY(),16/ Game.PPM,16/Game.PPM);
        setToDestroy=false;
        destroyed=false;
        velocity = new Vector2(-0.0f,0);
    }

    public void update(float dt){

        stateTime+=dt;
        if(setToDestroy && !destroyed){
            if(!schedule){
                world.destroyBody(b2body);
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {

                        destroyed=true;
                    }
                }, 0.3f);
                if(runningRight)
                    flip(true,false);
                //screen.getManager().get("audio/sounds/splat.wav", Sound.class).play(screen.getGame().getSoundVolume());
                stateTime=0;
                schedule=true;
            }
        }
        if(!destroyed){
            if(b2body.getLinearVelocity().y==0)
                b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x- getWidth()/2, b2body.getPosition().y- getHeight()/2);

            if(!attacking&&!dead)
                setRegion( walkAnimation.getKeyFrame(stateTime,true));

        }
        if(velocity.x<=0)
            runningRight=false;
        else
            runningRight=true;
        if( !runningRight && !isFlipX()){
            flip(true, false);
        }
        else if(runningRight && isFlipX()){
            flip(true, false);
        }

    }


    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ Game.PPM);
        fdef.filter.categoryBits = Game.ENEMY_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT
                | Game.PLAYER_BIT
                | Game.GROUND_BIT
                | Game.BRICK_BIT
                | Game.BLOCK_BIT
                | Game.ITEM_BIT
                | Game.BOX_BIT
                | Game.ATTACK_BIT
                | Game.BULLET_BIT
                | Game.ENEMY_BIT;
        fdef.shape = shape;
        shape.dispose();
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 2.5){
            super.draw(batch);
        }
    }

    @Override
    public void OnHit() {
        dead=true;
        setRegion(deadAnimation.getKeyFrame(stateTime, false));
        setToDestroy=true;
        screen.getGame().setScreen(new TransitionScreen(screen,new EndScreen(screen.getGame(),screen.getManager()),screen.getGame(),true));
    }

    @Override
    public void hitOnHead() {

    }

}
