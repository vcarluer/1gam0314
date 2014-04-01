package gamers.associate;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class Ia1 extends NPC implements IState {
	public int state = 0;
	private ArrayList<DialInfo> dialog1;
	private ArrayList<DialInfo> dialog2;	
	
	public Ia1(String itemId, Rectangle itemRect) {
		super(itemId, itemRect);
		states = new ArrayList<String>();
		states.add("new");
		
		dialog1 = new ArrayList<DialInfo>();
		dialog2 = new ArrayList<DialInfo>();
		
		dialog1.add(me("Bonjour à toi Neon", "Hello to you Neon"));
		dialog1.add(me("J'ai cru entendre que tu me cherchais", "I've heard you were looking for me"));
		dialog1.add(me("Je suis la première trinity", "I am the first trinity"));
		dialog1.add(me("Avant de pouvoir t'aider", "Before I can help you"));
	       	dialog1.add(me("J'aimerai que tu fasses quelque chose pour moi", "I'd like you to do something for me"));
		dialog1.add(me("Amène-moi une preuve d'amour véritable dans ce cadre 3D", "Bring me proof of true love in this 3D frame"));
	       	dialog1.add(me("Alors je répondrai à tes questions", "Then I will answer your questions"));
		dialog1.add(me("Je veux que tu me montres ce qui est important pour toi", "I want you to show me what is important to you"));
		dialog1.add(me("#cadre"));
		
		dialog2.add(me("C'est exactement ce que je cherchais !", "This is exactly what I was looking for!"));
		dialog2.add(him("Vous allez pouvoir m'aider maintenant ?", "You can help me now?"));
		dialog2.add(him("Revoir sa photo m'a rappelé de bons souvenir", "Seeing her picture reminded me good memories"));
		dialog2.add(him("J'aimerai tellement pouvoir lui parler", "I would really like to talk to her"));
		dialog2.add(him("Mais elle s'est uploadée dans la matrice il y a 2 semaines", "But she uploaded herself in the matrix 2 weeks ago"));
		dialog2.add(him("Elle m'a trahi en me laissant seul, sans rien dire", "She betrayed me, leaving me alone, saying nothing"));
		dialog2.add(me("Je vais regarder ce que je peux faire", "I'll see what I can do"));
		dialog2.add(me("Je te recontacte bientôt", "I will soon recontact you"));
		dialog2.add(me("En attendant prends déjà cette clé tu l'a bien mérité", "In the meantime take this key has deserved"));
		dialog2.add(me("Elle te permettra d'accéder au firewall qui renferme ce que tu cherches", "It will allow you to access the firewall that contains what you seek"));
		dialog2.add(me("#cle1"));
		dialog2.add(me("-photoex"));
	}
	
	private DialInfo me(String txtEn, String txtFr) {
		return new DialInfo("ia1", DialWho.NPC, txtEn, txtFr);
	}
	
	private DialInfo him(String txtEn, String txtFr) {
		return new DialInfo("player", DialWho.Player, txtEn, txtFr);
	}

	private ArrayList<String> states;
	

	@Override
	public void addState(String state) {
		states.add(state);
	}

	@Override
	public void rmState(String state) {
		states.remove(state);
	}

	@Override
	public boolean isState(String state) {

		return states.contains(state);
	}

	@Override
	public String getId() {
		return "ia1";
	}

	@Override
	public boolean useItemOn(Item item) {
		super.useItemOn(item);
		if (item.id == "photoex") {
			state = 1;
			Aitrinity.game.dialogRenderer.setText(dialog2);
			return true;
		} else {
			Aitrinity.game.setSay(new GameText("She won't be interrested", "Ca ne l'intéressera pas"));
		}
		
		return false;
	}
	
	public ArrayList<DialInfo> getDialog() {
		if (state == 0) {
			return dialog1;
		}
		
		if (state == 1) {
			return dialog2;
		}
		
		return null;
	}
}
