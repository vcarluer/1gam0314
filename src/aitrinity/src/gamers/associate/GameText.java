package gamers.associate;

public class GameText {
	public String english;
	public String french;
	
	public GameText(String e, String f) {
		english = e;
		french = f;
	}
	
	public String get() {
		if (Aitrinity.game.lang.equals("fr")) {
			return french;
		} else {
			return english;
		}
	}
}
