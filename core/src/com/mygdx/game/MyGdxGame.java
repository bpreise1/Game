package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.Box2D.Axe;
import com.mygdx.game.Box2D.Enemy;
import com.mygdx.game.Box2D.Wall;
import com.mygdx.game.Box2D.WallEnemy;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {

	OrthographicCamera camera;
	ExtendViewport viewport;
	TextureAtlas axeAtlas;
	TextureAtlas enemyAtlas;
	Animation<TextureRegion> axeAnimation;
	Animation<TextureRegion> enemyAnimation;
	World world;
	Axe axe;
	Texture redLine;
	float elapsedTime = 0f;
	SpriteBatch batch;
	int score;
	BitmapFont font;
	boolean isStarted = false;
	float spawnTime = 5f;
	float elapsedSpawnTime = 0f;
	int highScore = 0;
	ArrayList<Enemy> enemies;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(50, 50, camera);

		axeAtlas = new TextureAtlas(Gdx.files.internal("axe.atlas"));
		axeAnimation = new Animation<TextureRegion>(.075f, axeAtlas.getRegions());

		enemyAtlas = new TextureAtlas(Gdx.files.internal("enemy.atlas"));
		enemyAnimation = new Animation<TextureRegion>(.075f, enemyAtlas.getRegions());

		redLine = new Texture(Gdx.files.internal("red_line.png"));

		batch = new SpriteBatch();

		enemies = new ArrayList<>();

		Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				axe.throwAxe(velocityX, -1 * velocityY);
				return true;
			}

			@Override
			public boolean tap(float x, float y, int count, int button) {
				if(!isStarted) {
					clearBodies();
					axe = new Axe(batch, world, axeAnimation, 25, 25);
				}
				isStarted = true;
				return true;
			}
		}));

		Box2D.init();
		world = new World(new Vector2(0, 0), true);
		score = 0;
		font = new BitmapFont();
		font.getData().setScale(.4f, .4f);

		createWalls();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 1, 0, 1);

		batch.begin();

		if(isStarted) {
			world.step(1f / 60f, 8, 3);//step physics engine

			elapsedTime += Gdx.graphics.getDeltaTime();
			elapsedSpawnTime += Gdx.graphics.getDeltaTime();

			createEnemies();

			batch.draw(redLine, 10, 0, 2, 100);

			displayScore();

			axe.display(elapsedTime);

			checkLose();
		}
		else {
			displayStart();
		}

		batch.end();
	}

	public void clearBodies() {
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);
		for(Body body : bodies) {
			if(body.getPosition().x != 0f && body.getPosition().x != 75f && body.getPosition().x != -5f) {
				world.destroyBody(body);
			}
		}
	}

	public void createEnemies() {
		Random rand = new Random();
		int rand_int = rand.nextInt(2);
		float float_random = rand.nextFloat() * 40 + 5;
		if(elapsedSpawnTime >= spawnTime) {
			if(rand_int == 0) {
				enemies.add(new Enemy(batch, world, enemyAnimation, 70, float_random));
			}
			else {
				enemies.add(new WallEnemy(batch, world, enemyAnimation, 70, float_random));
			}
			elapsedSpawnTime = 0;
		}
		if(score > 30 && score <= 60) {
			spawnTime = 4f;
		}
		else if(score > 60 && score <= 90) {
			spawnTime = 3f;
		}
		else if(score > 90 && score <= 120) {
			spawnTime = 2f;
		}
		else if(score > 150) {
			spawnTime = 1f;
		}
		for(Enemy enemy : enemies) {
			enemy.display(elapsedTime);
		}
	}

	public void checkLose() {
		for(Enemy enemy : enemies) {
			if(!enemy.isDestroyed && enemy.getPositionX() < 10) {
				for(Enemy e : enemies) {
					e.isDestroyed = true;
				}
				enemies.clear();
				score = 0;
				spawnTime = 5f;
				isStarted = false;
				return;
			}
		}
	}

	public void displayStart() {
		font.draw(batch,"Tap to start!", 25, 25);
		font.draw(batch, "High Score: " + highScore, 25, 10);
	}

	public void displayScore() {
		if(elapsedTime >= 1f) {
			elapsedTime = 0f;
			score++;
		}
		if(score > highScore) {
			highScore = score;
		}
		font.draw(batch, "Score: " + score, 5, 45);
	}

	public void createWalls() {
		Wall ceiling = new Wall(world, 0, 50, 100, 0);
		Wall floor = new Wall(world, 0, 0, 100, 0);
		Wall rightWall = new Wall(world, 75, 0, 0, 100);
		Wall leftWall = new Wall(world, -5, 0, 0, 100);
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
