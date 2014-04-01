package gamers.associate;

import com.badlogic.gdx.Gdx;

public class TextReader {
	public static String[] get(String id) {
		return Gdx.files.internal("data/" + id + "-" + Aitrinity.game.lang + ".txt").readString("UTF-8").split("\\n");
	}
}
