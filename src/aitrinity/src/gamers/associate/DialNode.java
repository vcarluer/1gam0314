package gamers.associate;

import java.util.ArrayList;
import java.util.List;

public class DialNode {
	private String line;
	private List<DialNode> childs;
	private DialNode parent; 
	private String stateConstraint;
	private String itemContraint;
	private String setState;
	private IState stateItem;
	
	public DialNode() {
		childs = new ArrayList<DialNode>();
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public void addChild(DialNode child) {
		childs.add(child);
	}

	public DialNode getParent() {
		return parent;
	}

	public void setParent(DialNode parent) {
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public String getStateConstraint() {
		return stateConstraint;
	}

	public void setStateConstraint(String stateConstraint) {
		this.stateConstraint = stateConstraint;
	}

	public String getSetState() {
		return setState;
	}

	public void setSetState(String setState) {
		this.setState = setState;
	}

	public IState getStateItem() {
		return stateItem;
	}

	public void setStateItem(IState stateItem) {
		this.stateItem = stateItem;
	}

	public String getItemContraint() {
		return itemContraint;
	}

	public void setItemContraint(String itemContraint) {
		this.itemContraint = itemContraint;
	}
}
