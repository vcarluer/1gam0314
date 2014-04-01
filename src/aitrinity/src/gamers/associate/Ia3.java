package gamers.associate;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class Ia3 extends NPC implements IState {
	public int state = 0;
	private ArrayList<DialInfo> dialog1;
	private ArrayList<DialInfo> dialog2;	
	
	public Ia3(String itemId, Rectangle itemRect) {
		super(itemId, itemRect);
		states = new ArrayList<String>();
		states.add("new");
		
		dialog1 = new ArrayList<DialInfo>();
		dialog2 = new ArrayList<DialInfo>();

		dialog1.add(me("Je veux que tu m'amuse"));
		dialog1.add(me("#manche"));
		
		
		dialog2.add(me("En attendant prends déjà cette clé tu l'a bien mérité"));
		dialog2.add(me("#cle3"));
		dialog2.add(me("-coupepapier"));
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
		return "ia3";
	}

	@Override
	public boolean useItemOn(Item item) {
		super.useItemOn(item);
		if (item.id == "coupepapier") {
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
