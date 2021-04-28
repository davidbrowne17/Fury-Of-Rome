package net.davidbrowne.furyofrome.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.PlayScreen;
import net.davidbrowne.furyofrome.Sprites.AttackingEnemy;
import net.davidbrowne.furyofrome.Sprites.Box;
import net.davidbrowne.furyofrome.Sprites.Brick;
import net.davidbrowne.furyofrome.Sprites.Coin;
import net.davidbrowne.furyofrome.Sprites.Enemy;
import net.davidbrowne.furyofrome.Sprites.Fireball;
import net.davidbrowne.furyofrome.Sprites.FlyingEnemy;
import net.davidbrowne.furyofrome.Sprites.FriendlyNPC;
import net.davidbrowne.furyofrome.Sprites.LevelEnd;
import net.davidbrowne.furyofrome.Sprites.Player;
import net.davidbrowne.furyofrome.Sprites.Spear;
import net.davidbrowne.furyofrome.Sprites.Spike;

public class B2WorldCreator {
    private Array<Enemy> enemies;
    private Array<AttackingEnemy> attackingEnemies;
    private Array<FriendlyNPC>friendlyNPC;
    private Array<Fireball> fireballs;
    private Array<FlyingEnemy> flyingEnemy;
    private PlayScreen screen;

    public B2WorldCreator(PlayScreen screen){
        this.screen=screen;
        enemies = new Array<Enemy>();
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        AssetManager manager = screen.getManager();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //create ground
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(((rect.getX()+rect.getWidth()/2)/ Game.PPM),(rect.getY()+rect.getHeight()/2)/ Game.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2 / Game.PPM,rect.getHeight()/2 / Game.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Box(screen,object,manager);
        }
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Brick(screen,object,manager);
        }
        //spawn player
        for(MapObject object : screen.getMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            Player player = new Player(screen,(int)rect.getX(),(int)rect.getY());
            screen.setPlayer(player);
        }
        //spawn enemy
        attackingEnemies = new Array<AttackingEnemy>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            attackingEnemies.add(new AttackingEnemy(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM,object));
        }
        //spawn npc
        friendlyNPC = new Array<FriendlyNPC >();
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            friendlyNPC.add(new FriendlyNPC (screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM,object));
        }
        //finish level
        for(MapObject object : screen.getMap().getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new LevelEnd(screen,object,manager);
        }
        //coins
        for(MapObject object : screen.getMap().getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Coin(screen,object,manager);
        }
        //spears
        for(MapObject object : screen.getMap().getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Spear(screen,object,manager);
        }
        //spike
        for(MapObject object : screen.getMap().getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Spike(screen,object,manager);
        }
        //Fireballs
        fireballs = new Array<Fireball>();
        for(MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            fireballs.add(new Fireball(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //Flying enemy
        flyingEnemy = new Array<FlyingEnemy>();
        for(MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            flyingEnemy.add(new FlyingEnemy(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }


    }
    public Array<Enemy> getEnemies() {
        if(enemies.isEmpty()){
            enemies.addAll(attackingEnemies);
            enemies.addAll(friendlyNPC);
            enemies.addAll(fireballs);
            enemies.addAll(flyingEnemy);
        }
        return enemies;
    }
}
