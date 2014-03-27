package gamers.associate;

import java.util.ArrayList;

public class Ia1 implements IState {
	private ArrayList<String> states;
	
	public Ia1() {
		states = new ArrayList<String>();
		states.add("new");
	}

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
}
