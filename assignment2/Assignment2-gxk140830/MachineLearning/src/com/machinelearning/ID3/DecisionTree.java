package com.machinelearning.ID3;

import com.machinelearning.ID3.CSVInputReader.Tuple;
import java.util.ArrayList;
import java.util.List;

/*
 * Class containing the helpers for find the smallest tree using ID3 algorithm
 */
public class DecisionTree {
	public DecisionTreeNode root;
	private List<String> attributeList;

	public void createDT(List<Tuple> datapoints, List<String> attriList) {
		root = new DecisionTreeNode();
		this.attributeList = attriList;
		createDTUtil(root, datapoints, attriList, 0);
	}

	public void createDTUtil(DecisionTreeNode node, List<Tuple> datapoints, List<String> availAttributes, int level) {
		double rootEntropy = 0;
		int[] O1Count = { 0, 0 };
		rootEntropy = calculateRootEntropy(datapoints, O1Count);
		node.setOneCount(O1Count[1]);
		node.setZeroCount(O1Count[0]);

		// Base conditions
		if (rootEntropy == 0) {
			// If number of negative data points classes is 0 set data to one.
			node.setData("" + (O1Count[0] == 0 ? "1" : "0"));
			return;
		}

		if (availAttributes.size() == 1) {
			node.setData("" + (O1Count[0] > O1Count[1] ? "0" : "1"));
			return;
		}

		double maxInfGain = 0;
		int number_of_attributes = datapoints.get(0).attributeValues.length;
		int attributeChoosen = -1;
		double pEntropy = 0, nEntropy = 0;
		for (int column = 0; column < (number_of_attributes - 1); column++) {
			// if available attributes is not having that attribute
			if (!availAttributes.contains(this.attributeList.get(column)))
				continue;
			pEntropy = 0;
			nEntropy = 0;
			nEntropy = calculateChildEntropy(datapoints, "0", number_of_attributes, column);

			pEntropy = calculateChildEntropy(datapoints, "1", number_of_attributes, column);
			double gain = rootEntropy - (nEntropy + pEntropy);
			if (gain > maxInfGain) {
				maxInfGain = gain;
				attributeChoosen = column;
			}

		} // attribute is chosen by now

		if (attributeChoosen == -1) {
			node.setData("" + (O1Count[0] > O1Count[1] ? "0" : "1"));
			return;
		}

		List<Tuple> zeroDataPoints = new ArrayList<Tuple>();
		List<Tuple> oneDataPoints = new ArrayList<Tuple>();
		List<String> availAttr_Zero = new ArrayList<String>(availAttributes);
		List<String> availAttr_one = new ArrayList<String>(availAttributes);
		for (int i = 0; i < datapoints.size(); i++) {
			if (datapoints.get(i).attributeValues[attributeChoosen].equalsIgnoreCase("0"))
				zeroDataPoints.add(datapoints.get(i));
			else if (datapoints.get(i).attributeValues[attributeChoosen].equalsIgnoreCase("1"))
				oneDataPoints.add(datapoints.get(i));
		}

		node.setData(this.attributeList.get(attributeChoosen));
		level++;
		DecisionTreeNode zeroNode = new DecisionTreeNode();
		zeroNode.setEntropy(nEntropy);
		zeroNode.setParent(node);
		zeroNode.setLevel(level);
		node.setLeftNode(zeroNode);

		if (zeroDataPoints.size() == 1) {
			zeroNode.setData("" + zeroDataPoints.get(0).attributeValues[number_of_attributes - 1]);
		} else {
			availAttr_Zero.remove(availAttr_Zero.indexOf(node.getData()));
			createDTUtil(zeroNode, zeroDataPoints, availAttr_Zero, level);
		}

		DecisionTreeNode oneNode = new DecisionTreeNode();
		oneNode.setEntropy(pEntropy);
		oneNode.setParent(node);
		oneNode.setLevel(level);
		node.setRightNode(oneNode);

		if (oneDataPoints.size() == 1) {
			oneNode.setData("" + oneDataPoints.get(0).attributeValues[number_of_attributes - 1]);
		} else {
			availAttr_one.remove(availAttr_one.indexOf(node.getData()));
			createDTUtil(oneNode, oneDataPoints, availAttr_one, level);
		}

		level--;

	}

