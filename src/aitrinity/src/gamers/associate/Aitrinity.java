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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
import com.badlogic.gdx.utils.Array;

public class Aitrinity implements ApplicationListener, InputProcessor, TweenAccessor<Aitrinity> {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private int activateCols = 8;
	private int activateCount = 6;
	private Array<AtlasRegion>	activateFrames;
	private Animation activateAnimation;
	private float stateTime;
	
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
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
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
		font = fontGenerator.generateFont(20);
		font.setColor(Color.GREEN);
		
		effects = new ArrayList<PooledEffect>();
		ParticleEffect prototype = new ParticleEffect();
		prototype.load(Gdx.files.internal("data/zerogreen2.p"), atlas);
		pool = new ParticleEffectPool(prototype, 1, 2);
		
		Gdx.input.setInputProcessor(this);
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Aitrinity.class, this);
		
//		TextureRegion region = new TextureRegion(texture, 0, 0, 256, 64);
//		
//		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		// texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// sprite.draw(batch);
		float delta = Gdx.graphics.getDeltaTime();
		tweenManager.update(delta);
		stateTime += delta;
		TextureRegion texture = activateAnimation.getKeyFrame(stateTime, true);
		
		font.draw(batch, "Waazzaaaaaa!", 0, 0);
		for(PooledEffect effect : effects) {
			effect.setPosition(x, y);
			effect.draw(batch, delta);
			if (effect.isComplete()) {
				effect.free();
				effects.remove(effect);
			}
		}
		
		if (!move) {
			batch.draw(texture, x-texture.getRegionWidth() / 2f, y-texture.getRegionHeight() / 2f);
		}
		
		batch.end();		
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
		targetX = (screenX - Gdx.graphics.getWidth() / 2) * zoom;
		targetY = (Gdx.graphics.getHeight() / 2f - screenY) * zoom;
		Timeline.createSequence()
			.push(Tween.call(new TweenCallback() {
				
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					move = true;
					PooledEffect effect = pool.obtain();
					effects.add(effect);
				}
			}))
			.push(Tween.to(this, 0, 1.0f).target(targetX, targetY).start(tweenManager))
			.push(Tween.call(new TweenCallback() {
				
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					for (PooledEffect effect : effects) {
						effect.free();						
					}
					
					effects.clear();
					move = false;
				}
			}))
			.start(tweenManager);
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
