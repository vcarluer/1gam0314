package gamers.associate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ItemsRenderer {
	private HashMap<String, AtlasRegion> itemTextures;
	private HashMap<Rectangle, Item> itemsMap;

	public static ItemsRenderer instance;

	public ItemsRenderer() {
		itemTextures = new HashMap<String, AtlasRegion>();
		itemsMap = new HashMap<Rectangle, Item>();
		instance = this;
	}

	public void render(SpriteBatch batch, ArrayList<Item> items) {		
		batch.begin();
		itemsMap.clear();
		for (Item item : items) {
			AtlasRegion region = getTexture(item.id);
			Rectangle showRect = new Rectangle(item.rect.x, item.rect.y, region.getRegionWidth(), region.getRegionHeight());
			itemsMap.put(showRect, item);
			batch.draw(region, showRect.x, showRect.y, showRect.width, showRect.height);
		}

		batch.end();
	}

	public AtlasRegion getTexture(String id) {
		TextureAtlas atlas = Aitrinity.game.getAtlas();
		if (!itemTextures.containsKey(id)) {
			AtlasRegion texture = atlas.findRegion(id);
			itemTextures.put(id, texture);	
		}

		return itemTextures.get(id);
	}

	public Item selectItem(Vector2 vect) {
		Item selectedItem = null;
		for (Entry<Rectangle, Item> itemkv : itemsMap.entrySet()) {
			if (itemkv.getKey().contains(vect)) {
				selectedItem = itemkv.getValue();
				break;
			}
		}
		
		return selectedItem;
	}


}