	public double calculateRootEntropy(List<Tuple> datapoints, int[] O1Count) {
		double rootEntropy = 0;
		// For all the data points in the training data.
		for (int index = 0; index < datapoints.size(); index++) {
			// Counting the number of zeroes and ones of the class for root
			// entropy.
			if (datapoints.get(index).attributeValues[datapoints.get(index).attributeValues.length - 1]
					.equalsIgnoreCase("0"))
				O1Count[0]++;
			else if (datapoints.get(index).attributeValues[datapoints.get(index).attributeValues.length - 1]
					.equalsIgnoreCase("1"))
				O1Count[1]++;
		}
		// At least one is not zero.
		if (O1Count[1] != 0 && O1Count[0] != 0) {
			rootEntropy = calEntropy(O1Count[1], O1Count[0]);
			return rootEntropy;
		}
		return rootEntropy;
	}

	public double calculateChildEntropy(List<Tuple> datapoints, String outputClass, int noAttr, int column) {
		double entropy = 0;
		int colCount = 0, zeroCount = 0, oneCount = 0;
		for (int i = 0; i < datapoints.size(); i++) {
			String outputValue = datapoints.get(i).attributeValues[noAttr - 1];
			if (datapoints.get(i).attributeValues[column].equalsIgnoreCase(outputClass)) {
				colCount++;
				if (outputValue.equalsIgnoreCase("0"))
					zeroCount++;
				else if (outputValue.equalsIgnoreCase("1"))
					oneCount++;
			}
		}
		double pPos = (double) oneCount / (double) (colCount);
		double pNeg = (double) zeroCount / (double) (colCount);
		if (pPos > 0 && pNeg > 0) {
			entropy = -(pPos * (Math.log10(pPos) / Math.log10(2))) - (pNeg * (Math.log10(pNeg) / Math.log10(2)));
			return ((double) colCount / (double) (datapoints.size())) * entropy;
		}
		return entropy;
	}

	public void getCount(List<Tuple> dataPoints, String outputClass, int outputColumn, int curCol, int[] O1count) {
		for (int i = 0; i < dataPoints.size(); i++) {
			String outputValue = dataPoints.get(i).attributeValues[outputColumn];
			if (dataPoints.get(i).attributeValues[curCol].equalsIgnoreCase(outputClass)) {
				O1count[2]++;
				if (outputValue.equalsIgnoreCase("0"))
					O1count[0]++;
				else if (outputValue.equalsIgnoreCase("1"))
					O1count[1]++;
			}
		}
	}

	/*
	 * This function calculates Entropy of a given Node Entropy(S) = -p(+) log
	 * p(+) - p(-) log p(-)
	 */
	public double calEntropy(int posCount, int negCount) {
		int TotalCount = posCount + negCount;
		return -((double) posCount / (double) (TotalCount))
				* (Math.log10((double) posCount / (double) (TotalCount)) / Math.log10(2))
				- ((double) negCount / (double) (TotalCount))
						* (Math.log10((double) negCount / (double) (TotalCount)) / Math.log10(2));
	}

	public void printTree(DecisionTreeNode root) {
		printTreeUtil(root);
	}

