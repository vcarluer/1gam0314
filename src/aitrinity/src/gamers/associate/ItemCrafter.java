package gamers.associate;

import com.badlogic.gdx.math.Rectangle;

public class ItemCrafter {
	public Item combine(Item item1, Item item2) {
		Item item = null;
		item = tryCombine(item1, item2, "cadre", "photo", "photoex");
		if (item != null) {
			return item;
		}

		item = tryCombine(item1, item2, "casque", "cable", "casquerv");
		if (item != null) {
			return item;
		}

		item = tryCombine(item1, item2, "manche", "lame", "coupepapier");
		if (item != null) {
			return item;
		}

		item = tryCombine(item1, item2, "cle1", "cle2", "cle12");
		if (item != null) {
			return item;
		}

		item = tryCombine(item1, item2, "cle12", "cle3", "cle");
		if (item != null) {
			return item;
		}

		item = tryCombine(item1, item2, "cle1", "cle3", "cle13");
		if (item != null) {
			return item;
		}

		item = tryCombine(item1, item2, "cle13", "cle2", "cle");
		if (item != null) {
			return item;
		}

		item = tryCombine(item1, item2, "cle2", "cle3", "cle23");
		if (item != null) {
			return item;
		}

		item = tryCombine(item1, item2, "cle23", "cle1", "cle");
		if (item != null) {
			return item;
		}
		
		return null;
	}

	private Item tryCombine(Item item1, Item item2, String id1, String id2, String idOut) {
		if ((item1.id.equals(id1) || item1.id.equals(id2)) && (item2.id.equals(id1) || item2.id.equals(id2))) {
			return new Item(idOut, new Rectangle(0, 0, 0, 0));
		}
		
		return null;		
	}
	
	public String useOn(Item selectedItem, Item mapItem) {
		String returnSay = null;
		if (selectedItem.equals("cle") && mapItem.id.equals("porte")) {
			returnSay = "La porte s'est ouverte !";
		}

		return returnSay;
	}
}
