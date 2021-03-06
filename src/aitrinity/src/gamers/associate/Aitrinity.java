package gamers.associate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
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
	private Animation introAnimation;
	private float stateTime;
	private ShapeRenderer shapeRenderer;
	
	private TextureAtlas atlas;
	private BitmapFont font;
	
	public FreeTypeFontGenerator fontGenerator;
	
	private ParticleEffectPool pool;
	private HashSet<PooledEffect> effects;
	private ParticleEffect bkgEffect;
	private ParticleEffect introEffect;
	
	public Rectangle player;
	private Vector2 targetV;
	private Vector2 clickV;
	
	private TweenManager tweenManager;
	private float zoom = 0.5f;
	
	private boolean move;
	private int fontSize = 12;
	
	private BitmapFont fontIntro;
	private BitmapFont fontTitle;
	private int fontSizeIntro = 30;
	private int fontSizeTitle = 60;
	
	private float sayLife = 2f;
	private String sayText;
	private float sayTime;
	
	private float speed = 500;
	
	private TiledMap room0;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private float camX;
	private float camY;
	
	private float mapRatio = 2F;
	private float baseTileSize = 16;
	public float tileSize = baseTileSize * mapRatio;
	
	private HashSet<Vector2> passable;
	private Vector2 testV;
	
	private Ia1 ia1;
	private Ia2 ia2;
	private Ia3 ia3;
	private Rectangle playerTextRect;
	
	private Rectangle referenceTalk;
	
	private Rectangle tempRect;
	
	public int scene;
		
	public static Aitrinity game;
	
	public DialogRenderer dialogRenderer;

	public ArrayList<Item> inventory;

	private ArrayList<Item> mapItems;

	private ItemsRenderer itemsRenderer;
	private InventoryRenderer inventoryRenderer;
	private ItemCrafter itemCrafter;
	public ItemInfo itemInfo;

	private Item selectedItem;

	private ArrayList<NPC> npcs;
	
	private HashMap<String, Item> otherItems;
	private float yStart;
	private float xStart;
	
	public Color ia2Tint = new Color(0.9f, 0.3f, 0f, 1f);
	public Color ia3Tint = new Color(0f, 0.3f, 0.9f, 1f);
	// public Color iaMeldTint = new Color(0.5f, 0.1f, 0.5f, 0.5f);
	private int iaMeld;
	
	private Sound moveSound;
	public Sound selectSound;
	public Sound music;
	
	public String lang = "en"; // or fr

	@Override
	public void create() {
		inventory = new ArrayList<Item>();
		mapItems = new ArrayList<Item>();
		npcs = new ArrayList<NPC>();
		otherItems = new HashMap<String, Item>();

		game = this;
		passable = new HashSet<Vector2>();
		testV = new Vector2();
		targetV = new Vector2();
		clickV = new Vector2();
		player = new Rectangle();
		playerTextRect = new Rectangle();
		tempRect = new Rectangle();
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		xStart = worldCoord(25);
		player.x = xStart;
		yStart = worldCoord(18);
		player.y = yStart;
		player.width = tileSize;
		player.height = tileSize;
		
		
		Rectangle rect = new Rectangle();
		rect.x = worldCoord(5);
		rect.y = worldCoord(12);
		rect.width = 32;
		rect.height = 64;
		ia1 = new Ia1("ia1", rect);
		npcs.add(ia1);
		
		Rectangle rect2 = new Rectangle();
		rect2.x = worldCoord(14);
		rect2.y = worldCoord(31);
		rect2.width = 32;
		rect2.height = 64;
		ia2 = new Ia2("ia2", rect2);
		npcs.add(ia2);
		
		Rectangle rect3 = new Rectangle();
		rect3.x = worldCoord(49);
		rect3.y = worldCoord(8);		
		rect3.width = 32;
		rect3.height = 64;
		ia3 = new Ia3("ia3", rect3);
		npcs.add(ia3);
		
		setCamera(new OrthographicCamera(w, h));
		setCamX((w / 2f) * zoom);
		setCamY((h / 2f) * zoom);
		getCamera().translate(getCamX(), getCamY());
		getCamera().zoom = zoom;
		getCamera().update();
		batch = new SpriteBatch();
		stateTime = 0f;
		
		// second method
		atlas = new TextureAtlas(Gdx.files.internal("data/pack.atlas"));
		activateFrames = atlas.findRegions("player/idle");
		
		activateAnimation = new Animation(0.12f, activateFrames);
		
		ia1Frames = atlas.findRegions("ia1/activate");
		ia1Animation = new Animation(0.12f, ia1Frames);
		
		setFontGenerator(new FreeTypeFontGenerator(Gdx.files.internal("data/upheavtt.ttf")));
		setFont(getFontGenerator().generateFont(fontSize));
		getFont().setColor(Color.BLACK);
		fontIntro = getFontGenerator().generateFont(fontSizeIntro);
		fontIntro.setColor(Color.WHITE);
		fontTitle = getFontGenerator().generateFont(fontSizeTitle);
		
		effects = new HashSet<PooledEffect>();
		ParticleEffect prototype = new ParticleEffect();
		prototype.load(Gdx.files.internal("data/zerogreen2.p"), atlas);
		pool = new ParticleEffectPool(prototype, 1, 2);
		
		bkgEffect = new ParticleEffect();
		bkgEffect.load(Gdx.files.internal("data/bkg.p"), atlas);
		introEffect = new ParticleEffect();
		introEffect.load(Gdx.files.internal("data/zerogreenintro.p"), atlas);
		introAnimation = new Animation(0.15f, atlas.findRegions("player/introfly"));
		
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
		
		
		
		dialogRenderer = new DialogRenderer();

		// Items
		itemsRenderer = new ItemsRenderer();
		inventoryRenderer = new InventoryRenderer();
		itemCrafter = new ItemCrafter();
		itemInfo = new ItemInfo();
		
		float itemSize = 16;
		Item photo = new Item("photo", new Rectangle(worldCoord(47), worldCoord(21), itemSize, itemSize));
		mapItems.add(photo);
		
		Item cable = new Item("cable", new Rectangle(worldCoord(26), worldCoord(7), itemSize, itemSize));
		mapItems.add(cable);
		
		Item lame = new Item("lame", new Rectangle(worldCoord(7), worldCoord(6), itemSize, itemSize));
		mapItems.add(lame);

		Item porte = new Item("porte", new Rectangle(worldCoord(41), worldCoord(34), itemSize * 2, itemSize * 2), false);
		mapItems.add(porte);		
		
		Item casque = new Item("casque", new Rectangle(worldCoord(9), worldCoord(6), 0, 0));
		otherItems.put(casque.id, casque);
		Item cadre = new Item("cadre", new Rectangle(worldCoord(5), worldCoord(5), 0, 0));
		otherItems.put(cadre.id, cadre);
		Item manche = new Item("manche", new Rectangle(worldCoord(13), worldCoord(6), 0, 0));
		otherItems.put(manche.id, manche);
		Item cle1 = new Item("cle1", new Rectangle(0, 0, 0, 0));
		otherItems.put(cle1.id, cle1);
		Item cle2 = new Item("cle2", new Rectangle(0, 0, 0, 0));
		otherItems.put(cle2.id, cle2);
		Item cle3 = new Item("cle3", new Rectangle(0, 0, 0, 0));
		otherItems.put(cle3.id, cle3);
		
		
		
		dead = true;
		float iaSize = 256;
		choix1 = new Rectangle(- iaSize / 4f, - iaSize / 2f, iaSize / 2f, iaSize);
		choix2 = new Rectangle(iaSize - iaSize / 4f, - iaSize / 2f, iaSize / 2f, iaSize);
		choix3 = new Rectangle(iaSize * 2 - iaSize / 4f, - iaSize / 2f, iaSize / 2f, iaSize);
		
		moveSound = Gdx.audio.newSound(Gdx.files.internal("data/move.wav"));
		selectSound = Gdx.audio.newSound(Gdx.files.internal("data/select.wav"));
		music = Gdx.audio.newSound(Gdx.files.internal("data/music.mp3"));
		
		scene = -1;		
		music.loop();
	}
	
	public void takeOtherItem(String id) {
		if (otherItems.containsKey(id)) {
			inventory.add(otherItems.get(id));
			otherItems.remove(id);
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
	
	private float interludeTextLife = 3.5f;
	private float interludeTextTime;
	private int interludeTextIdx = 0;
	private String[] sentencesScene1;
	private String[] sentencesScene3;
	private String[] sentencesSceneF;
	private String[] sentencesSceneFF;
	private String[] sentencesSceneF1;
	private String[] sentencesSceneF2;
	private String[] sentencesSceneF3;
	private String[] sentencesSceneF4;
	
	private Rectangle choix1;
	private Rectangle choix2;
	private Rectangle choix3;
	private boolean init;
	
	@Override
	public void render() {		
		Gdx.gl.glClearColor(0.01f, 0.05f, 0, 0.1f);
		// Gdx.gl.glClearColor(0.06f, 0.3f, 0, 0.36f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float delta = Gdx.graphics.getDeltaTime();
		stateTime += delta;
		
		TextureRegion textureIa1 = ia1Animation.getKeyFrame(stateTime, true);
		
		if (scene == -1) {
			drawInterlude(delta);
			batch.begin();
			TextBounds bounds = fontTitle.getBounds("AI TRINITY");
			fontTitle.draw(batch, "AI TRINITY", - bounds.width / 2f, Gdx.graphics.getHeight() / 4);
			
			
			bounds = fontIntro.getBounds("Choose your language:");
			fontIntro.draw(batch, "Choose your language:", - bounds.width / 2f, bounds.height * 3 - Gdx.graphics.getHeight() / 4);
			bounds = fontIntro.getBounds("Poor [E]nglish");
			fontIntro.draw(batch, "Poor [E]nglish", - bounds.width / 2f, 0 - Gdx.graphics.getHeight() / 4);
			bounds = fontIntro.getBounds("[F]ran�ais");
			fontIntro.draw(batch, "[F]ran�ais", - bounds.width / 2f, 0 - bounds.height * 3 - Gdx.graphics.getHeight() / 4);
			
			batch.end();
			
			init = false;
		}
		
		if (scene == 0) {
			if (!init) {
				initSentences();
				init = true;
			}
			
			drawInterlude(delta);
			drawText(delta, sentencesScene1);
		}
		
		if (scene == 3) {
			drawInterlude(delta);
			drawText(delta, sentencesScene3);
		}
		
		if (scene == 40) {
			drawInterlude(delta, -512, -128);
			drawText(delta, sentencesSceneF);
			batch.begin();
			Rectangle choix = choix1;
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			choix = choix2;
			batch.setColor(ia2Tint);
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			choix = choix3;
			batch.setColor(ia3Tint);
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			batch.end();

			batch.setColor(Color.WHITE);
		}
		
		if (scene == 4) {
			drawInterlude(delta, -512, -128);
			drawText(delta, sentencesSceneFF);
			batch.begin();
			Rectangle choix = choix1;
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			choix = choix2;
			batch.setColor(ia2Tint);
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			choix = choix3;
			batch.setColor(ia3Tint);
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			batch.end();

			batch.setColor(Color.WHITE);
		}
		
		if (scene == 5) {
			drawInterlude(delta, -512, -128);
			batch.begin();
			Rectangle choix = choix1;
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			batch.setColor(Color.WHITE);
			batch.end();
			drawText(delta, sentencesSceneF1);
		}
		
		if (scene == 6) {
			drawInterlude(delta, -512, -128);
			batch.begin();			
			Rectangle choix = choix2;
			batch.setColor(ia2Tint);
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			batch.setColor(Color.WHITE);
			batch.end();
			
			drawText(delta, sentencesSceneF2);
		}
		
		if (scene == 7) {
			drawInterlude(delta, -512, -128);
			batch.begin();
			Rectangle choix = choix3;
			batch.setColor(ia3Tint);
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			batch.setColor(Color.WHITE);
			batch.end();
			drawText(delta, sentencesSceneF3);
		}
		
		if (scene == 8) {
			drawInterlude(delta, -512, -128);
			batch.begin();			
			Rectangle choix = choix2;
			if (iaMeld == 0) {
				batch.setColor(Color.WHITE);
				iaMeld++;
			} else {
				if (iaMeld == 1) {
					batch.setColor(ia2Tint);
					iaMeld++;
				} else {
					if (iaMeld == 2) {
						batch.setColor(ia3Tint);
						iaMeld = 0;
					}
				}								
			}
			
			batch.draw(textureIa1, choix.x, choix.y, choix.width, choix.height);
			batch.setColor(Color.WHITE);
			batch.end();
			drawText(delta, sentencesSceneF4);
		}
		
		if (scene == 9)  {
			batch.begin();
			TextBounds bounds = fontTitle.getBounds("AI TRINITY");
			fontTitle.draw(batch, "AI TRINITY", - bounds.width / 2f, Gdx.graphics.getHeight() / 4);
			bounds = fontTitle.getBounds("GAME OVER");
			fontTitle.draw(batch, "GAME OVER", - bounds.width / 2f, - Gdx.graphics.getHeight() / 4);
			batch.end();
		}
		
		if (scene == 1) {
			
			texturePlayer = activateAnimation.getKeyFrame(stateTime, true);
			
			if (dead) {
				music.stop();
				moveSound.play(0.3f);
				dead = false;
				player.x = worldCoord(-5);
				player.y = yStart;
				referenceTalk = null;
				sayText = null;
				
				clickV.x = xStart;
				clickV.y = yStart;
				
				movePlayer();
			}
					
			setCamX(player.x);
			setCamY(player.y);
			getCamera().position.x = player.x;
			getCamera().position.y = player.y;
			getCamera().zoom = zoom;
			getCamera().update();
			batch.setProjectionMatrix(getCamera().combined);
			
			mapRenderer.setView(getCamera());
			
			
			batch.begin();
			// bkgEffect.setPosition(x, y);
			bkgEffect.draw(batch, delta);
			batch.end();
			
			mapRenderer.render();
			itemsRenderer.render(batch, mapItems);

			batch.begin();
			// sprite.draw(batch);
			
			tweenManager.update(delta);
			
			
			
			
			for(PooledEffect effect : effects) {
				effect.setPosition(player.x + texturePlayer.getRegionWidth() / 2f, player.y+texturePlayer.getRegionHeight() / 2f);
				effect.draw(batch, delta);
				if (effect.isComplete()) {
					effect.free();
					effects.remove(effect);
				}
			}
			
			if (ia1.rect.y > player.y) {
				batch.draw(textureIa1, ia1.rect.x, ia1.rect.y);				
			}
			
			if (ia2.rect.y > player.y) {
				batch.setColor(ia2Tint);
				batch.draw(textureIa1, ia2.rect.x, ia2.rect.y);				
			}
			
			if (ia3.rect.y > player.y) {
				batch.setColor(ia3Tint);
				batch.draw(textureIa1, ia3.rect.x, ia3.rect.y);				
			}
			batch.setColor(Color.WHITE);
			
			if (!move) {
				batch.draw(texturePlayer, player.x, player.y);
			}
			
			if (ia1.rect.y <= player.y) {
				batch.draw(textureIa1, ia1.rect.x, ia1.rect.y);
			}
			
			if (ia2.rect.y <= player.y) {
				batch.setColor(ia2Tint);
				batch.draw(textureIa1, ia2.rect.x, ia2.rect.y);				
			}
			
			if (ia3.rect.y <= player.y) {
				batch.setColor(ia3Tint);
				batch.draw(textureIa1, ia3.rect.x, ia3.rect.y);				
			}
			batch.setColor(Color.WHITE);
			
			batch.end();
			
			if (sayText != null && !sayText.equals("")) {
				sayTime += delta;
				if (sayTime < sayLife) {
					say(sayText);
				} else {
					sayText = null;
				}
			}
			
			if (dialogRenderer.text == null) {
				inventoryRenderer.render(inventory);
			}
			
			if (selectedItem != null) {
				inventoryRenderer.renderItem(selectedItem, Gdx.input.getX(), Gdx.input.getY());
			}
			
			inventoryRenderer.renderInfo();

			dialogRenderer.render(delta);
		}
	}

	private void initSentences() {
		sentencesScene1 = TextReader.get("scene0");				
		sentencesScene3 = TextReader.get("scene3");				
		sentencesSceneF1 = TextReader.get("sceneF1");				
		sentencesSceneF = TextReader.get("sceneF");				
		sentencesSceneFF = TextReader.get("sceneFF");
		
		sentencesSceneF2 = TextReader.get("sceneF2");
		sentencesSceneF3 = TextReader.get("sceneF3");
						
		sentencesSceneF4 = TextReader.get("sceneF4");
	}
	
	private void drawInterlude(float delta) {
		drawInterlude(delta, -128, -128);
	}
	
	private void drawInterlude(float delta, float x, float y) {
		getCamera().zoom = 1f;
		getCamera().position.x = 0;
		getCamera().position.y = 0;
		getCamera().update();
		batch.setProjectionMatrix(getCamera().combined);
		batch.begin();
		introEffect.draw(batch, delta);
		batch.draw(introAnimation.getKeyFrame(stateTime, true), x, y, 0, 0, 256, 256, 1, 1, 0);
		batch.end();
	}
	
	private void drawText(float delta, String[] sentences) {
		interludeTextTime+=delta;
		if (interludeTextTime > interludeTextLife) {
			interludeTextTime = 0;
			interludeTextIdx++;
		}
		
		if (interludeTextIdx >= sentences.length) {
			interludeTextIdx = 0;
			interludeTextTime = 0;
			
			if (scene < 4) {
				scene = 1;
				return;
			} else {
				if (scene > 4 && scene < 40) {
					scene = 9;
					return;
				} else {
					if (scene == 4) {
						interludeTextIdx = sentences.length - 1;
					} else {
						scene = 4;
						return;
					}
				}
			}			
		}
		
		String text = sentences[interludeTextIdx];
		TextBounds bounds = fontIntro.getBounds(text);
		tempRect.x = - bounds.width / 2f;
		tempRect.y = -300;
		tempRect.width = bounds.width;
		tempRect.height = bounds.height;
		
		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0.4f, 1f, 0, 1f));
		int padding = 25;
		shapeRenderer.rect(-Gdx.graphics.getWidth() / 2f + padding, tempRect.y - padding, Gdx.graphics.getWidth() - padding * 2, tempRect.height + padding * 2);
		shapeRenderer.end();
		batch.begin();
		fontIntro.draw(batch, text, tempRect.x, tempRect.y + tempRect.height);
		batch.end();
	}
	
	private void say(String text) {
		setSayRect(text, 0, texturePlayer, player.x, player.y, playerTextRect, referenceTalk);
		say(text,  playerTextRect, false);
	}
	
	private void setSayRect(String text, int idx, TextureRegion texture, float x, float y, Rectangle rect, Rectangle relativeTalk) {
		TextBounds bounds = getFont().getBounds(text);
		rect.x = x - bounds.width / 2 +texture.getRegionWidth() / 2f - padding;
		
		rect.y = y + texture.getRegionHeight() - idx * (bounds.height + padding * 2);
		
		rect.width = bounds.width;
		rect.height = bounds.height;
	}
	
	private float padding = 5;
	private float paddingIn = 4;
	
	private void say(String text, Rectangle rect, boolean focus) {
		shapeRenderer.setProjectionMatrix(getCamera().combined);
		
		drawText(text, rect, shapeRenderer, font, batch);
		
		rect.x = tempRect.x;
		rect.y = tempRect.y;
		rect.width = tempRect.width;
		rect.height = tempRect.height;
	}

	public void drawText(String text, Rectangle rect, ShapeRenderer localShapeRenderer, BitmapFont bitmapFont, SpriteBatch spriteBatch) {
		localShapeRenderer.begin(ShapeType.Filled);
		
		localShapeRenderer.setColor(Color.GREEN);
		tempRect.x = rect.x - padding;
		tempRect.y = rect.y-padding;
		tempRect.width = rect.width + padding * 2;
		tempRect.height = rect.height + padding * 2;
		localShapeRenderer.rect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
		localShapeRenderer.setColor(new Color(0.4f, 1f, 0, 1f));		
		
		localShapeRenderer.rect(rect.x - paddingIn, rect.y-paddingIn, rect.width + paddingIn * 2, rect.height + paddingIn * 2);
		localShapeRenderer.end();
		bitmapFont.setColor(new Color(0.06f, 0.3f, 0, 1));		
		
		spriteBatch.begin();
		bitmapFont.draw(spriteBatch, text, rect.x, rect.y + rect.height);
		spriteBatch.end();
	}
	
	public void setSay(GameText text) {
		setSay(text.get());		
	}
	
	public void setSay(String text) {
		sayText = text;
		sayTime = 0;
	}

	@Override
	public void resize(int width, int height) {
		getCamera().viewportWidth = width;
		getCamera().viewportHeight = height;
		getCamera().update();
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
		if (scene == -1) {
			if (keycode == Input.Keys.E) {
				lang = "en";
				scene = 0;
			}
			
			if (keycode == Input.Keys.F) {
				lang = "fr";
				scene = 0;
			}
			
			return true;
		}
		
		
		if (scene == 0 || scene == 3) {
			if (keycode == Input.Keys.ESCAPE) scene = 1;
		}
		
		return true;
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
		clickV.x = (screenX - Gdx.graphics.getWidth() / 2) * zoom + getCamX();
		clickV.y = (Gdx.graphics.getHeight() / 2f - screenY) * zoom + getCamY();
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (scene == -1) {
			scene = 0;
			return true;
		}
		
		if (scene == 9) {
			scene = -1;
			return true;
		}
		
		if (scene == 0 || scene == 3) {
			if (button == 0) {
				interludeTextIdx++;
				interludeTextTime = 0;
			} else {
				scene = 1;
			}
			
			return true;
		}
		
		if (scene > 4) {
			if (button == 0) {
				interludeTextIdx++;
				interludeTextTime = 0;
			} else {
				if (scene < 40) {
					scene = 9;
				} else {
					scene = 4;
				}
			}
			
			return true;
		}
		
		if (scene == 4) {
			clickV.x = (screenX - Gdx.graphics.getWidth() / 2);
			clickV.y = (Gdx.graphics.getHeight() / 2f - screenY);
			interludeTextTime = 0;
			interludeTextIdx = 0;
			
			if (choix1.contains(clickV)) {
				scene = 5;
				return true;
			}
			
			if (choix2.contains(clickV)) {
				scene = 6;
				return true;
			}
			
			if (choix3.contains(clickV)) {
				scene = 7;
				return true;
			}
			
			scene = 8;
			return true;
			
		}
		
		
		setClickV(screenX, screenY);
		if (button == 0) {
			if (dialogRenderer.text != null) {
				dialogRenderer.skip();
			} else {
				if (ia1.contains(clickV) && selectedItem == null) {
					ArrayList<DialInfo> text = ia1.getDialog();
					dialogRenderer.setText(text);
					return true;
				}
				
				if (ia2.contains(clickV) && selectedItem == null) {
					ArrayList<DialInfo> text = ia2.getDialog();
					dialogRenderer.setText(text);
					return true;
				}
				
				if (ia3.contains(clickV) && selectedItem == null) {
					ArrayList<DialInfo> text = ia3.getDialog();
					dialogRenderer.setText(text);
					return true;
				}

				Item item = itemsRenderer.selectItem(clickV);
				if (item != null && selectedItem == null) {
					if (item.isPickable()) {
						inventory.add(item);
						mapItems.remove(item);
						Aitrinity.game.selectSound.play();
					}
				}

				item = inventoryRenderer.select(screenX, screenY);

				if (item != null) {
					if (selectedItem == null) {
						selectedItem = item;
					} else {
						Item crafted = itemCrafter.combine(selectedItem, item);
						if (crafted != null) {
							inventory.remove(item);
							inventory.remove(selectedItem);
							inventory.add(crafted);
							String info = itemInfo.getInfo(crafted);
							if (info != null) {
								setSay(new GameText("H� !" + info, "Hey! " + info));
							}
						} else {
							setSay(new GameText("Qu'est-ce que j'essaie de faire...", "What am I trying to do..."));
						}

						selectedItem = null;
					}
				} else {
					if (selectedItem != null) {
						Item mapItem = itemsRenderer.selectItem(clickV);
						if (mapItem != null) {
							if (!mapItem.useItemOn(selectedItem)) {	
								String say = itemCrafter.useOn(selectedItem, mapItem);
								if (say != null) {
									setSay(say);
									mapItems.remove(mapItem);
									inventory.remove(selectedItem);
								} else {
									setSay(new GameText("Je ne peux pas faire �a", "I cannot do that"));
								}
							}
						} else {
							NPC selectedNpc = selectNPC();
							
							if (selectedNpc != null) {
								if (selectedNpc.useItemOn(selectedItem)) {
									
								}
							}
						}
					}

					selectedItem = null;
				}
			}
		}
		
		if (button == 1) {
			if (dialogRenderer.text != null) {
				dialogRenderer.cancel();
			} else {
				if (move) {
					if (currentTL != null) {
						currentTL.free();
					}
					
					move = false;				
				}
				
				if (!dead) {
					
					movePlayer();
				}
			}
			
		}
		
		return true;
	}

	private void movePlayer() {
		moveSound.play(0.02f);
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
					testV.x = mapCoord(player.x + texturePlayer.getRegionWidth() / 2f);
					testV.y = mapCoord(player.y);
					if (passable.contains(testV)) {
						
					} else {
						dead = true;
						scene = 3;
						music.loop();
					}
					
				}
			}))
			.start(tweenManager);
	}

	private NPC selectNPC() {
		NPC selectedNpc = null;
		for (NPC npc : npcs) {
			if (npc.rect.contains(clickV)) {
				selectedNpc = npc;
				break;
			}
		}
		return selectedNpc;
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
		
		Item item = inventoryRenderer.select(screenX, screenY);
		if (item == null) {
			setClickV(screenX, screenY);
			item = itemsRenderer.selectItem(clickV);
			if (item == null) {
				item = selectNPC();
			}
		}

		inventoryRenderer.writeInfo(null, 0, 0);
		
		if (item != null) {
			String info = itemInfo.getInfo(item);
			if (info != null && (sayText == null || !sayText.equals(info))) {
				inventoryRenderer.writeInfo(itemInfo.getInfo(item), screenX, screenY);
			}
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

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public float getZoom() {
		return zoom;
	}

	public float getCamX() {
		return camX;
	}

	public void setCamX(float camX) {
		this.camX = camX;
	}

	public float getCamY() {
		return camY;
	}

	public void setCamY(float camY) {
		this.camY = camY;
	}

	public FreeTypeFontGenerator getFontGenerator() {
		return fontGenerator;
	}

	public void setFontGenerator(FreeTypeFontGenerator fontGenerator) {
		this.fontGenerator = fontGenerator;
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public void removeInventory(String id) {
		Item item = null;
		for (Item search : inventory) {
			if (search.id.equals(id)) {
				item =search;
				break;
			}
		}
		
		if (item != null) {
			inventory.remove(item);
		}
	}
}
