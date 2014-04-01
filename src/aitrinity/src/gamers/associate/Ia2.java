package gamers.associate;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class Ia2 extends NPC implements IState {
	public int state = 0;
	private ArrayList<DialInfo> dialog1;
	private ArrayList<DialInfo> dialog2;	
	
	public Ia2(String itemId, Rectangle itemRect) {
		super(itemId, itemRect);
		states = new ArrayList<String>();
		states.add("new");
		
		dialog1 = new ArrayList<DialInfo>();
		dialog2 = new ArrayList<DialInfo>();
		
		dialog1.add(me("Bonjour � toi Neon", "Hello to you Neon"));
		dialog1.add(me("Tu m�as trouv� c�est bien", "Congratulation, you have found me"));
		dialog1.add(me("Je suis la seconde trinity", "I am the second trinity"));
		dialog1.add(me("Avant de pouvoir t'aider", "Before I can help you"));
		dialog1.add(me("J�aimerai que tu fasses quelque chose pour moi", "I'd like you to do something for me"));
		dialog1.add(me("R�pare-moi ces lunettes de RV matricielle", "Repairs me these VR matrix glasses"));
		dialog1.add(me("En tant qu�IA ces lunettes me montrent la r�alit�", "As an AI these glasses show me the reality"));
		dialog1.add(me("Alors je r�pondrai � tes questions", "Then I will answer your questions"));
		dialog1.add(me("#casque", "#casque" ));

		dialog2.add(me("Elles ont l�air parfaitement r�par�e", "They look in perfectly repaired"));
		dialog2.add(me("Comment souhaiterais-tu que je t�aide ?", "How would you like me to help you?"));
		dialog2.add(him("Et bien j�ai d�couvert une mauvaise nouvelle il y a peu", "Well I found out bad news few days ago"));
		dialog2.add(him("Le FBI semblerait �tre sur mes traces", "The FBI seems to be in my footsteps"));
		dialog2.add(him("Mais je ne suis s�r de rien, pourrais-tu v�rifier ?", "But I'm not sure of anything, can you check?"));
		dialog2.add(me("Je peux m�me faire plus que cela", "I can even do more than that"));
		dialog2.add(me("Je te recontacte bient�t", "I will recontact you soon"));
		dialog2.add(me("En attendant prends d�j� cette cl� tu l'a bien m�rit�", "Take this key has deserved"));
		dialog2.add(me("Elle te permettra d'acc�der au firewall qui renferme ce que tu cherches", "It will allow you to access the firewall that contains what you seek"));
		dialog2.add(me("#cle2", "#cle2"));
		dialog2.add(me("-casquerv", "-casquerv"));
	}
	
	private DialInfo me(String txtEn, String txtFr) {
		return new DialInfo("ia2", DialWho.NPC, txtEn, txtFr);
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
		return "ia2";
	}

	@Override
	public boolean useItemOn(Item item) {
		super.useItemOn(item);
		if (item.id == "casquerv") {
			state = 1;
			Aitrinity.game.dialogRenderer.setText(dialog2);
			return true;
		} else {
			Aitrinity.game.setSay(new GameText("She won't be interrested", "Ca ne l'int�ressera pas"));
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
