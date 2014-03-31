package gamers.associate;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "aitrinity";
		cfg.useGL20 = false;
		cfg.width = 1920;
		cfg.height = 1080;
		cfg.fullscreen = true;
		
		new LwjglApplication(new Aitrinity(), cfg);
	}
}
