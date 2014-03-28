package gamers.associate;

public class InventoryRenderer {
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private HashMap<Rectangle, Item> items;

	public InventoryRenderer {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		items = new HashMap<Rectangle, Item>();
		OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setProjectionMatrix(camera.combined());	
		shapeRenderer.setProjectionMatric(camera.combined());
	}

	public void render(ArrayList<Item> inventory) {
		int marge = 10;
		int padding = 10;
		shapeRenderer.begin(ShapeType.Fill);
		shapeRenderer.rect(-Gdx.graphics.getWidth() / 2f + marge, -Gdx.graphics.getHeight() + marge, Gdx.graphics.getWidth() - 2 * marge, 64);
		shapeRenderer.end();
		batch.begin();
		int i = 0;
		items.clear();
		for (Item item : inventory) {
			AtlasRegion region = ItemsRenderer.instance.getTexture(item.id);
			Rectangle rect = new Rectangle(-Gdx.graphics.getWidth() / 2f + marge + padding + i * region.getRegionWidth(), - Gdx.graphics.getHeight() + padding, region.getRegionWidth(), region.getRegionHeight());
			items.add(rect);
			batch.draw(region, rect.x, rect.y, rect.width, rect.height);

			i++
		}

		batch.end();
	}

	public void renderItem(Item, int screenX, int screenY) {
		batch.begin();
		AtlasRegion region = ItemsRenderer.instance.getTexture(item.id);
		batch.draw(region, screenX - region.getRegionWidth() / 2f, screenY - region.getRegionHeight() / 2f, region.getRegion.getRegionWidth(), region.getRegionHeight());
		batch.end();
	}

	public Item select(int screenX, int screenY) {
		Item selectedItem = null;
		Vector2 vect = getCamCorrection(screenX, screenY);
		for (Item itemkv : items) {
			if (itemkv.getKey().contains(vect)) {
				selectedItem = itemkv.getValue();
				break;
			}
		}

		return selectedItem;
	}

	private Vector2 getCamCorrection(int screenX, int screenY) {
		Vector2 clickV = new Vector2();
		clickV.x = (screenX - Gdx.graphics.getWidth() / 2);
		clickV.y = (Gdx.graphics.getHeight() / 2f - screenY);
		return clickV;
	}
}
