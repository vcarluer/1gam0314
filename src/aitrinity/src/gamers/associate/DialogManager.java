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
	
	private DialNode gotoNextNodeNPC(DialNode localCursor) {
		if (localCursor != (null)) {						
			for (DialNode node : localCursor.getChilds()) {
				if (!node.isDone()) {
					return node;
				}									
			}
			
			localCursor.setDone(true);
			if (localCursor.getParent() != null) {
				return gotoNextNodeNPC(localCursor.getParent());
			}
		}
		
		return null;
	}
	
	public boolean atEnd() {
		return cursor == null;
	}
	
	public void gotoNextNodeNPC() {
		cursor = gotoNextNodeNPC(cursor);
	}

	public String getNPCSay() {
		if (cursor != null && cursor.getWho() == DialWho.NPC) {
			return cursor.getSay();
		} else {
			return null;
		}
		
	}

	public ArrayList<DialNode> getPCOptions() {
		if (cursor != null) {
			ArrayList<DialNode> pcOptions = new ArrayList<DialNode>(); 
			for (DialNode node : cursor.getChilds()) {
				if (node.getWho() == DialWho.Player) {
					pcOptions.add(node);
				}
			}
			
			return pcOptions;
		} else {
			return null;
		}
	}

	public void setNextNpc(DialNode dialNode) {
		cursor = dialNode;
		gotoNextNodeNPC();
	}
}
