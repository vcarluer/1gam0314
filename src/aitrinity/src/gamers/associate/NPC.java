package gamers.associate;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class NPC extends Item {
	public NPC(String itemId, Rectangle itemRect) {
		super(itemId, itemRect);
		isPickable = false;
	}
	
	public boolean contains(Vector2 vector) {
		return rect.contains(vector);
	}
}
