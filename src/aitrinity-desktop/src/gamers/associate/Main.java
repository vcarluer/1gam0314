package gamers.associate;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		try {
			LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
			cfg.title = "aitrinity";
			cfg.useGL20 = false;
			cfg.width = 1280;
			cfg.height = 720;
			cfg.fullscreen = false;
			
			new LwjglApplication(new Aitrinity(), cfg);
		} catch (Exception e) {
			 System.out.print("RuntimeException: ");
			    System.out.println(e.getMessage());
			    e.printStackTrace();
		}
	
	}
}