	/*
	 * Function to print the tree in the format as specified in the assignment
	 */
	public void printTreeUtil(DecisionTreeNode node) {

		String indentation = "";

		for (int i = 0; i < node.getLevel(); i++) {
			indentation = indentation + "|";
		}

		if (node.getLeftNode().getData().equalsIgnoreCase("0") || node.getLeftNode().getData().equalsIgnoreCase("1")) {
			System.out.println(indentation + node.getData() + " = 0 : " + node.getLeftNode().getData());
		} else {
			System.out.println(indentation + node.getData() + " = 0 :");
			printTreeUtil(node.getLeftNode());
		}

		if (node.getRightNode().getData().equalsIgnoreCase("0")
				|| node.getRightNode().getData().equalsIgnoreCase("1")) {
			System.out.println(indentation + node.getData() + " = 1 : " + node.getRightNode().getData());
		} else {
			System.out.println(indentation + node.getData() + " = 1 :");
			printTreeUtil(node.getRightNode());
		}

	}

	public double evaluateTree(List<Tuple> testData) {
		return evaluateTreeUtil(root, testData);
	}

	/*
	 * Evaluate Tree : Function to evaluate the accuracy of tree
	 */

	public double evaluateTreeUtil(DecisionTreeNode root, List<Tuple> testData) {
		int testPass = 0, testFail = 0;
		for (int i = 0; i < testData.size(); i++) {
			String[] copyAttr = testData.get(i).attributeValues;
			String output = evaulateSample(root, copyAttr);
			if ((output.equals("0") && copyAttr[copyAttr.length - 1].equals("0"))
					|| (output.equals("1") && copyAttr[copyAttr.length - 1].equals("1"))) {
				testPass++;
			} else if ((output.equals("0") && copyAttr[copyAttr.length - 1].equals("1"))
					|| (output.equals("1") && copyAttr[copyAttr.length - 1].equals("0"))) {
				String str = "";
				for (int j = 0; j < copyAttr.length; j++) {
					str = str + copyAttr[j] + ",";
				}
				testFail++;
			}
		}

		return ((double) testPass / (testPass + testFail));
	}

	public String evaulateSample(DecisionTreeNode root, String[] instance) {
		return evaluateSampleDataUtil(root, instance);
	}

	public String evaluateSampleDataUtil(DecisionTreeNode node, String[] instance) {

		int column = -1;
		String retVal = "";

		// The Leaf has value 0 or 1
		if (node.getData().equals("0") || node.getData().equals("1")) {
			return node.getData();
		}

		for (int i = 0; i < instance.length; i++) {
			// Match with the root which attributes to be picked and what is the
			// Value at that attribute
			if (this.attributeList.get(i).equalsIgnoreCase(node.getData())) {
				column = i;
				break;
			}
		}
		// If its zero in the instance then go to left else right
		if (instance[column].equalsIgnoreCase("0")) {
			retVal = evaluateSampleDataUtil(node.getLeftNode(), instance);
		} else {
			retVal = evaluateSampleDataUtil(node.getRightNode(), instance);
		}

		return retVal;
	}

	/*
	 * Method to clone a DecisionTree
	 */
	private DecisionTreeNode cloneDTree(DecisionTreeNode node) {
		DecisionTreeNode newNode = new DecisionTreeNode(node.getData(), node.getLevel(), node.getEntropy(),
				node.getInfGain(), node.getOneCount(), node.getZeroCount());

		if (node.getLeftNode() != null) {
			newNode.setLeftNode(cloneDTree(node.getLeftNode()));
		}

		if (node.getRightNode() != null) {
			newNode.setRightNode(cloneDTree(node.getRightNode()));
		}

		return newNode;
	}

