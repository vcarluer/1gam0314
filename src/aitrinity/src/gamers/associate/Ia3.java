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

		dialog1.add(me("Je suis contente que tu m'aies trouvé", "I'm glad you found me"));
		dialog1.add(me("Je me présente, je suis la troisième trinity", "Let me introduce myself, I am the third trinity"));
		dialog1.add(me("Ce que je préfère ce sont les jeux !", "What I like are games!"));
		dialog1.add(me("Alors avant de répondre à tes questions", "So before I answer your questions"));
		dialog1.add(me("J'aimerai que tu m'en trouve un", "I'd like you to find me one"));
		dialog1.add(me("Tout ce que je possède c'est un manche texturé en bois", "All I have is a textured wooden handle"));
		dialog1.add(me("Si tu m'aides je t'aiderai en retour", "If you help me I will help you in return"));
		dialog1.add(me("#manche", "#manche"));

		dialog2.add(me("Un coupe papier, mais ce n'est pas un jeu", "A paper cut, but this is not a game"));
		dialog2.add(him("J'en ai un en fait pour toi", "I have one for you actually"));
		dialog2.add(him("A l'aide de ce coupe papier virtuelle", "Using this virtual paper cut"));
		dialog2.add(him("Tu peux accéder aux mails des gens", "You can access the people's mails"));
		dialog2.add(him("Tu pourrais t'amuser à me trouver des informations croustillantes ?", "You could have fun finding me crispy information?"));
		dialog2.add(me("Je crois que je peux faire mieux que ça !", "I think I can do better than that!"));
		dialog2.add(me("Je te tiens au courant", "I'll let you know"));
		dialog2.add(me("En attendant prends déjà cette clé tu l'a bien mérité", "Take this key has deserved"));
		dialog2.add(me("Elle te permettra d'accéder au firewall qui renferme ce que tu cherches", "It will allow you to access the firewall that contains what you seek"));
		dialog2.add(me("#cle3", "#cle3"));
		dialog2.add(me("-coupepapier", "-coupepapier"));
	}
	
	private DialInfo me(String txtEn, String txtFr) {
		return new DialInfo("ia3", DialWho.NPC, txtEn, txtFr);
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
			Aitrinity.game.setSay(new GameText("Ca ne l'intéressera pas", "She won't be interrested"));
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
