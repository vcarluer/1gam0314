package gamers.associate;	

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class DialogRenderer {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private int fontSize = 25;	
	int paddingH = 20;
	int paddingW = 50;
	int dialogH = 100;
	private float paddingInH = 5;
	private float paddingInW = 10;
	
	private boolean optRectInit;
	private float sayLife = 3f;
	private float sayTime;
	private int sayIdx;
	
	private AtlasRegion playerTexture;
	private AtlasRegion ia1Texture;
	
	private int portraitPadding = 10;
	private float portraitSize = dialogH - 2 * portraitPadding;
	
	public DialogRenderer() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		font = Aitrinity.game.getFontGenerator().generateFont(fontSize);
		font.setColor(Color.WHITE);
		
		playerTexture = Aitrinity.game.getAtlas().findRegion("dialplayer");
		ia1Texture = Aitrinity.game.getAtlas().findRegion("dialia1");
	}
	
	private void renderTextPC(String write) {
		renderTextNPC(write);
	}
	
	private void renderTextNPC(String write) {
		batch.begin();		
		TextBounds bounds = font.getBounds(write);
		font.setColor(Color.WHITE);
		font.draw(batch, write, xTextBase + paddingInW, Gdx.graphics.getHeight() / 2f - paddingH - dialogH / 2f + bounds.height / 2f);
		batch.end();
	}
	
	private void renderBackNPC(DialInfo info) {
		shapeRenderer.setColor(new Color(0.1f, 0.3f, 0, 1));
		shapeRenderer.begin(ShapeType.Filled);
		float xBase = -Gdx.graphics.getWidth() / 2f + paddingW;
		float yBase = Gdx.graphics.getHeight() / 2f - paddingH - dialogH;
		shapeRenderer.rect(xBase, yBase, Gdx.graphics.getWidth() - 2 * paddingW, dialogH);
		shapeRenderer.end();
		
		xTextBase = xBase + portraitPadding + portraitSize;
		batch.begin();		
		if (info.talkerId.equals("ia2")) {
			batch.setColor(Aitrinity.game.ia2Tint);
		}
		
		if (info.talkerId.equals("ia3")) {
			batch.setColor(Aitrinity.game.ia3Tint);
		}
		
		batch.draw(ia1Texture, xBase + portraitPadding, yBase + portraitPadding, portraitSize, portraitSize);
		batch.setColor(Color.WHITE);
		batch.end();
	}
	
	private float xTextBase;
	
	private void renderBackPC() {
		shapeRenderer.setColor(new Color(0.1f, 0.3f, 0, 1));
		shapeRenderer.begin(ShapeType.Filled);
		float xBase = -Gdx.graphics.getWidth() / 2f + paddingW;
		float yBase = Gdx.graphics.getHeight() / 2f - paddingH - dialogH;
		shapeRenderer.rect(xBase, yBase, Gdx.graphics.getWidth() - 2 * paddingW, dialogH);
		shapeRenderer.end();
		
		xTextBase = xBase + portraitPadding + portraitSize;
		batch.begin();		
		batch.draw(playerTexture, xBase + portraitPadding, yBase + portraitPadding, portraitSize, portraitSize);
		batch.end();
		
	}
	
	
	private Vector2 getCamCorrection(int screenX, int screenY) {
		Vector2 clickV = new Vector2();
		clickV.x = (screenX - Gdx.graphics.getWidth() / 2);
		clickV.y = (Gdx.graphics.getHeight() / 2f - screenY);
		return clickV;
	}
	
	private DialWho who;
	public ArrayList<DialInfo> text;
	private int idx;
	
	public void setText(ArrayList<DialInfo> text) {
		this.who = who;
		this.text = text;
		idx = 0;
		sayTime = 0;
	}
	
	public void render(float delta) {
		if (text != null) {
			sayTime += delta;
			if (sayTime > sayLife) {
				sayTime = 0;
				idx++;			
			}
			
			if (idx >= text.size()) {
				text = null;
			} else {
				DialInfo dial = text.get(idx);
				boolean say = true;
				if (dial.text.get().startsWith("#")) {
					say = false;
					String id = dial.text.get().substring(1);
					Aitrinity.game.takeOtherItem(id);
					Aitrinity.game.setSay(Aitrinity.game.itemInfo.getInfo(id));
				}
				
				if (dial.text.get().startsWith("-")) {
					say = false;
					String id = dial.text.get().substring(1);
					Aitrinity.game.removeInventory(id);
				}
				
				if (say) {
					if (dial.who == DialWho.NPC) {
						renderBackNPC(dial);
						renderTextNPC(dial.text.get());
					} else {
						renderBackPC();
						renderTextPC(dial.text.get());
					}
				} else {
					sayTime = sayLife;
				}
			}			
		}
	}
	
	public void skip() {
		sayTime = sayLife;
	}

	public void cancel() {
		text = null;
		
	}
}
