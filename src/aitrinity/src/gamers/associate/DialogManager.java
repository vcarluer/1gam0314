package gamers.associate;

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
	
	public String getNextSay() {
		String say = "";
		if (cursor != null) {
			say = cursor.getSay();
			
			DialNode nextNode = gotoNextNode(cursor);
			if (nextNode != null) {				
				cursor = nextNode;
			}
		}	
		
		return say;
	}
	
	private DialNode gotoNextNode(DialNode localCursor) {
		if (localCursor != (null)) {						
			for (DialNode node : localCursor.getChilds()) {
				if (!node.isDone()) {
					return node;
				}									
			}
			
			localCursor.setDone(true);
			if (localCursor.getParent() != null) {
				return gotoNextNode(localCursor.getParent());
			}
		}
		
		return null;
	}

}
