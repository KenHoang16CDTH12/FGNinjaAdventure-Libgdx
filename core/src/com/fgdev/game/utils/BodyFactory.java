package com.fgdev.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.fgdev.game.Constants.*;

public class BodyFactory {

    public static final int NOTHING = 0;
    public static final int GROUND = 1;
    public static final int SIGN = 2;
    public static final int KUNAI = 3;
    public static final int COIN = 4;
    public static final int CRATE = 5;
    public static final int FEATHER = 6;
    public static final int PLAYER = 7;
    public static final int PLAYER_FOOT = 8;
    public static final int PLAYER_ATTACK = 9;
    public static final int ZOMBIE = 10;
    public static final int ZOMBIE_FOOT = 11;

    private final float DEGTORAD = 0.0174533f;

    private World world;

    public static BodyFactory instance;

    public BodyFactory(World world){
        this.world = world;
    }

    public static BodyFactory getInstance(World world){
        if(instance == null){
            instance = new BodyFactory(world);
        } else {
            instance.world = world;
        }
        return instance;
    }

    public FixtureDef makeFixture(int material, Shape shape){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        switch(material){
            case GROUND:
                fixtureDef.filter.categoryBits = GROUND_BIT;
                fixtureDef.filter.maskBits = PLAYER_BIT | CRATE_BIT | ZOMBIE_BIT;
                break;
            case SIGN:
                fixtureDef.filter.categoryBits = SIGN_BIT;
                fixtureDef.filter.maskBits = PLAYER_BIT;
                fixtureDef.isSensor = true;
                break;
            case KUNAI:
                fixtureDef.filter.categoryBits = KUNAI_BIT;
                fixtureDef.filter.maskBits = CRATE_BIT;
                break;
            case COIN:
                fixtureDef.filter.categoryBits = COIN_BIT;
                fixtureDef.filter.maskBits = PLAYER_BIT;
                fixtureDef.isSensor = true;
                break;
            case CRATE:
                fixtureDef.filter.categoryBits = CRATE_BIT;
                fixtureDef.filter.maskBits = CRATE_BIT | GROUND_BIT | ATTACK_BIT | KUNAI_BIT | PLAYER_BIT ;
                break;
            case FEATHER:
                fixtureDef.filter.categoryBits = FEATHER_BIT;
                fixtureDef.filter.maskBits = PLAYER_BIT;
                fixtureDef.isSensor = true;
                break;
            case PLAYER:
                fixtureDef.filter.categoryBits = PLAYER_BIT;
                fixtureDef.filter.maskBits =  GROUND_BIT | FEATHER_BIT | COIN_BIT | CRATE_BIT | SIGN_BIT;
                break;
            case PLAYER_FOOT:
                fixtureDef.filter.categoryBits = PLAYER_BIT;
                fixtureDef.filter.maskBits = GROUND_BIT | FEATHER_BIT | COIN_BIT | CRATE_BIT | SIGN_BIT;
                fixtureDef.isSensor = true;
                break;
            case PLAYER_ATTACK:
                fixtureDef.filter.categoryBits = ATTACK_BIT;
                fixtureDef.filter.maskBits = CRATE_BIT;
                fixtureDef.isSensor = true;
                break;
            case ZOMBIE:
                fixtureDef.filter.categoryBits = ZOMBIE_BIT;
                fixtureDef.filter.maskBits =  GROUND_BIT | CRATE_BIT | PLAYER_BIT;
                break;
            case ZOMBIE_FOOT:
                fixtureDef.filter.categoryBits = ZOMBIE_BIT;
                fixtureDef.filter.maskBits =  GROUND_BIT | CRATE_BIT | PLAYER_BIT;
                fixtureDef.isSensor = true;
                break;
            case NOTHING:
            default:
                fixtureDef.density = 0;
                fixtureDef.friction = 0;
                fixtureDef.restitution = 0;
                break;
        }
        return fixtureDef;
    }

    public void makeAllFixturesSensors(Body bod){
        for(Fixture fix :bod.getFixtureList()){
            fix.setSensor(true);
        }
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, int material){
        return makeCirclePolyBody( posx, posy, radius, material, BodyDef.BodyType.DynamicBody, false);
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyDef.BodyType bodyType, Object object){
        return makeCirclePolyBody( posx, posy, radius, material, bodyType, false, object);
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyDef.BodyType bodyType, boolean fixedRotation, Object object){
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;
        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        boxBody.createFixture(makeFixture(material,circleShape)).setUserData(object);
        circleShape.dispose();
        return boxBody;
    }

    public Body makeBoxPolyBody(float posx, float posy, float width, float height,int material, BodyDef.BodyType bodyType, Object object){
        return makeBoxPolyBody(posx, posy, width, height, material, bodyType, false, object);
    }

    public Body makeBoxPolyBody(float posx, float posy, float width, float height, int material, BodyDef.BodyType bodyType, boolean fixedRotation, Object object) {
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.type = bodyType;
        boxBodyDef.fixedRotation = fixedRotation;
        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        boxBody.createFixture(makeFixture(material,poly)).setUserData(object);
        poly.dispose();
        return boxBody;
    }

    public Body makePolygonShapeBody(Vector2[] vertices, float posx, float posy, int material, BodyDef.BodyType bodyType, Object object){
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.type = bodyType;
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices);
        boxBody.createFixture(makeFixture(material,polygon)).setUserData(object);
        polygon.dispose();
        return boxBody;
    }

    public void makeObjectSensor(Body body, float hx, float hy, Vector2 center, float angel, int material, Object object) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy, center, angel);
        FixtureDef fixtureDef = makeFixture(material, shape);
        body.createFixture(fixtureDef).setUserData(object);
        shape.dispose();
    }

    public void makeConeSensor(Body body, float size){
        FixtureDef fixtureDef = new FixtureDef();
        //fixtureDef.isSensor = true; // will add in future
        PolygonShape polygon = new PolygonShape();
        float radius = size;
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0,0);
        for (int i = 2; i < 6; i++) {
            float angle = (float) (i / 6.0 * 145 * DEGTORAD); // convert degrees to radians
            vertices[i-1] = new Vector2( radius * ((float)Math.cos(angle)), radius * ((float)Math.sin(angle)));
        }
        polygon.set(vertices);
        fixtureDef.shape = polygon;
        body.createFixture(fixtureDef);
        polygon.dispose();
    }
}
