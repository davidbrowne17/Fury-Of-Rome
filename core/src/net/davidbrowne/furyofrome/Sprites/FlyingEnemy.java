package net.davidbrowne.furyofrome.Sprites;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.PlayScreen;

public class FlyingEnemy extends Enemy {
    private float stateTime;
    private int flycount=0;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private PlayScreen screen;
    public FlyingEnemy(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.screen = screen;
        frames = new Array<TextureRegion>();
        for(int i=0; i<2;i++){
            frames.add(screen.getAtlas().findRegion("flyingdemon"));
            frames.add(screen.getAtlas().findRegion("flyingdemon1"));
        }
        walkAnimation = new Animation(.2f,frames);
        stateTime= 0;
        setBounds(getX(),getY(),16/ Game.PPM,16/Game.PPM);
        setToDestroy=false;
        destroyed=false;
        velocity = new Vector2(-0.5f,0);
    }

    public void update(float dt){
        stateTime+=dt;
        flycount++;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed=true;
            setRegion(screen.getAtlas().findRegion("flyingdemon3"));
            stateTime=0;
        }
        if(!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x- getWidth()/2, b2body.getPosition().y- getHeight()/2);
            setRegion( walkAnimation.getKeyFrame(stateTime,true));
        }
        if(flycount>13){
            b2body.applyLinearImpulse(new Vector2(0f,2.8f),b2body.getWorldCenter(),true);
            flycount=0;
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
        shape.setRadius(6/ Game.PPM);
        fdef.filter.categoryBits = Game.ENEMY_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT
                | Game.PLAYER_BIT
                | Game.ATTACK_BIT
                | Game.GROUND_BIT
                | Game.BLOCK_BIT
                | Game.BRICK_BIT
                | Game.ITEM_BIT
                | Game.BOX_BIT
                | Game.BULLET_BIT
                | Game.ENEMY_BIT;
        fdef.shape = shape;
        fdef.restitution = 1f;
        fdef.friction = 0f;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 3){
            super.draw(batch);
        }
    }

    @Override
    public void OnHit() {
        setToDestroy=true;
        screen.getManager().get("audio/sounds/splat.wav", Sound.class).play(screen.getGame().getVolume());
    }

    @Override
    public void hitOnHead() {
        if(!setToDestroy)
        screen.getPlayer().die();

    }
}