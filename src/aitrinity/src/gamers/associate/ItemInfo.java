package gamers.associate;

import java.util.HashMap;

public class ItemInfo {
	private HashMap<String, GameText> info;

	public ItemInfo() {
		info = new HashMap<String, GameText>();
		info.put("cadre", new GameText("Un cadre en 3 dimensions", "A 3 dimensional frame"));
		info.put("photo", new GameText("Une image compressée de mon ex", "A compressed image of my ex"));
		info.put("casque", new GameText("Des lunettes de RV, la connexion est manquante", "VR glasses, wire is missing"));
		info.put("cable", new GameText("Une connexion neuronale", "A neural wire"));
		info.put("manche", new GameText("Un manche texturé en bois", "A wood textured handle"));
		info.put("lame", new GameText("Une lame chromée brillante", "A shiny chrome blade"));
		info.put("cle1", new GameText("La partie d'une clé", "The part of a key"));
		info.put("cle2", new GameText("La partie d'une clé", "The part of a key"));
		info.put("cle3", new GameText("La partie d'une clé", "The part of a key"));
		info.put("cle12", new GameText("La partie d'une clé", "The part of a key"));
		info.put("cle13", new GameText("La partie d'une clé", "The part of a key"));
		info.put("cle23", new GameText("La partie d'une clé", "The part of a key"));
		info.put("cle", new GameText("La clé pour ouvrir le firewall", "The key to open the firewall"));
		info.put("photoex", new GameText("La photo 3D de mon ex", "The 3D photo of my ex"));
		info.put("casquerv", new GameText("Des lunettes RV pleinement fonctionnelles", "Fully working VR glasses"));
		info.put("coupepapier", new GameText("Un coupe papier pour le courrier", "A paper cut for mail"));
		info.put("porte", new GameText("Le firewall principal, j'ai besoin d'une clé", "The main firewall. I need a key"));
		info.put("ia1", new GameText("La première IA trinity", "The first trinity AI"));
		info.put("ia2", new GameText("La seconde IA trinity", "The second trinity AI"));
		info.put("ia3", new GameText("La troisième IA trinity", "The third trinity AI"));
	}

	public String getInfo(Item item) {
		return info.get(item.id).get();
	}

	public String getInfo(String id) {
		return info.get(id).get();
	}
}
