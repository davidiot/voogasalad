package authoring.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class InteractionTreeNode {
	
	private List<InteractionTreeNode> children;
	private String value;
	
	public InteractionTreeNode () {
		this(null);
	}
	
	public InteractionTreeNode (String value) {
		this.value = value;
		this.children = new ArrayList<InteractionTreeNode>();
	}

	public InteractionTreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	public int getChildCount() {
		return children.size();
	}
	
	public List<InteractionTreeNode> children () {
		return Collections.unmodifiableList(children);
	}

	public void addChild (InteractionTreeNode n) {
		children.add(n);
	}
	public void remove(int index) {
		children.remove(index);
	}

	public void remove(InteractionTreeNode node) {
		children.remove(node);
	}
	
	public String getValue () {
		return value;
	}
}
//
//// possible alternative
//class TriggerNode {
//	private List<Actor> actors;
//	private List<String> actions;
//	private List<Condition> conditions;
//	public TriggerNode () {
//		actors = new ArrayList<Actor>();
//		actions = new ArrayList<String>();
//		conditions = new ArrayList<String>();
//	}
//	public void run () {
//		// for each combination of actors:
//		for (String condition : conditions) {
//			// run scripts
//		}
//		// if all true:
//		for (String action : actions) {
//			// run scripts
//		}
//	}
//	private boolean applyGroovy (String script) {
//		
//	}
//}
