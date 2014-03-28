package gamers.associate;

import com.badlogic.gdx.math.Rectangle;

public class NPC extends Item {
	public NPC(String itemId, Rectangle itemRect) {
		super(itemId, itemRect);
		isPickable = false;
	}

	public String say;
}
