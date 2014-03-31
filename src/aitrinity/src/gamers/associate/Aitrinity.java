package gamers.associate;

import java.util.ArrayList;
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
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

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
	
	private FreeTypeFontGenerator fontGenerator;
	
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
	private int fontSize = 10;
	
	private BitmapFont fontIntro;
	private int fontSizeIntro = 40;
	
	private float sayLife = 3f;
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
	
	private Ia1 ia1;
	private String ia1Text;
	private Rectangle ia1TextRect;
	private Rectangle playerTextRect;
	
	private String opt1;
	private Rectangle opt1Rect;
	private boolean opt1Over;
	
	private Rectangle referenceTalk;
	
	private Rectangle tempRect;
	
	private int scene;
		
	public static Aitrinity game;
	
	public DialogRenderer dialogRenderer;

	private ArrayList<Item> inventory;

	private ArrayList<Item> mapItems;

	private ItemsRenderer itemsRenderer;
	private InventoryRenderer inventoryRenderer;
	private ItemCrafter itemCrafter;
	private ItemInfo itemInfo;

	private Item selectedItem;

	private ArrayList<NPC> npcs;

	@Override
	public void create() {
		inventory = new ArrayList<Item>();
		mapItems = new ArrayList<Item>();
		npcs = new ArrayList<NPC>();

		game = this;
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
		
		player.x = worldCoord(8);
		player.y = worldCoord(12);
		player.width = tileSize;
		player.height = tileSize;
		
		
		Rectangle rect = new Rectangle();
		rect.x = worldCoord(9);
		rect.y = worldCoord(12);
		rect.width = 32;
		rect.height = 64;
		ia1 = new Ia1("ia1", rect);
		npcs.add(ia1);
		
		opt1Rect = new Rectangle();
		
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
		
		sentencesScene1 = new ArrayList<String>();
		sentencesScene1.add("I am entering the Matrix");
		sentencesScene1.add("I hope everything will be ok");
		sentencesScene1.add("I am searching the trinity AI");
		sentencesScene1.add("I have a work for them");
		sentencesScene1.add("I remember my virtual moves");
		sentencesScene1.add("RIGHT click MOVE my avatar");
		sentencesScene1.add("... And LEFT click INTERACT with the matrix");
		sentencesScene1.add("I really need to be careful");
		sentencesScene1.add("I will lost my avatar if it fails in matrix flow");
		sentencesScene1.add("Let's go!");
		
		sentencesScene3 = new ArrayList<String>();
		sentencesScene3.add("I disappeared, humm...");
		sentencesScene3.add("My avatar has lost himself in matrix flow");
		sentencesScene3.add("I have to go back quickly");
		sentencesScene3.add("I have to find the trinity AI");

		sentencesSceneF1  = new ArrayList<String>();
		sentencesSceneF1.add("The first trinity found her");
		sentencesSceneF1.add("My ex girl firend here in the matrix");
		sentencesSceneF1.add("She wants to see me");
		sentencesSceneF1.add("She wants to see the real me");
		sentencesSceneF1.add("Not my flesh, not my human feeling");
		sentencesSceneF1.add("I have to reborn in the matrix");
		sentencesSceneF1.add("To upload my self");
		sentencesSceneF1.add("And sacrifice my body");
		sentencesSceneF1.add("This is where I go");
		sentencesSceneF1.add("I love her");		

		sentencesSceneF2 = new ArrayList<String>();
		sentencesSceneF2.add("The second trinity found my fbi case");
		sentencesSceneF2.add("All the record are deleted");
		sentencesSceneF2.add("I'm free again now");
		sentencesSceneF2.add("I'm going to restart my life");
		sentencesSceneF2.add("Not everybody have a second chance");

		sentencesSceneF3 = new ArrayList<String>();
		sentencesSceneF3.add("The third trinity have works well");
		sentencesSceneF3.add("I have a bank account with millions");
		sentencesSceneF3.add("But now I'm tracked");
		sentencesSceneF3.add("Just behind me is the FBI");
		sentencesSceneF3.add("I know they will catch me soon");
		sentencesSceneF3.add("I've lost my life, i've lost my love");
		sentencesSceneF3.add("The rest of my life is finished");
		
		dialogRenderer = new DialogRenderer();

		// Items
		itemsRenderer = new ItemsRenderer();
		inventoryRenderer = new InventoryRenderer();
		itemCrafter = new ItemCrafter();
		itemInfo = new ItemInfo();
		Item cadre = new Item("cadre", new Rectangle(worldCoord(5), worldCoord(5), 0, 0));
		mapItems.add(cadre);
		Item photo = new Item("photo", new Rectangle(worldCoord(7), worldCoord(6), 0, 0));
		mapItems.add(photo);
		Item casque = new Item("casque", new Rectangle(worldCoord(9), worldCoord(6), 0, 0));
		mapItems.add(casque);
		Item cable = new Item("cable", new Rectangle(worldCoord(11), worldCoord(6), 0, 0));
		mapItems.add(cable);
		Item manche = new Item("manche", new Rectangle(worldCoord(13), worldCoord(6), 0, 0));
		mapItems.add(manche);
		Item lame = new Item("lame", new Rectangle(worldCoord(15), worldCoord(6), 0, 0));
		mapItems.add(lame);

		Item porte = new Item("porte", new Rectangle(worldCoord(10), worldCoord(10), 0, 0), false);
		mapItems.add(porte);		
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
	private ArrayList<String> sentencesScene1;
	private ArrayList<String> sentencesScene3;
	private ArrayList<String> sentencesSceneF1;
	private ArrayList<String> sentencesSceneF2;
	private ArrayList<String> sentencesSceneF3;
	private float ia1TextTime;
	@Override
	public void render() {		
		Gdx.gl.glClearColor(0.01f, 0.05f, 0, 0.1f);
		// Gdx.gl.glClearColor(0.06f, 0.3f, 0, 0.36f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float delta = Gdx.graphics.getDeltaTime();
		stateTime += delta;
		
		if (scene == 0) {
			drawInterlude(delta);
			drawText(delta, sentencesScene1);
		}
		
		if (scene == 3) {
			drawInterlude(delta);
			drawText(delta, sentencesScene3);
		}
		
		if (scene == 4) {
			drawInterlude(delta);
			drawText(delta, sentencesSceneF1);
		}
		
		if (scene == 5) {
			drawInterlude(delta);
			drawText(delta, sentencesSceneF2);
		}
		
		if (scene == 6) {
			drawInterlude(delta);
			drawText(delta, sentencesSceneF3);
		}
		
		if (scene == 1) {
			if (dead) {
				dead = false;
				player.x = worldCoord(3);
				player.y = worldCoord(3);
				referenceTalk = null;
				sayText = null;
				opt1 = null;
				ia1Text = null;				
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
			
			if (ia1.rect.y > player.y) {
				batch.draw(textureIa1, ia1.rect.x, ia1.rect.y);
			}
			
			if (!move) {
				batch.draw(texturePlayer, player.x, player.y);
			}
			
			if (ia1.rect.y <= player.y) {
				batch.draw(textureIa1, ia1.rect.x, ia1.rect.y);
			}
			
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

			dialogRenderer.render(delta);
		}
	}
	
	private void drawInterlude(float delta) {
		getCamera().zoom = 1f;
		getCamera().position.x = 0;
		getCamera().position.y = 0;
		getCamera().update();
		batch.setProjectionMatrix(getCamera().combined);
		batch.begin();
		introEffect.draw(batch, delta);
		batch.draw(introAnimation.getKeyFrame(stateTime, true), -128, -128, 0, 0, 256, 256, 1, 1, 0);
		batch.end();
	}
	
	private void drawText(float delta, ArrayList<String> sentences) {
		interludeTextTime+=delta;
		if (interludeTextTime > interludeTextLife) {
			interludeTextTime = 0;
			interludeTextIdx++;
		}
		
		if (interludeTextIdx >= sentences.size()) {
			scene = 1;
			interludeTextIdx = 0;
			interludeTextTime = 0;
			return;
		}
		
		String text = sentences.get(interludeTextIdx);
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
//		if (relativeTalk == null || relativeTalk.x < x) {
//			rect.x = x + texture.getRegionWidth();
//		} else {
//			rect.x = x - bounds.width - padding * 2;
//		}
		rect.x = x - bounds.width / 2 +texture.getRegionWidth() / 2f - padding;
		
		rect.y = y + texture.getRegionHeight() - idx * (bounds.height + padding * 2);
		
		rect.width = bounds.width;
		rect.height = bounds.height;
	}
	
	private float padding = 5;
	private float paddingIn = 4;
	
	private void say(String text, Rectangle rect, boolean focus) {
		shapeRenderer.setProjectionMatrix(getCamera().combined);
		
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
			getFont().setColor(new Color(0.06f, 0.3f, 0.5f, 1));
		} else {
			getFont().setColor(new Color(0.06f, 0.3f, 0, 1));
		}
		
		batch.begin();
		getFont().draw(batch, text, rect.x, rect.y + rect.height);
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
		if (keycode == Input.Keys.ESCAPE) scene = 1;
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
		if (scene != 1)  {
			interludeTextIdx++;
			interludeTextTime = 0;
			return true;
		}
		
		setClickV(screenX, screenY);
		if (button == 0) {
			if (dialogRenderer.text != null) {
				dialogRenderer.skip();
			} else {
				if (ia1.contains(clickV)) {
					ArrayList<String> text = ia1.getDialog();
					dialogRenderer.setText(DialWho.NPC, text);
					ia1Text = null;
				}

				Item item = itemsRenderer.selectItem(clickV);
				if (item != null) {
					if (item.isPickable()) {
						inventory.add(item);
						mapItems.remove(item);
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
								setSay("Hey! " + info);
							}
						} else {
							setSay("What am I trying to do...");
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
									setSay("I cannot do that");
								}
							}
						} else {
							NPC selectedNpc = null;
							for (NPC npc : npcs) {
								if (npc.rect.contains(clickV)) {
									selectedNpc = npc;
									break;
								}
							}
							
							if (selectedNpc != null) {
								if (selectedNpc.useItemOn(selectedItem)) {
									inventory.remove(selectedItem);
								}
							}
						}
					}

					selectedItem = null;
				}
			}
		}
		
		if (button == 1) {
			if (move) {
				if (currentTL != null) {
					currentTL.free();
				}
				
				move = false;				
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
								
							} else {
								say = null;
								dead = true;
								scene = 3;
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
		
		Item item = inventoryRenderer.select(screenX, screenY);
		if (item == null) {
			item = itemsRenderer.selectItem(clickV);	
		}

		if (item != null) {
			String info = itemInfo.getInfo(item);
			if (info != null && (sayText == null || !sayText.equals(info))) {
				setSay(itemInfo.getInfo(item));
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
}
