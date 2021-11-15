package com.mygdx.game.Box2D;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Wall {

    Body wall;

    public Wall(World world, float x, float y, float width, float height) {

        //DEFINE BODY
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));//pixel coords work if use ortho camera 50x50
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //CREATE BODY
        wall = world.createBody(bodyDef);

        //CREATE SHAPE
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        //CREATE FIXTURE
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        //ATTACH FIXTURE TO BODY
        wall.createFixture(fixtureDef);

        shape.dispose();
    }

    void setPosition(float x, float y) {
        wall.setTransform(x, y, 0);
    }

    float getPositionX() {
        return wall.getPosition().x;
    }

    float getPositionY() {
        return wall.getPosition().y;
    }
}
