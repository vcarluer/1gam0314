package gamers.associate;

import com.badlogic.gdx.math.Rectangle;

public class Item {
	public String id;
	public Rectangle rect;
	protected boolean isPickable;

	public Item(String itemId, Rectangle itemRect) {
		id = itemId;
		rect = itemRect;
		isPickable = true;
	}

	public Item(String itemId, Rectangle itemRect, boolean pickable) {
		this(itemId, itemRect);
		isPickable = pickable;
	}

	public boolean isPickable() {
		return isPickable;
	}
}
