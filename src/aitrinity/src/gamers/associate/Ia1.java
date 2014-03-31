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
		
		dialog1.add(me("Bonjour � toi Neon."));
		dialog1.add(me("J'ai cr� entendre que tu me cherchais."));
		dialog1.add(me("Am�ne moi une preuve d'amour et je r�pondrai � tes questions."));
		
		dialog2.add(me("C'est exactement ce que je cherchais!"));
		dialog2.add(him("Vous allez pouvoir m'aider maintenant?"));
		dialog2.add(him("Revoir sa photo m'a rappeler de bons souvenir"));
		dialog2.add(him("J'aimerai tellement pouvoir lui parler"));
		dialog2.add(him("Mais elle s'est upload� dans la matrice il y a 2 semaines"));
		dialog2.add(me("Je vais regardr ce que je peux faire"));
		dialog2.add(me("Je te recontacte d�s que possible"));
		dialog2.add(me("En attendant prends d�j� cette cl� tu l'a bien m�rit�"));
		dialog2.add(me("#cle1"));
	}
	
	private DialInfo me(String txt) {
		return new DialInfo(DialWho.NPC, txt);
	}
	
	private DialInfo him(String txt) {
		return new DialInfo(DialWho.Player, txt);
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
