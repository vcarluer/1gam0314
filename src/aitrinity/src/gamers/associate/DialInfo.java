package gamers.associate;

public class DialInfo {
	public DialWho who;
	public GameText text;
	public String talkerId;
	
	public DialInfo(String id, DialWho who, String txtEn, String txtFr) {
		this.who = who;
		text = new GameText(txtEn, txtFr);
		talkerId = id;
	}
}
