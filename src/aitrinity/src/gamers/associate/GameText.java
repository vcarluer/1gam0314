package gamers.associate;

public class GameText {
	public String english;
	public String french;
	
	public GameText(String f, String e) {
		french = f;
		english = e;		
	}
	
	public String get() {
		if (Aitrinity.game.lang.equals("fr")) {
			return french;
		} else {
			return english;
		}
	}
}
