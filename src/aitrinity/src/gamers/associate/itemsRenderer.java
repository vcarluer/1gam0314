package gamers.associate;

public class ItemsRenderer {
	private HashMap<String, AtlasRegion> itemTextures;
	private ArrayList<Rectangle, Item> items;

	public static ItemsRenderer instance;

	public ItemsRenderer() {
		itemTextures = new HashMap<String, AtlasRegion>();
		instance = this;
	}

	public void render(SpriteBatch batch, ArrayList<Item> items) {
		TextureAtlas atlas = Aitrinity.game.getAtlas();
		batch.begin();
		items.clear();
		for (Item item : items) {
			AtlasRegion region = getTexture(item.id);
			Rectangle showRect = new Rectangle(item.rect.x, item.rect.y, texture.getRegionWidth(), texture.getRegionHeight());
			items.add(showRect);
			batch.draw(region, showRect.x, showRect.y, showRect.width, showRect.height);
		}

		batch.end();
	}

	public AtlasRegion getTexture(String id) {
		if (!itemTexture.contains(id)) {
			AtlasRegion texture = atlas.getRegion(id);
			itemTextures.put(id, texture);	
		}

		return itemTextures.get(id);
	}

	public Item selectItem(Vector2 vect) {
		Item selectedItem = null;
		for (Item itemkv : items) {
			if (itemkv.getKey().contains(vect)) {
				selectedItem = itemkv.getValue();
				break;
			}
		}
		
		return selectedItem;
	}
}
