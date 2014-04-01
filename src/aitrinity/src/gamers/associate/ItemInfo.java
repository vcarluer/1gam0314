package gamers.associate;

import java.util.HashMap;

public class ItemInfo {
	private HashMap<String, GameText> info;

	public ItemInfo() {
		info = new HashMap<String, GameText>();
		info.put("cadre", new GameText("A 3 dimensional frame", "Un cadre en 3 dimensions"));
		info.put("photo", new GameText("A compressed image of my ex", "Une image compressée de mon ex"));
		info.put("casque", new GameText("VR glasses, wire is missing", "Des lunettes de RV, la connexion est manquante"));
		info.put("cable", new GameText("A neural wire", "Une connexion neuronale"));
		info.put("manche", new GameText("A wood textured handle", "Un manche texturé en bois"));
		info.put("lame", new GameText("A shiny chrome blade", "Une lame chromée brillante"));
		info.put("cle1", new GameText("The part of a key", "La partie d'une clé"));
		info.put("cle2", new GameText("The part of a key", "La partie d'une clé"));
		info.put("cle3", new GameText("The part of a key", "La partie d'une clé"));
		info.put("cle12", new GameText("The part of a key", "La partie d'une clé"));
		info.put("cle13", new GameText("The part of a key", "La partie d'une clé"));
		info.put("cle23", new GameText("The part of a key", "La partie d'une clé"));
		info.put("cle", new GameText("The key to open the firewall", "La clé pour ouvrir le firewall"));
		info.put("photoex", new GameText("The 3D photo of my ex", "La photo 3D de mon ex"));
		info.put("casquerv", new GameText("Fully working VR glasses", "Des lunettes RV pleinement fonctionnelles"));
		info.put("coupepapier", new GameText("A paper cut for mail", "Un coupe papier pour le courrier"));
		info.put("porte", new GameText("The main firewall. I need a key", "Le firewall principal, j'ai besoin d'une clé"));
		info.put("ia1", new GameText("The first trinity AI", "La première IA trinity"));
		info.put("ia2", new GameText("The second trinity AI", "La seconde IA trinity"));
		info.put("ia3", new GameText("The third trinity AI", "La troisième IA trinity"));
	}

	public String getInfo(Item item) {
		return info.get(item.id).get();
	}

	public String getInfo(String id) {
		return info.get(id).get();
	}
}
