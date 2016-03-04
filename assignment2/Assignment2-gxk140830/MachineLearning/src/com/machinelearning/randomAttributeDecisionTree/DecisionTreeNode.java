package com.machinelearning.randomAttributeDecisionTree;

/*
 * Node class for Decision Tree 
 */
public class DecisionTreeNode {
	private DecisionTreeNode parent;
	private int oneCount;
	private int zeroCount;
	private double entropy;
	private double infGain;
	private String data;
	private int level;
	private DecisionTreeNode leftNode;
	private DecisionTreeNode rightNode;

	public DecisionTreeNode() {

	}

	/*
	 * This is used to provide indentation while printing the tree.
	 */
	public DecisionTreeNode(String data, int level, double entropy, double infGain, int posCount, int negCount) {
		this.data = data;
		this.level = level;
		this.entropy = entropy;
		this.infGain = infGain;
		this.oneCount = posCount;
		this.zeroCount = negCount;
	}

	public DecisionTreeNode getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(DecisionTreeNode leftNode) {
		this.leftNode = leftNode;
	}

	public DecisionTreeNode getRightNode() {
		return rightNode;
	}

	public void setRightNode(DecisionTreeNode rightNode) {
		this.rightNode = rightNode;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public DecisionTreeNode getParent() {
		return parent;
	}

	public void setParent(DecisionTreeNode parent) {
		this.parent = parent;
	}

	public int getOneCount() {
		return oneCount;
	}

	public void setOneCount(int oneCount) {
		this.oneCount = oneCount;
	}

	public int getZeroCount() {
		return zeroCount;
	}

	public void setZeroCount(int zeroCount) {
		this.zeroCount = zeroCount;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public double getInfGain() {
		return infGain;
	}

	public void setInfGain(double infGain) {
		this.infGain = infGain;
	}

}