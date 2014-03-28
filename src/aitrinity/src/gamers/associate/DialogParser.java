package gamers.associate;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DialogParser {
	public static DialNode parse(IState stateItem) {
		DialNode rootNode = new DialNode();
		DialNode previousNode = rootNode;
		FileHandle file = Gdx.files.internal("data/dialog/" + stateItem.getId());
		String dialFull = file.readString("UTF-8");
		String[] dials = dialFull.split("\r\n");
		int i = 0;
		int previousIndent = -1;
		ArrayList<DialNode> nodeCursor = new ArrayList<DialNode>();		
		
		for (String dial : dials) {
			DialNode node = new DialNode();
			int indentLevel = 0;
			for (int c = 0; c < dial.length(); c++) {
				char ch = dial.charAt(c);
				if (ch == '\t') {
					indentLevel++;
				} else {
					break;
				}
			}
			
			String toParse = dial.replaceAll("\t", "");
			
			if (previousIndent < indentLevel) {
				nodeCursor.add(previousNode);
			}
			
			if (previousIndent == indentLevel) {
				previousNode.setLinkedNode(node);
			}
			
			if (previousIndent > indentLevel) {
				for (int rm = indentLevel + 1; rm < nodeCursor.size();) {
					nodeCursor.remove(nodeCursor.size() - 1);
				}
			}
						
			node.setParent(nodeCursor.get(nodeCursor.size() - 1));			
			
			node.setStateItem(stateItem);
			node.setWho(DialWho.NPC);
			node.setStrategy(DialStrat.Or);
			
			node.setStrategy(DialStrat.Or);
			
			if (toParse.startsWith(">")) {
				node.setWho(DialWho.Player);				
				toParse = toParse.substring(1);
			}
			
			if (toParse.startsWith("#")) {			
				node.setStrategy(DialStrat.And);
				toParse = toParse.substring(1);
			}
			
			if (toParse.startsWith("$STATE")) {
				int pos = toParse.indexOf(";");
				if (pos != -1) {
					String state = toParse.substring(7, pos);
					node.setStateConstraint(state);
					toParse = toParse.substring(pos + 1);
				}
			}
			
			if (toParse.startsWith("$SET_STATE")) {
				int pos = toParse.indexOf(";");
				if (pos != -1) {
					String state = toParse.substring(11, pos);
					node.setSetState(state);
					toParse = toParse.substring(pos + 1);
				}
			}
			
			if (toParse.startsWith("$RM_STATE")) {
				int pos = toParse.indexOf(";");
				if (pos != -1) {
					String state = toParse.substring(10, pos);
					node.setRemoveState(state);
					toParse = toParse.substring(pos + 1);
				}
			}
			
			if (toParse.startsWith("$ITEM")) {
				int pos = toParse.indexOf(";");
				if (pos != -1) {
					String item = toParse.substring(6, pos);
					node.setItemContraint(item);
					toParse = toParse.substring(pos + 1);
				}
			}
			
			if (toParse.startsWith("$GOTOEXIT")) {
				node.setGotoExit(true);
				toParse = toParse.substring(9);
			}
			
			if (toParse.startsWith("$EXIT")) {
				node.setExit(true);
				toParse = toParse.substring(5);
			}
			
			node.setSay(toParse.trim());
			
			previousNode = node;
			previousIndent = indentLevel;
			i++;
		}
		
		return rootNode;
	}
}