	/*
	 * This function is used to prune the decision tree based on the logic
	 * provided.
	 */
	public DecisionTreeNode postPrune(int numberOfNodesToPrune, List<Tuple> validationSet) {

		DecisionTreeNode dBest = cloneDTree(root);
		double bestAccuracy = evaluateTreeUtil(dBest, validationSet);
		System.out.println("Best Accuary with Validation Set without Pruning: " + bestAccuracy);

		// Construct 1000 pruned trees with K number of nodes pruned.
		for (int i = 0; i < 1000; i++) {
			DecisionTreeNode dTemp = cloneDTree(root);

			// Prune K nodes from one single tree
			for (int j = 0; j < numberOfNodesToPrune; j++) {
				int N = getNonLeafNodeCount(dTemp);
				int P = (int) (Math.floor(Math.random() * N) + 1);
				// System.out.println("Node chosen to be pruned: " + P);
				pruneNode(dTemp, P);
			}

			double accuracy = evaluateTreeUtil(dTemp, validationSet);
			// Choose the best pruned tree
			if (accuracy > bestAccuracy) {
				dBest = dTemp;
				bestAccuracy = accuracy;
			}
		}
		System.out.println("The average depth of the best tree is: " + averageDepthOfTree(dBest));
		System.out.println("Total number of nodes in the best tree is: " + getTotalNodeCount(dBest));
		return dBest;
	}

	/*
	 * Method to retrieve the number of non leaf nodes
	 */
	public int getNonLeafNodeCount(DecisionTreeNode node) {
		return (node.getLeftNode() != null ? getNonLeafNodeCount(node.getLeftNode()) : 0)
				+ (node.getRightNode() != null ? getNonLeafNodeCount(node.getRightNode()) : 0)
				+ (node.getData().equals("0") || node.getData().equals("1") ? 0 : 1);
	}

	/*
	 * Method to retrieve the total number of nodes in the tree
	 */
	public int getTotalNodeCount(DecisionTreeNode node) {
		// if it's null, it doesn't exist, return 0 
		  if (node == null) return 0;
		  // count myself + my left child + my right child
		  return 1 + getTotalNodeCount(node.getLeftNode()) + getTotalNodeCount(node.getRightNode());
	}

	/*
	 * Sum of depth of leaf nodes
	 * Nodes
	 */
	public int sumOfLeafDepths(DecisionTreeNode node, int depth) {
		// When called as sumOfLeafDepths(root,0), this will compute the
		// sum of the depths of all the leaves in the tree to which root
		// points. When called recursively, the depth parameter gives
		// the depth of the node, and the routine returns the sum of the
		// depths of the leaves in the subtree to which node points.
		// In each recursive call to this routine, depth goes up by one.
		if (node == null) {
			// Since the tree is empty and there are no leaves,
			// the sum is zero.
			return 0;
		} else if (node.getLeftNode() == null && node.getRightNode() == null) {
			// The node is a leaf, and there are no subtrees of node, so
			// the sum of the leaf depths is just the depth of this node.
			return depth;
		} else {
			// The node is not a leaf. Return the sum of the
			// the depths of the leaves in the subtrees.
			return sumOfLeafDepths(node.getLeftNode(), depth + 1) + sumOfLeafDepths(node.getRightNode(), depth + 1);
		}
	}
	
	/*
	 * Average depth of tree = Sum of depth of leaf nodes / Total number of Leaf nodes
	 */
	public double averageDepthOfTree(DecisionTreeNode node){
		return (double)sumOfLeafDepths(node, 0)/(double)(getTotalNodeCount(node)-getNonLeafNodeCount(node));
	}

	/*
	 * Method to prune a particular node from a tree
	 */
	private int pruneNode(DecisionTreeNode node, int P) {
		if (P == 1) {
			P--;
			node.setData(node.getZeroCount() > node.getOneCount() ? "0" : "1");
			node.setLeftNode(null);
			node.setRightNode(null);
		} else {
			P--;
			if (P >= 1 && node.getLeftNode() != null
					&& !(node.getLeftNode().getData().equals("0") || node.getLeftNode().getData().equals("1"))) {
				P = pruneNode(node.getLeftNode(), P);
			}

			if (P >= 1 && node.getRightNode() != null
					&& !(node.getRightNode().getData().equals("0") || node.getRightNode().getData().equals("1"))) {
				P = pruneNode(node.getRightNode(), P);
			}

		}
		return P;
	}
}