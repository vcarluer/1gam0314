package gamers.associate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DialogParser {
	public static DialNode parse(IState stateItem) {
		DialNode rootNode = null;
		DialNode parentNode = null;
		FileHandle file = Gdx.files.internal("data/dialog/" + stateItem.getId());
		String dialFull = file.readString();
		String[] dials = dialFull.split("\r\n");
		int i = 0;
		for (String dial : dials) {
			DialNode node = new DialNode();
			
			if (i == 0) {
				rootNode = node; 
			}
			
			if (parentNode != null) {
				node.setParent(parentNode);
			}
			
			node.setStateItem(stateItem);
			
			node.setLine(dial);
			
			parentNode = node;
			i++;
		}
		
		return rootNode;
	}
}
