package gamers.associate;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Aitrinity implements ApplicationListener, InputProcessor, TweenAccessor<Aitrinity> {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Sprite sprite;
	private int activateCols = 8;
	private int activateCount = 6;
	private Array<AtlasRegion>	activateFrames;
	private Animation activateAnimation;
	private float stateTime;
	private ShapeRenderer shapeRenderer;
	
	private TextureAtlas atlas;
	private BitmapFont font;
	
	private FreeTypeFontGenerator fontGenerator;
	
	private ParticleEffectPool pool;
	private ArrayList<PooledEffect> effects;
	
	public float x;
	public float y;
	
	private float targetX;
	private float targetY;
	
	private TweenManager tweenManager;
	private float zoom = 0.5f;
	
	private boolean move;
	private int fontSize = 10;
	
	private float sayLife = 1f;
	private String sayText;
	private float sayTime;
	
	private float speed = 300;
	
	private TiledMap room0;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private float camX;
	private float camY;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		x = 50;
		y = 50;
		
		camera = new OrthographicCamera(w, h);
		camX = (w / 2f) * zoom;
		camY = (h / 2f) * zoom;
		camera.translate(camX, camY);
		camera.zoom = zoom;
		camera.update();
		batch = new SpriteBatch();
		/*
		texture = new Texture(Gdx.files.internal("data/anim_activate.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / activateCols, texture.getHeight());
		activateFrames = new TextureRegion[activateCount];
		for (int i = 0; i < activateCount; i++) {
			activateFrames[i] = tmp[0][i];
		}
				
		*/
		stateTime = 0f;
		
		// second method
		atlas = new TextureAtlas(Gdx.files.internal("data/pack.atlas"));
		activateFrames = atlas.findRegions("ia1/activate");
		
		activateAnimation = new Animation(0.1f, activateFrames);
		
		// font = new BitmapFont(Gdx.files.internal("data/04b03.fnt"), Gdx.files.internal("data/04b03.png"), false);
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/04b03.ttf"));
		font = fontGenerator.generateFont(fontSize);
		font.setColor(Color.BLACK);
		
		effects = new ArrayList<PooledEffect>();
		ParticleEffect prototype = new ParticleEffect();
		prototype.load(Gdx.files.internal("data/zerogreen2.p"), atlas);
		pool = new ParticleEffectPool(prototype, 1, 2);
		
		Gdx.input.setInputProcessor(this);
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Aitrinity.class, this);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.GREEN);
		
		room0 = new TmxMapLoader().load("data/room0.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(room0, 2f);
	}

	@Override
	public void dispose() {
		batch.dispose();
		// texture.dispose();
	}

	TextureRegion texturePlayer;
	
	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
				
		camX = x;
		camY = y;
		camera.position.x = x;
		camera.position.y = y;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		batch.begin();
		// sprite.draw(batch);
		float delta = Gdx.graphics.getDeltaTime();
		tweenManager.update(delta);
		stateTime += delta;
		texturePlayer = activateAnimation.getKeyFrame(stateTime, true);
		
		batch.end();
		if (sayText != null) {
			sayTime += delta;
			if (sayTime < sayLife) {
				say(sayText);
			} else {
				sayText = null;
			}
		}
		
		batch.begin();
		for(PooledEffect effect : effects) {
			effect.setPosition(x, y+texturePlayer.getRegionHeight() / 2f);
			effect.draw(batch, delta);
			if (effect.isComplete()) {
				effect.free();
				effects.remove(effect);
			}
		}
		
		if (!move) {
			batch.draw(texturePlayer, x-texturePlayer.getRegionWidth() / 2f, y);
		}
		
		batch.end();	
	}
	
	private void say(String text) {
		float sayX = x + texturePlayer.getRegionWidth() / 4f;
		float sayY = y + texturePlayer.getRegionHeight();
		float padding = 5;
		float paddingIn = 4;
		float sayWidth = text.length() * fontSize;
		float sayHeight = fontSize;
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(sayX - padding, sayY-padding, sayWidth + padding * 2, sayHeight + padding * 2);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(sayX - paddingIn, sayY-paddingIn, sayWidth + paddingIn * 2, sayHeight + paddingIn * 2);
		shapeRenderer.end();
		font.setColor(Color.BLACK);
		batch.begin();
		font.draw(batch, text, sayX, sayY + sayHeight * 3 / 4);
		batch.end();
	}
	
	private void setSay(String text) {
		sayText = text;
		sayTime = 0;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!move) {
			targetX = (screenX - Gdx.graphics.getWidth() / 2) * zoom + camX;
			targetY = (Gdx.graphics.getHeight() / 2f - screenY) * zoom + camY;
			Vector2 pos = new Vector2(x, y);
			Vector2 target = new Vector2(targetX, targetY);
			float length = target.sub(pos).len();
			float time = length / speed + 0.5f;
			Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						move = true;
						sayText = null;
						PooledEffect effect = pool.obtain();
						effects.add(effect);
					}
				}))
				.push(Tween.to(this, 0, time).target(targetX, targetY))
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						for (PooledEffect effect : effects) {
							effect.free();						
						}
						
						effects.clear();
						move = false;
						setSay("Waazzaaaaaaa");
					}
				}))
				.start(tweenManager);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getValues(Aitrinity target, int tweenType, float[] returnValues) {
		returnValues[0] = target.x;
		returnValues[1] = target.y;
		return 2;
	}

	@Override
	public void setValues(Aitrinity target, int tweenType, float[] newValues) {
		target.x = newValues[0];
		target.y = newValues[1];
	}
}
