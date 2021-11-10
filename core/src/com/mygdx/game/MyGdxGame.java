package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MyGdxGame extends ApplicationAdapter {

	OrthographicCamera camera;
	ExtendViewport viewport;
	TextureAtlas axeAtlas;
	TextureAtlas enemyAtlas;
	Animation<TextureRegion> axeAnimation;
	Animation<TextureRegion> enemyAnimation;
	World world;
	Axe axe;
	Enemy enemy;
	float elapsedTime = 0f;
	SpriteBatch batch;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(50, 50, camera);

		axeAtlas = new TextureAtlas(Gdx.files.internal("axe.atlas"));
		axeAnimation = new Animation<TextureRegion>(.075f, axeAtlas.getRegions());

		enemyAtlas = new TextureAtlas(Gdx.files.internal("enemy.atlas"));
		enemyAnimation = new Animation<TextureRegion>(.075f, enemyAtlas.getRegions());

		batch = new SpriteBatch();

		Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				axe.throwAxe(velocityX, velocityY);
				return true;
			}
		}));

		Box2D.init();
		world = new World(new Vector2(0, 0), true);
		axe = new Axe(batch, world, axeAnimation, 25, 25);
		enemy = new Enemy(batch, world, enemyAnimation, 51, 25);

		createWalls();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 1, 0, 1);

		world.step(1f / 60f, 8 ,3);//step physics engine

		elapsedTime += Gdx.graphics.getDeltaTime();

		batch.begin();

		//createEnemies();

		axe.display(elapsedTime);
		enemy.display(elapsedTime);

		batch.end();
	}

	public void createEnemies() {
		//Enemy enemy = new Enemy(batch, world, enemyAnimation, 51, 20);
	}

	public void createWalls() {
		Wall ceiling = new Wall(world, 0, 50, 100, 0);
		Wall floor = new Wall(world, 0, 0, 100, 0);
		Wall rightWall = new Wall(world, 75, 0, 0, 100);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}
}
