package com.mygdx.game.Box2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Box2D.Enemy;

public class WallEnemy extends Enemy {

    Wall wall;

    public WallEnemy(Batch batch, World world, Animation<TextureRegion> animation, float x, float y) {
        super(batch, world, animation, x, y);
        wall = new Wall(world, x - 4, y-1, 2, 5);
    }

    @Override
    public void display(float elapsedTime) {
        if(super.isDestroyed) {
            return;
        }

        if(enemy.getLinearVelocity().x != 0 || enemy.getLinearVelocity().y != 0) {
            super.w.destroyBody(super.enemy);
            super.w.destroyBody(wall.wall);
            super.isDestroyed = true;
            return;
        }

        float wallPosX = wall.getPositionX();
        float wallPosY = wall.getPositionY();

        wall.setPosition(wallPosX - .1f, wallPosY);
        super.enemy.setTransform(wallPosX + 3, wallPosY, 0);

        float enemyPosX = super.enemy.getPosition().x;
        float enemyPosY = super.enemy.getPosition().y;

        super.b.draw(new Texture(Gdx.files.internal("shield.png")), wallPosX, wallPosY, 5, 10);
        super.b.draw(super.enemyAnimation.getKeyFrame(elapsedTime, true), enemyPosX, enemyPosY + 2.5f, 5, 5);
    }
}
