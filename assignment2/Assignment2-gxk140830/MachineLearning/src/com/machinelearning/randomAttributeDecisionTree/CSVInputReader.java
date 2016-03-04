package com.machinelearning.randomAttributeDecisionTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
 * CSV file reader  
 */
public class CSVInputReader {

	// Variable to store all the attributes with last element as class
	private List<String> attributeNames = new ArrayList<String>();

	// Variable to store number of attributes which will be used across the
	// program
	private int numberOfAttributes;

	public class Tuple {
		// String array of the data points values
		public String[] attributeValues;

		public Tuple(int n) {
			attributeValues = new String[n];
		}
	}

	// List of all data points, each data point has a String array of all the
	// attribute values. Java maintains order of variables implementing the list
	// interface.
	List<Tuple> tuples = new ArrayList<Tuple>();

	public List<Tuple> loadData(String fileName) throws Exception {
		FileInputStream in = null;
		try {
			// Path path = Paths.get(fileName);
			// System.out.println(path.toAbsolutePath());
			File inputFile = new File(fileName);
			in = new FileInputStream(inputFile);
		} catch (Exception ex) {
			System.err.println("Unable to open File");
			return null;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		// first Line in the file is for all the attribute names
		String firstLine = br.readLine();
		// checking for data present in file or not
		if (firstLine == null || firstLine.length() == 0) {
			System.out.println("No data is found in the file");
			br.close();
			in.close();
			return null;
		}

		StringTokenizer tokens = new StringTokenizer(firstLine, ",");
		numberOfAttributes = tokens.countTokens();
		// attributeNames = new String[number_of_attributes];
		// get all attribute names
		// copy all the tokens to attribute names
		for (int i = 0; i < numberOfAttributes; i++) {
			// attributeNames[i] = tokens.nextToken();
			// System.out.println(attributeNames[i]);
			attributeNames.add(tokens.nextToken());
		}

		String nextLine;
		while ((nextLine = br.readLine()) != null) {
			tokens = new StringTokenizer(nextLine, ",");
			Tuple dp = new Tuple(numberOfAttributes);
			for (int i = 0; i < numberOfAttributes; i++) {
				dp.attributeValues[i] = tokens.nextToken();
				// Each data point is a row of attribute values of the data
			}
			tuples.add(dp);

		}

		br.close();
		in.close();
		return tuples;

	}

	public List<String> getAttributeNames() {
		return attributeNames;
	}

	public void setAttributeNames(List<String> attributeNames) {
		this.attributeNames = attributeNames;
	}

	public int getNumber_of_attributes() {
		return numberOfAttributes;
	}

	public void setNumber_of_attributes(int number_of_attributes) {
		this.numberOfAttributes = number_of_attributes;
	}
}