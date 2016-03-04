The zipped files have two different folder for the codes of ID3 and Random Attribute selection algorithms in building a decision tree.

The code of both the folders are the similar except for the and the logic for random attribute selection is shown with the following comment in the DecisionTree.java file in the random attribute code package.

// ********Randomly choosing attributes which is different from the ID3******

To run the program

Unzip the files

In commandline cd into the src folder.

For compiling all the java files use the command

javac com/machinelearning/ID3/*.java
javac com/machinelearning/randomAttributeDecisionTree/*.java

Command to run a algorithm
java mainClassName noOfNodesToPrune <trainData> <validationData> <testData> printOrNot

java com/machinelearning/ID3/DecisionTreeImpl 10 /Users/george/projects/eclipse_mars/projects/MachineLearning/src/com/machinelearning/ID3/data_sets1/training_set.csv /Users/george/projects/eclipse_mars/projects/MachineLearning/src/com/machinelearning/ID3/data_sets1/validation_set.csv /Users/george/projects/eclipse_mars/projects/MachineLearning/src/com/machinelearning/ID3/data_sets1/test_set.csv 1


java com/machinelearning/randomAttributeDecisionTree/DecisionTreeImpl 10 /Users/george/projects/eclipse_mars/projects/MachineLearning/src/com/machinelearning/ID3/data_sets1/training_set.csv /Users/george/projects/eclipse_mars/projects/MachineLearning/src/com/machinelearning/ID3/data_sets1/validation_set.csv /Users/george/projects/eclipse_mars/projects/MachineLearning/src/com/machinelearning/ID3/data_sets1/test_set.csv 0


The last parameter is to show or not show the tree.

The commands are to execute the program  on a MAC.

The data sets are not provided in this zip file as it would eat extra space in elearning.