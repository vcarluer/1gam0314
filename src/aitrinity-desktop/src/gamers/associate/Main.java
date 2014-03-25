package gamers.associate;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "aitrinity";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 780;
		
		new LwjglApplication(new Aitrinity(), cfg);
	}
}
