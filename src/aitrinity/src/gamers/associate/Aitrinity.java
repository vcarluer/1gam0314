package gamers.associate;

import java.util.HashSet;

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
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Aitrinity implements ApplicationListener, InputProcessor, TweenAccessor<Aitrinity> {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Array<AtlasRegion>	activateFrames;
	private Animation activateAnimation;
	private Array<AtlasRegion> ia1Frames;
	private Animation ia1Animation;
	private float stateTime;
	private ShapeRenderer shapeRenderer;
	
	private TextureAtlas atlas;
	private BitmapFont font;
	
	private FreeTypeFontGenerator fontGenerator;
	
	private ParticleEffectPool pool;
	private HashSet<PooledEffect> effects;
	private ParticleEffect bkgEffect;
	
	public Rectangle player;
	private Vector2 targetV;
	private Vector2 clickV;
	
	private TweenManager tweenManager;
	private float zoom = 0.5f;
	
	private boolean move;
	private int fontSize = 10;
	
	private float sayLife = 1f;
	private String sayText;
	private float sayTime;
	
	private float speed = 500;
	
	private TiledMap room0;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private float camX;
	private float camY;
	
	private float mapRatio = 2F;
	private float baseTileSize = 16;
	private float tileSize = baseTileSize * mapRatio;
	
	private HashSet<Vector2> passable;
	private Vector2 testV;
	
	private Rectangle ia1;
	private String ia1Text;
	private Rectangle ia1TextRect;
	private Rectangle playerTextRect;
	
	private String opt1;
	private Rectangle opt1Rect;
	private boolean opt1Over;
	
	private Rectangle referenceTalk;
	
	private Rectangle tempRect;
	
	@Override
	public void create() {		
		passable = new HashSet<Vector2>();
		testV = new Vector2();
		targetV = new Vector2();
		clickV = new Vector2();
		player = new Rectangle();
		ia1TextRect = new Rectangle();
		playerTextRect = new Rectangle();
		tempRect = new Rectangle();
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		player.x = worldCoord(3);
		player.y = worldCoord(3);
		player.width = tileSize;
		player.height = tileSize;
		
		ia1 = new Rectangle();
		ia1.x = worldCoord(9);
		ia1.y = worldCoord(12);
		ia1.width = 32;
		ia1.height = 64;
		opt1Rect = new Rectangle();
		
		camera = new OrthographicCamera(w, h);
		camX = (w / 2f) * zoom;
		camY = (h / 2f) * zoom;
		camera.translate(camX, camY);
		camera.zoom = zoom;
		camera.update();
		batch = new SpriteBatch();
		stateTime = 0f;
		
		// second method
		atlas = new TextureAtlas(Gdx.files.internal("data/pack.atlas"));
		activateFrames = atlas.findRegions("player/idle");
		
		activateAnimation = new Animation(0.12f, activateFrames);
		
		ia1Frames = atlas.findRegions("ia1/activate");
		ia1Animation = new Animation(0.12f, ia1Frames);
		
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/04b03.ttf"));
		font = fontGenerator.generateFont(fontSize);
		font.setColor(Color.BLACK);
		
		effects = new HashSet<PooledEffect>();
		ParticleEffect prototype = new ParticleEffect();
		prototype.load(Gdx.files.internal("data/zerogreen2.p"), atlas);
		pool = new ParticleEffectPool(prototype, 1, 2);
		
		bkgEffect = new ParticleEffect();
		bkgEffect.load(Gdx.files.internal("data/bkg.p"), atlas);
		
		Gdx.input.setInputProcessor(this);
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Aitrinity.class, this);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.GREEN);
		
		room0 = new TmxMapLoader().load("data/map3.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(room0, mapRatio);
		
		TiledMapTileLayer layer = (TiledMapTileLayer) room0.getLayers().get(0);
		int col = layer.getWidth();
		int row = layer.getHeight();
		for (int x = 0; x < col; x++) {
			for (int y = 0; y < row; y++) {
				TiledMapTileLayer.Cell cell = layer.getCell(x, y);
				if (cell != null) {
					TiledMapTile tile = cell.getTile();
					if (tile.getProperties().containsKey("passable")) {
						Vector2 v = new Vector2(x, y);
						passable.add(v);
					}
				}
				
			}
		}
	}

	private int mapCoord(float val) {
		return (int) Math.floor(val / tileSize);
	}
	
	private float worldCoord(int val) {
		return val * tileSize;
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		// texture.dispose();
	}

	TextureRegion texturePlayer;
	
	@Override
	public void render() {		
		Gdx.gl.glClearColor(0.01f, 0.05f, 0, 0.1f);
		// Gdx.gl.glClearColor(0.06f, 0.3f, 0, 0.36f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if (dead) {
			dead = false;
			player.x = worldCoord(3);
			player.y = worldCoord(3);
			referenceTalk = null;
			sayText = null;
			opt1 = null;
			ia1Text = null;
		}
				
		camX = player.x;
		camY = player.y;
		camera.position.x = player.x;
		camera.position.y = player.y;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		mapRenderer.setView(camera);
		float delta = Gdx.graphics.getDeltaTime();
		
		
		batch.begin();
		// bkgEffect.setPosition(x, y);
		bkgEffect.draw(batch, delta);
		batch.end();
		
		mapRenderer.render();
		
		batch.begin();
		// sprite.draw(batch);
		
		tweenManager.update(delta);
		stateTime += delta;
		texturePlayer = activateAnimation.getKeyFrame(stateTime, true);
		TextureRegion textureIa1 = ia1Animation.getKeyFrame(stateTime, true);
		
		for(PooledEffect effect : effects) {
			effect.setPosition(player.x + texturePlayer.getRegionWidth() / 2f, player.y+texturePlayer.getRegionHeight() / 2f);
			effect.draw(batch, delta);
			if (effect.isComplete()) {
				effect.free();
				effects.remove(effect);
			}
		}
		
		if (ia1.y > player.y) {
			batch.draw(textureIa1, ia1.x, ia1.y);
		}
		
		if (!move) {
			batch.draw(texturePlayer, player.x, player.y);
		}
		
		if (ia1.y <= player.y) {
			batch.draw(textureIa1, ia1.x, ia1.y);
		}
		
		batch.end();	
		
		if (sayText != null) {
			sayTime += delta;
			if (sayTime < sayLife) {
				say(sayText);
			} else {
				sayText = null;
			}
		}
		
		if (ia1Text != null) {
			setSayRect(ia1Text, 0, textureIa1, ia1.x, ia1.y, ia1TextRect, player);
			say(ia1Text, ia1TextRect, false);
		}
		
		if (opt1 != null) {
			setSayRect(opt1, 1, texturePlayer, player.x, player.y, opt1Rect, referenceTalk);
			say(opt1, opt1Rect, true);
		}
	}
	
	private void say(String text) {
		setSayRect(text, 0, texturePlayer, player.x, player.y, playerTextRect, referenceTalk);
		say(text,  playerTextRect, false);
	}
	
	private void setSayRect(String text, int idx, TextureRegion texture, float x, float y, Rectangle rect, Rectangle relativeTalk) {
		TextBounds bounds = font.getBounds(text);
		if (relativeTalk == null || relativeTalk.x < x) {
			rect.x = x + texture.getRegionWidth();
		} else {
			rect.x = x - bounds.width - padding * 2;
		}
		
		rect.y = y + texture.getRegionHeight() - idx * (bounds.height + padding * 2);
		
		rect.width = bounds.width;
		rect.height = bounds.height;
	}
	
	private float padding = 5;
	private float paddingIn = 4;
	
	private void say(String text, Rectangle rect, boolean focus) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.setColor(Color.GREEN);
		tempRect.x = rect.x - padding;
		tempRect.y = rect.y-padding;
		tempRect.width = rect.width + padding * 2;
		tempRect.height = rect.height + padding * 2;
		shapeRenderer.rect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
		if (opt1Over && focus) {
			shapeRenderer.setColor(new Color(0.8f, 1f, 0, 1f));
		} else {
			shapeRenderer.setColor(new Color(0.4f, 1f, 0, 1f));
		}
		
		shapeRenderer.rect(rect.x - paddingIn, rect.y-paddingIn, rect.width + paddingIn * 2, rect.height + paddingIn * 2);
		shapeRenderer.end();
		if (opt1Over && focus) {
			font.setColor(new Color(0.06f, 0.3f, 0.5f, 1));
		} else {
			font.setColor(new Color(0.06f, 0.3f, 0, 1));
		}
		
		batch.begin();
		font.draw(batch, text, rect.x, rect.y + rect.height);
		batch.end();
		
		rect.x = tempRect.x;
		rect.y = tempRect.y;
		rect.width = tempRect.width;
		rect.height = tempRect.height;
	}
	
	private void setSay(String text) {
		sayText = text;
		sayTime = 0;
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
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
	
	private boolean dead;
	private Timeline currentTL;
	
	private void setClickV(int screenX, int screenY) {
		clickV.x = (screenX - Gdx.graphics.getWidth() / 2) * zoom + camX;
		clickV.y = (Gdx.graphics.getHeight() / 2f - screenY) * zoom + camY;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		setClickV(screenX, screenY);
		if (button == 0) {
			if (ia1.contains(clickV)) {
				ia1Text = "How are you?";
				opt1 = "> Fine and you my dear?";
				referenceTalk = ia1;
			} else {
				if (opt1Rect.contains(clickV)) {
					ia1Text = "I'm OK";
					opt1 = null;
					referenceTalk = null;
				} else {
					ia1Text = "fuck off";
					opt1 = null;
					referenceTalk = null;
				}				
			}
		}
		if (button == 1) {
			if (move) {
				if (currentTL != null) {
					currentTL.free();
				}
				
				move = false;
				String say = "Hop";
				setSay(say);
			}
			
			if (!dead) {
				
				Vector2 pos = new Vector2(player.x, player.y);
				targetV.x = clickV.x - texturePlayer.getRegionWidth() / 2f;
				targetV.y = clickV.y;
				testV.x = targetV.x;
				testV.y = targetV.y;
				float length = testV.sub(pos).len();
				float time = length / speed + 0.5f;
				currentTL = Timeline.createSequence()
					.push(Tween.call(new TweenCallback() {
						
						@Override
						public void onEvent(int type, BaseTween<?> source) {
							move = true;
							// sayText = null;
							if (effects.size() == 0) {
								PooledEffect effect = pool.obtain();
								effects.add(effect);
							}
						}
					}))
					.push(Tween.to(this, 0, time).target(targetV.x, targetV.y))
					.push(Tween.call(new TweenCallback() {
						
						@Override
						public void onEvent(int type, BaseTween<?> source) {
							clearEffects();
							move = false;							
							String say = "";
							testV.x = mapCoord(player.x + texturePlayer.getRegionWidth() / 2f);
							testV.y = mapCoord(player.y);
							if (passable.contains(testV)) {
								say = "Waazzaaaaaaa ";
							} else {
								say = "Arg suis mort ";
								dead = true;
							}
							
							// say += String.valueOf(testV.x) + ";" + String.valueOf(testV.y);
							setSay(say);
							
						}
					}))
					.start(tweenManager);
			}
		}
		
		return true;
	}
	
	private void clearEffects() {
		for (PooledEffect effect : effects) {
			effect.free();						
		}
		
		effects.clear();
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		setClickV(screenX, screenY);
		if (opt1 != null) {
			opt1Over = opt1Rect.contains(clickV.x, clickV.y);
		}
		
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getValues(Aitrinity target, int tweenType, float[] returnValues) {
		returnValues[0] = target.player.x;
		returnValues[1] = target.player.y;
		return 2;
	}

	@Override
	public void setValues(Aitrinity target, int tweenType, float[] newValues) {
		target.player.x = newValues[0];
		target.player.y = newValues[1];
	}
}
