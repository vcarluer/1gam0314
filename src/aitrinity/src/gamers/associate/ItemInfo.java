package gamers.associate;

import java.util.HashMap;

public class ItemInfo {
	private HashMap<String, String> info;

	public ItemInfo() {
		info = new HashMap<String, String>();
		info.put("cadre", "A 3 dimensional frame");
		info.put("photo", "A compressed image of my ex");
		info.put("casque", "An RV helmet, wire is missing");
		info.put("cable", "A neural wire cable");
		info.put("manche", "A wood textured handle");
		info.put("lame", "A shiny chrome blade");
		info.put("cle1", "The part of a key");
		info.put("cle2", "The part of a key");
		info.put("cle3", "The part of a key");
		info.put("cle12", "The part of a key");
		info.put("cle13", "The part of a key");
		info.put("cle23", "The part of a key");
		info.put("cle", "The key to open the firewall");
		info.put("photoex", "The 3d photo of my ex");
		info.put("cle", "The key to open the firewall");
		info.put("cle", "The key to open the firewall");

	}

	public String getInfo(Item item) {
		return info.get(item.id);
	}
}
