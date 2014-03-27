package gamers.associate;

import java.util.ArrayList;
import java.util.List;

public class DialNode {	
	private String say;
	private List<DialNode> childs;
	private DialNode parent; 
	private String stateConstraint;
	private String itemContraint;
	private String setState;
	private IState stateItem;
	private DialStrat strategy;
	private boolean done;
	private DialWho who;
	private boolean gotoExit;
	private boolean isExit;
	private String removeState;
	private DialNode linkedNode;
	
	public DialNode() {
		childs = new ArrayList<DialNode>();
	}
	
	public void addChild(DialNode child) {
		childs.add(child);
	}
	
	public List<DialNode> getChilds() {
		return childs;
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

	public DialStrat getStrategy() {
		return strategy;
	}

	public void setStrategy(DialStrat strategy) {
		this.strategy = strategy;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public DialWho getWho() {
		return who;
	}

	public void setWho(DialWho who) {
		this.who = who;
	}

	public boolean isGotoExit() {
		return gotoExit;
	}

	public void setGotoExit(boolean exit) {
		this.gotoExit = exit;
	}

	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}

	public String getSay() {
		return say;
	}

	public void setSay(String say) {
		this.say = say;
	}

	public String getRemoveState() {
		return removeState;
	}

	public void setRemoveState(String removeState) {
		this.removeState = removeState;
	}

	public DialNode getLinkedNode() {
		return linkedNode;
	}

	public void setLinkedNode(DialNode linkedNode) {
		this.linkedNode = linkedNode;
	}
}
