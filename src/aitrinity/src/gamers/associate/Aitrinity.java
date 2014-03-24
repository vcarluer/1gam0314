package gamers.associate;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Aitrinity implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private int activateCols = 8;
	private int activateCount = 6;
	private TextureRegion[]	activateFrames;
	private Animation activateAnimation;
	private float stateTime;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		camera.zoom = 0.5f;
		camera.update();
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/anim_activate.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / activateCols, texture.getHeight());
		activateFrames = new TextureRegion[activateCount];
		for (int i = 0; i < activateCount; i++) {
			activateFrames[i] = tmp[0][i];
		}
				
		activateAnimation = new Animation(0.1f, activateFrames);
		stateTime = 0f;
		
//		TextureRegion region = new TextureRegion(texture, 0, 0, 256, 64);
//		
//		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// sprite.draw(batch);
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion texture = activateAnimation.getKeyFrame(stateTime, true);
		batch.draw(texture, -texture.getRegionWidth() / 2f, -texture.getRegionHeight() / 2f);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
