package com.machinelearning.ID3;

import java.util.List;

import com.machinelearning.ID3.CSVInputReader.Tuple;

/*  
 * Implementation of DecisionTree - ID3 Algorithm 
 */
public class DecisionTreeImpl {

	public static void main(String[] args) {
		// Check if zero arguments are given.
		if (args.length == 0 || args == null || args[0] == null || args[0].trim().length() == 0) {
			System.out.println("You have to enter arguments, type --usage to see sample format!");
			System.exit(1);
		}

		// Print usage of the program
		if ("--usage".equals(args[0])) {
			printUsage();
			System.exit(1);
		}

		// Print usage if number of arguments are less than 5
		if (args.length < 5) {
			System.out.println("The program needs exactly five inputs to run!");
			printUsage();
			System.exit(1);
		}

		try {
			CSVInputReader input = new CSVInputReader();
			// Load the training data from argument
			List<Tuple> trainData = input.loadData(args[1]);
			if (trainData == null) {
				System.err.println("Error loading in training Data");
				System.exit(1);
			}

			List<String> attributeNames = input.getAttributeNames();
			DecisionTree dt = new DecisionTree();
			System.out.println("Running ID3 algorithm");

			// Creating a decision tree.
			dt.createDT(trainData, attributeNames);

			// 1000 is to create 1000 pruned trees eliminating
			// Integer.parseInt(args[0]) number of random nodes.
			runID3(dt, args[2], args[3], args[4], Integer.parseInt(args[0]));
			System.out.println("Completed running ID3 algorithm");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/*
	 * Function to describe usage of the program.
	 */
	public static void printUsage() {
		System.out.println("Usage Format?");
		System.out.println("NumberOfNodesToPrune <training-set> <validation-set> <test-set> PrintOrNot");
	}

	/*
	 * Method to run tests to choose the best decision tree after pruning
	 */
	public static void runID3(DecisionTree decisionTree, String validationFile, String testFile, String option,
			int numberOfNodesToPrune) throws Exception {
		if ("1".equalsIgnoreCase(option)) {
			System.out.println("Started printing the Unpruned Decision Tree");
			decisionTree.printTree(decisionTree.root);
			System.out.println("End of printing the Unpruned Decision Tree");
		}
		CSVInputReader test = new CSVInputReader();

		// Loading the test data to calculate the accuracy before pruning any
		// nodes.
		List<Tuple> testData = test.loadData(testFile);
		if (testData == null) {
			System.err.println("Error in loading the test data!");
			System.exit(1);
		}
		double accuracyBeforePruning = decisionTree.evaluateTree(testData);

		System.out.println("Accuracy before pruning is" + accuracyBeforePruning);

		CSVInputReader csvInputReader = new CSVInputReader();
		// Loading validation data for pruning
		List<Tuple> validationData = csvInputReader.loadData(validationFile);

		if (validationData == null) {
			System.err.println("Error in loading the validation Data");
			System.exit(1);
		}

		DecisionTreeNode decisionTreeBest = decisionTree.postPrune(numberOfNodesToPrune, validationData);
		double accuracyAfterPruning = decisionTree.evaluateTreeUtil(decisionTreeBest, testData);
		if ("1".equalsIgnoreCase(option)) {
			System.out.println("Printing the Pruned Tree");
			decisionTree.printTree(decisionTreeBest);
			System.out.println("Finished printing the Pruned Tree");
		}
		System.out.println("Accuracy after pruning is" + accuracyAfterPruning);
	}
}