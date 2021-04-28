package net.davidbrowne.furyofrome.Sprites;
import com.badlogic.gdx.graphics.Texture;
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

public class Fireball extends Enemy {
    private Animation<TextureRegion> fireAnimation;
    private Array<TextureRegion> frames;
    private PlayScreen screen;
    private float stateTime;
    private Texture hideTexture;
    public Fireball(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.screen = screen;
        frames = new Array<TextureRegion>();
        frames.add(screen.getAtlas().findRegion("fireball"));
        frames.add(screen.getAtlas().findRegion("fireball2"));
        frames.add(screen.getAtlas().findRegion("fireball3"));
        fireAnimation = new Animation(0.1f,frames);
        setBounds(getX(),getY(),16/ Game.PPM,16/Game.PPM);
        velocity = new Vector2(0f,1f);
        stateTime=0;
    }

    public void update(float dt){
        stateTime+=dt;
        setRegion(fireAnimation.getKeyFrame(stateTime,true));
        if(stateTime>=0.8f){
            reverseVelocity(false,true);
            stateTime=0;
        }
        b2body.setLinearVelocity(velocity);
        setPosition(b2body.getPosition().x- getWidth()/2, b2body.getPosition().y- getHeight()/2);
    }


    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8/ Game.PPM);
        fdef.filter.categoryBits = Game.ENEMY_BIT;
        fdef.filter.maskBits = Game.PLAYER_BIT
                | Game.GROUND_BIT
                | Game.SPIKE_BIT;
        fdef.shape = shape;
        b2body.setGravityScale(0);
        shape.dispose();
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        super.draw(batch);
    }

    @Override
    public void OnHit() {
        screen.getPlayer().die();
    }


    @Override
    public void hitOnHead() {
        screen.getPlayer().die();
    }
}
