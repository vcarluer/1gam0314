package gamers.associate;

public class Ia1 implements IState {
	private String state;
	
	public Ia1() {
		state = "new";
	}
	
	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String getId() {
		return "ia1";
	}

}
