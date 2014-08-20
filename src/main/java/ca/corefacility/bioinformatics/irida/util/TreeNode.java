package ca.corefacility.bioinformatics.irida.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Simple generic tree node with links to parent and a list of children
 * 
 * @author Thomas Matthews <thomas.matthews@phac-aspc.gc.ca>
 *
 * @param <ValueType>
 *            The value to be stored by this node
 */
public class TreeNode<ValueType> {
	// parent node of this node
	private TreeNode<ValueType> parent;

	// All the childen of the node. May be empty
	private List<TreeNode<ValueType>> children;

	// The value to be stored by this node
	private ValueType value;

	/**
	 * Create a new {@link TreeNode} with a given value
	 * 
	 * @param value
	 *            The value to set
	 */
	public TreeNode(ValueType value) {
		this.value = value;
		children = new ArrayList<>();
	}

	/**
	 * Get a list of all the children of this node
	 * 
	 * @return A List<TreeNode>
	 */
	public List<TreeNode<ValueType>> getChildren() {
		return children;
	}

	/**
	 * Add a child to the children list
	 * 
	 * @param node
	 *            The child to add
	 */
	public void addChild(TreeNode<ValueType> node) {
		children.add(node);
	}

	/**
	 * Get the value stored by this node
	 * 
	 * @return
	 */
	public ValueType getValue() {
		return value;
	}

	/**
	 * Set the value for this node
	 * 
	 * @param value
	 */
	public void setValue(ValueType value) {
		this.value = value;
	}

	/**
	 * Set the parent node
	 * 
	 * @param parent
	 */
	public void setParent(TreeNode<ValueType> parent) {
		this.parent = parent;
	}

	/**
	 * Get the parent node
	 * 
	 * @return
	 */
	public TreeNode<ValueType> getParent() {
		return parent;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TreeNode) {
			@SuppressWarnings("rawtypes")
			TreeNode other = (TreeNode) obj;
			return Objects.equals(value, other.value);
		}
		return false;
	}

	@Override
	public String toString() {
		return "TreeNode[" + value.toString() + "]";
	}

}
