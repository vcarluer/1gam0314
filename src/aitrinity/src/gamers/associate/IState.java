package gamers.associate;

public interface IState {
	public void addState(String state);
	public void rmState(String state);
	public boolean isState(String state);
	public String getId();
}
