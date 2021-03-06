package gamers.associate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InventoryRenderer {
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private HashMap<Rectangle, Item> items;
	private BitmapFont font;
	private Rectangle textInfo;
	private Vector2 vectmp;

	public InventoryRenderer() {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		items = new HashMap<Rectangle, Item>();
		OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setProjectionMatrix(camera.combined);	
		shapeRenderer.setProjectionMatrix(camera.combined);
		font = Aitrinity.game.fontGenerator.generateFont(26);
		textInfo = new Rectangle();
		vectmp = new Vector2();
	}

	public void render(ArrayList<Item> inventory) {
		if (inventory.size() > 0) {
			int marge = 10;
			int padding = 10;
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(new Color(0.1f, 0.3f, 0, 1));
			shapeRenderer.rect(-Gdx.graphics.getWidth() / 2f + marge, -Gdx.graphics.getHeight() / 2f + marge, Gdx.graphics.getWidth() - 2 * marge, 64);
			shapeRenderer.end();
			batch.begin();
			int i = 0;
			items.clear();
			for (Item item : inventory) {
				AtlasRegion region = ItemsRenderer.instance.getTexture(item.id);
				int size = 60;
				Rectangle rect = new Rectangle(-Gdx.graphics.getWidth() / 2f + marge + padding + i * size, - Gdx.graphics.getHeight() / 2f + padding, size, size);
				items.put(rect, item);
				batch.draw(region, rect.x, rect.y, rect.width, rect.height);
	
				i++;
			}
	
			batch.end();
		}
	}

	public void renderItem(Item item, int screenX, int screenY) {
		batch.begin();
		AtlasRegion region = ItemsRenderer.instance.getTexture(item.id);
		Vector2 vect = getCamCorrection(screenX, screenY);
		int size = 48;
		batch.draw(region, vect.x - size / 2f, vect.y - size / 2f, 0, 0, size, size, 1, 1, -30);
		batch.end();
	}
	
	public void renderInfo() {
		if (info !=null) {
			TextBounds bounds = font.getBounds(info);
			textInfo.x = infoX;
			textInfo.y = infoY;
			textInfo.width = bounds.width;
			textInfo.height = bounds.height;
			Aitrinity.game.drawText(info, textInfo, shapeRenderer, font, batch);
		}
	}

	public Item select(int screenX, int screenY) {
		Item selectedItem = null;
		Vector2 vect = getCamCorrection(screenX, screenY);
		for (Entry<Rectangle, Item> itemkv : items.entrySet()) {
			if (itemkv.getKey().contains(vect)) {
				selectedItem = itemkv.getValue();
				break;
			}
		}

		return selectedItem;
	}

	private Vector2 getCamCorrection(int screenX, int screenY) {
		vectmp.x = (screenX - Gdx.graphics.getWidth() / 2);
		vectmp.y = (Gdx.graphics.getHeight() / 2f - screenY);
		return vectmp;
	}

	private String info;
	private float infoX;
	private float infoY;
	
	public void writeInfo(String info, int screenX, int screenY) {
		this.info = info;
		Vector2 vect = getCamCorrection(screenX, screenY);
		infoX = vect.x;
		infoY = vect.y;
	}
}
