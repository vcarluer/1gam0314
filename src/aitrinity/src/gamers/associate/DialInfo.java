package gamers.associate;

public class DialInfo {
	public DialWho who;
	public GameText text;
	public String talkerId;
	
	public DialInfo(String id, DialWho who, String txtFr, String txtEn) {
		this.who = who;
		text = new GameText(txtFr, txtEn);
		talkerId = id;
	}
}
