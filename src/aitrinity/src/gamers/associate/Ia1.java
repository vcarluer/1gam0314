package gamers.associate;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class Ia1 extends NPC implements IState {
	public int state = 0;
	private ArrayList<String> dialog1;
	private ArrayList<String> dialog2;	
	
	public Ia1(String itemId, Rectangle itemRect) {
		super(itemId, itemRect);
		states = new ArrayList<String>();
		states.add("new");
		
		dialog1 = new ArrayList<String>();
		dialog2 = new ArrayList<String>();
		
		dialog1.add("Bonjour à toi Neon.");
		dialog1.add("J'ai crû entendre que tu me cherchais.");
		dialog1.add("Amène moi une preuve d'amour et je répondrai à tes questions.");
		
		dialog2.add("Je regarde ce que je peux faire");
		dialog2.add("Je te recontacte dès que possible");
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
			Aitrinity.game.dialogRenderer.setText(DialWho.NPC, dialog2);
			return true;
		}
		
		return false;
	}
	
	public ArrayList<String> getDialog() {
		if (state == 0) {
			return dialog1;
		}
		
		if (state == 1) {
			return dialog2;
		}
		
		return null;
	}
}
