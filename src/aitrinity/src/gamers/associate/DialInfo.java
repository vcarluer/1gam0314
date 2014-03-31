package gamers.associate;

public class DialInfo {
	public DialWho who;
	public String text;
	public String talkerId;
	
	public DialInfo(String id, DialWho who, String txt) {
		this.who = who;
		text = txt;
		talkerId = id;
	}
}
