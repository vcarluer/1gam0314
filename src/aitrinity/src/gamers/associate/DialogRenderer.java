package gamers.associate;	

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DialogRenderer {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private int fontSize = 35;	
	int paddingH = 20;
	int paddingW = 50;
	int dialogH = 200;
	private float paddingInH = 5;
	private float paddingInW = 10;
	
	private boolean optRectInit;
	private float sayLife = 3f;
	private float sayTime;
	private int sayIdx;
	
	public DialogRenderer() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		font = Aitrinity.game.getFontGenerator().generateFont(fontSize);
		font.setColor(Color.WHITE);		
	}
	
	private void renderTextPC(String write) {
		batch.begin();		
		TextBounds bounds = font.getBounds(write);
		font.setColor(Color.WHITE);
		font.setColor(Color.WHITE);
		font.draw(batch, write, 
				-Gdx.graphics.getWidth() / 2f + paddingW + paddingInW, 
				- Gdx.graphics.getHeight() / 2f + paddingH + dialogH / 2 + bounds.height / 2);
		batch.end();
	}
	
	private void renderTextNPC(String write) {
		batch.begin();		
		TextBounds bounds = font.getBounds(write);
		font.setColor(Color.WHITE);
		font.draw(batch, write, -Gdx.graphics.getWidth() / 2f + paddingW + paddingInW, Gdx.graphics.getHeight() / 2f - paddingH - dialogH / 2f + bounds.height / 2f);
		batch.end();
	}
	
	private void renderBackNPC() {
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.rect(-Gdx.graphics.getWidth() / 2f + paddingW  , Gdx.graphics.getHeight() / 2f - paddingH - dialogH, Gdx.graphics.getWidth() - 2 * paddingW, dialogH);
		shapeRenderer.end();
	}
	
	private void renderBackPC() {
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(-Gdx.graphics.getWidth() / 2f + paddingW  , - Gdx.graphics.getHeight() / 2f + paddingH, Gdx.graphics.getWidth() - 2 * paddingW, dialogH);
		shapeRenderer.end();
	}
	
	
	private Vector2 getCamCorrection(int screenX, int screenY) {
		Vector2 clickV = new Vector2();
		clickV.x = (screenX - Gdx.graphics.getWidth() / 2);
		clickV.y = (Gdx.graphics.getHeight() / 2f - screenY);
		return clickV;
	}
	
	private DialWho who;
	public ArrayList<String> text;
	private int idx;
	
	public void setText(DialWho who, ArrayList<String> text) {
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
				if (this.who == DialWho.NPC) {
					renderBackNPC();
					renderTextNPC(text.get(idx));
				} else {
					renderBackPC();
					renderTextPC(text.get(idx));
				}
			}			
		}
	}
	
	public void skip() {
		text = null;
	}
}
