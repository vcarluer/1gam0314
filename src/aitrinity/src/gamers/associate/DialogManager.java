package gamers.associate;

import java.util.ArrayList;

public class DialogManager {
	private DialNode rootNode;
	private DialNode cursor;
	
	public DialogManager(DialNode ia1DialNode) {
		rootNode = ia1DialNode;
	}
	
	public void startDialog() {
		cursor = rootNode;
		initDone(cursor);
	}
	
	private void initDone(DialNode node) {
		node.setDone(false);
		for (DialNode child : node.getChilds()) {
			initDone(child);
		}
	}
	
	public boolean atEnd() {
		return cursor == null;
	}
	
	public void gotoNextNodeNPC() {
		if (cursor != (null)) {						
			for (DialNode node : cursor.getChilds()) {
				if (!node.isDone()) {
					cursor = node;
					break;
				}									
			}
			
			cursor.setDone(true);
			if (cursor.getParent() != null) {
				cursor = cursor.getParent();
				gotoNextNodeNPC();
			}
		}		
	}

	public String getNPCSay() {
		if (cursor != null && cursor.getWho() == DialWho.NPC) {
			return cursor.getSay();
		} else {
			return null;
		}
		
	}
	
	public String getPCSay() {
		if (cursor != null && cursor.getWho() == DialWho.Player) {
			return cursor.getSay();
		} else {
			return null;
		}
		
	}

	public ArrayList<DialNode> getPCOptions() {
		ArrayList<DialNode> pcOptions = null;
		if (cursor != null) {
			if (cursor.getWho() == DialWho.NPC || cursor.getWho() == null) {
				pcOptions = new ArrayList<DialNode>(); 
				for (DialNode node : cursor.getChilds()) {
					if (node.getWho() == DialWho.Player) {
						pcOptions.add(node);
					}
				}
			}
		}
		
		return pcOptions;
	}

	public void chooseOption(DialNode dialNode) {
		if (cursor != null) {
			if (cursor.getStrategy() == DialStrat.Or) {
				cursor.setDone(true);
			}
		}
		
		cursor = dialNode;
	}
}
