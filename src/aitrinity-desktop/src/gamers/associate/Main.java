package gamers.associate;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "aitrinity";
		cfg.useGL20 = false;
		cfg.width = 640;
		cfg.height = 640;
		cfg.fullscreen = false;
		
		new LwjglApplication(new Aitrinity(), cfg);
	}
}
