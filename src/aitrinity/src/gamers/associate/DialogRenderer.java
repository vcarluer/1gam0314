package gamers.associate;	

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DialogRenderer {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private int fontSize = 35;
	private DialogManager dialog;
	int paddingH = 20;
	int paddingW = 50;
	int dialogH = 200;
	private float paddingInH = 5;
	private float paddingInW = 10;
	private ArrayList<DialNode> optNodes;
	private ArrayList<Rectangle> optRect;
	private boolean optRectInit;
	private ArrayList<String> sayingNPC;
	private ArrayList<String> sayingPC;
	private float sayLife = 3f;
	private float sayTime;
	private int sayIdx;
	private boolean npcSayEnd;
	
	public DialogRenderer() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		font = Aitrinity.game.getFontGenerator().generateFont(fontSize);
		font.setColor(Color.WHITE);
		optNodes = new ArrayList<DialNode>();
		optRect = new ArrayList<Rectangle>();
		sayingNPC = new ArrayList<String>();
		sayingPC = new ArrayList<String>();
	}
	public void render(DialogManager dialogManager) {
		if (dialog == null && dialogManager != null) {
			reset();
		}
		
		dialog = dialogManager;
		if (dialog != null) {
			if (!dialog.atEnd()) {
				renderNPC();
				renderPC();
			} else {
				Aitrinity.game.dialogEnd();
			}
		}
	}
	
	private void reset() {
		optNodes.clear();
		optRect.clear();
		optRectInit = false;
		sayingNPC.clear();
		sayingPC.clear();
		sayTime = 0;
		sayIdx = 0;
		npcSayEnd = false;
		optOver = -1;
	}
	private void renderPC() {
		String say = dialog.getPCSay();
		if (say != null && !say.equals("")) {
			if (sayingPC.size() == 0) {
				String[] sentences = say.split("\\.");
				for (String sentence : sentences) {
					sayingPC.add(sentence.trim());
				}
			}		
		} else {
			sayingPC.clear();
		}
		
		if (sayingPC.size() > 0) {
			sayTime += Gdx.graphics.getDeltaTime();
			if (sayTime > sayLife) {
				sayTime = 0;
				sayIdx++;
			}
			
			if (sayIdx >= sayingNPC.size()) {
				pcEndSay();
			} else {
				renderBackPC();
				renderTextPC();
			}						
		} else {
			optNodes = dialog.getPCOptions();
			if (optNodes != null) {
				renderBackPC();
				renderOptPC();
			}
		}
		
//			
//		
//		
//		if (selectedNode != null) {
//			float delta = Gdx.graphics.getDeltaTime();
//			sayTime += delta;
//			if (sayTime > sayLife) {
//				sayTime = 0;
//				sayIdx++;
//			}
//			
//			if (sayIdx >= sayingPC.size()) {
//				pcEndSay();
//			} else {
//				renderBackPC();
//				String write = sayingPC.get(sayIdx).trim();
//				TextBounds bounds = font.getBounds(write);
//				batch.begin();
//				font.setColor(Color.WHITE);
//				font.draw(batch, write, 
//						-Gdx.graphics.getWidth() / 2f + paddingW + paddingInW, 
//						- Gdx.graphics.getHeight() / 2f + paddingH + dialogH / 2 + bounds.height / 2);
//				batch.end();
//			}
//		} else {
//			optNodes = dialog.getPCOptions();
//			if (optNodes != null) {
//				if (optNodes.size() != 0) {
//					renderBackPC();
//					renderTextPC();
//				} else {
//					if (npcSayEnd) {
//						dialog.gotoNextNodeNPC();
//						reset();
//					}
//				}
//			}
//		}
	}
	
	private void renderNPC() {		
		String say = dialog.getNPCSay();
		if (say != null && !say.equals("")) {
			if (sayingNPC.size() == 0) {
				String[] sentences = say.split("\\.");
				for (String sentence : sentences) {
					sayingNPC.add(sentence.trim());
				}
			}		
		} else {
			sayingNPC.clear();
		}
	
		if (sayingNPC.size() > 0) {
			sayTime += Gdx.graphics.getDeltaTime();
			if (sayTime > sayLife) {
				sayTime = 0;
				sayIdx++;
			}
			
			if (sayIdx >= sayingNPC.size()) {
				npcSayEnd = true;
				sayIdx = sayingNPC.size() - 1;
			}
			
			renderBackNPC();
			renderTextNPC();
		}
	}
	private void renderOptPC() {
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.begin(ShapeType.Line);
		
		int i = 0;
		for (DialNode opt : optNodes) {
			TextBounds bounds = font.getBounds(opt.getSay());
			Rectangle rect = new Rectangle();
			rect.x = -Gdx.graphics.getWidth() / 2f + paddingW;
			rect.y = - Gdx.graphics.getHeight() / 2f + paddingH + dialogH - paddingInH - bounds.height - i * bounds.height * 2 - paddingInH;
			rect.width = Gdx.graphics.getWidth() - 2 * paddingW;
			rect.height = bounds.height + paddingInH * 2;
			if (!optRectInit) {
				optRect.add(rect);
			}
			
			shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
						
			i++;
		}

		shapeRenderer.end();
		
		if (optOver > -1) {
			shapeRenderer.begin(ShapeType.Filled);
			Rectangle rect = optRect.get(optOver);
			shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
			shapeRenderer.end();
		}

		batch.begin();
		
		int j = 0;
		for (DialNode opt : optNodes) {
			String say = opt.getSay();
			TextBounds bounds = font.getBounds(opt.getSay());
			if (j == optOver) {
				font.setColor(Color.BLACK);
			} else {
				font.setColor(Color.WHITE);
			}
			
			
			int pos = say.indexOf(".");
			String write = say;
			if (pos != -1) {
				write = say.substring(0, pos);
			}
			
			font.draw(batch, write, -Gdx.graphics.getWidth() / 2f + paddingW + paddingInW, - j * bounds.height * 2 - Gdx.graphics.getHeight() / 2f + paddingH + dialogH - paddingInH );
			j++;
		}
		
		batch.end();

	}
	
	private void renderTextPC() {
		batch.begin();
		String write = sayingPC.get(sayIdx);
		TextBounds bounds = font.getBounds(write);
		font.setColor(Color.WHITE);
		font.setColor(Color.WHITE);
		font.draw(batch, write, 
				-Gdx.graphics.getWidth() / 2f + paddingW + paddingInW, 
				- Gdx.graphics.getHeight() / 2f + paddingH + dialogH / 2 + bounds.height / 2);
		batch.end();
	}
	
	private void renderTextNPC() {
		batch.begin();
		String write = sayingNPC.get(sayIdx);
		TextBounds bounds = font.getBounds(write);
		font.setColor(Color.WHITE);
		font.draw(batch, write, -Gdx.graphics.getWidth() / 2f + paddingW + paddingInW, Gdx.graphics.getHeight() / 2f - paddingH - dialogH / 2f + bounds.height / 2f);
		batch.end();
	}
	
	private void renderBackNPC() {
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.rect(-Gdx.graphics.getWidth() / 2f + paddingW  , Gdx.graphics.getHeight() / 2f - paddingH - dialogH, Gdx.graphics.getWidth() - 2 * paddingW, dialogH);
		shapeRenderer.end();
	}
	
	private void renderBackPC() {
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(-Gdx.graphics.getWidth() / 2f + paddingW  , - Gdx.graphics.getHeight() / 2f + paddingH, Gdx.graphics.getWidth() - 2 * paddingW, dialogH);
		shapeRenderer.end();
	}
	
	int optOver = -1;
	
	public void mouseOn(int screenX, int screenY) {
		optOver = -1;
		if (dialog != null) {
			int i = 0;
			for (Rectangle rect : optRect) {
				if (rect.contains(getCamCorrection(screenX, screenY))) {
					optOver = i;
					break;
				}
				i++;
			}
		}
	}
	
	public void clickOn(int screenX, int screenY) {
		if (dialog != null) {
			if (optRect.size() > 0) {
				int i = 0;
				for (Rectangle rect : optRect) {
					if (rect.contains(getCamCorrection(screenX, screenY))) {
						dialog.chooseOption(optNodes.get(i));
						reset();
						break;
					}
					
					i++;
				}
			} else {
				dialog.gotoNextNodeNPC();
			}
		}
	}
	
	private void pcEndSay() {
		dialog.gotoNextNodeNPC();
		reset();
	}
	
	private Vector2 getCamCorrection(int screenX, int screenY) {
		Vector2 clickV = new Vector2();
		clickV.x = (screenX - Gdx.graphics.getWidth() / 2);
		clickV.y = (Gdx.graphics.getHeight() / 2f - screenY);
		return clickV;
	}
}
