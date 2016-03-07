
# coding: utf-8

# # Machine learning Assignment 3

# In[12]:

# Comparing the performance of classifiers
# • Decision Trees
# • Perceptron (Single Linear Classifier)
# • Neural Net
# • Support Vector Machines
# • Naïve Bayes Classifiers

# Implemented code on github
# https://github.com/gkoshyk/MachineLearning/tree/master/assignment3


# # Prerequisites to install

# In[13]:

# Install python 2.7 

# Install jupyter command
#    pip install jupyter

# Install latest dev version of sklearn command
#    pip install git+git://github.com/scikit-learn/scikit-learn.git

# Install pandas command
#    pip install pandas

# Start jupyter command 
#    ipython notebook


# # General Imports

# In[14]:

from __future__ import division
import pandas as pd
import os
print(os.getcwd() + "\n")


# # Loading the datasets into dataframes
# Location(https://archive.ics.uci.edu/ml/datasets.html)

# In[15]:

# Credit card defaulting data set.
credit_card_default_df = pd.read_excel("datasets/dataset1/default of credit card clients.xls",
                                       headers = "True", parse_dates="True")
credit_card_default_df = credit_card_default_df.drop('ID', 1)
credit_card_default_df=credit_card_default_df.rename(columns = {'default payment next month':'class'})


# In[16]:

#Ionosphere data set
ionosphere_df = pd.read_csv("datasets/dataset2/ionosphere.txt", header = None, parse_dates="True")
ionosphere_df = ionosphere_df.rename(columns = {34:'class'})


# In[17]:

#Phishing data set
phishing_df = pd.read_csv("datasets/dataset3/phishing.txt", header = None, parse_dates="True")
phishing_df = phishing_df.rename(columns = {30:'class'})


# In[18]:

# Transfusion Data Set
transfusion_df = pd.read_csv("datasets/dataset4/transfusion.data", header = None, parse_dates="True")
transfusion_df = transfusion_df.rename(columns = {4:'class'})


# In[19]:

# Breast Cancer Diagnostic Data set
breast_cancer_diagnostic_df = pd.read_csv("datasets/dataset5/Wisconsin_Diagnostic_Breast_Cancer.txt",
                                          header = None, parse_dates="True")
breast_cancer_diagnostic_df = breast_cancer_diagnostic_df.rename(columns = {1:'class'})


# # Importing the sklearn algorithms(Need sklearn latest dev version 0.18dev)

# In[20]:

# Importing the algorithms required to run
from sklearn.cross_validation import train_test_split, StratifiedShuffleSplit, LeaveOneOut

from sklearn import tree

from sklearn.linear_model import Perceptron


#Install the latest dev version 0.18dev to get neural network code with the following instructions below

# http://stackoverflow.com/questions/33568244/upgrade-to-dev-version-of-scikit-learn-on-anaconda
# pip install git+git://github.com/scikit-learn/scikit-learn.git
# This command runs perfectly fine on a MAC.

# After installation shut down and open ipython and run the code again to see the import working perfectly fine.

from sklearn.neural_network import MLPClassifier

from sklearn import svm

from sklearn.naive_bayes import GaussianNB


# In[21]:

# Map of all the datasets to help in accuracy printing
data_frame_map = {"Credit Card Approval DataSet" : credit_card_default_df,
                    "Phishing Data Set" : phishing_df,                    
                    "Ionosphere Data Set" : ionosphere_df,
                    "Transfusion Data Set" : transfusion_df,
                    "Breast Cancer Diagnostic Data Set" : breast_cancer_diagnostic_df}

# Map of all the five classifiers to help in accuracy printing
classifier_map = {"Decision Tree " : tree.DecisionTreeClassifier(),
                   "Perceptron " : Perceptron(fit_intercept=False, n_iter=10, shuffle=False),
                   "Neural Network " : MLPClassifier(algorithm='sgd', alpha=1e-5, hidden_layer_sizes=(5, 2), random_state=1),
                   "Support Vector Machines" : svm.SVC(),
                   "Naive Bayes" :GaussianNB()}


# In[22]:

#Implementing the pseudo code.

for data_frame_key, data_frame_value in data_frame_map.iteritems():
    
    classLabel = data_frame_value.pop('class')
    print ("Number of instances and number of attributes in %s dataset are %d and %d" % (data_frame_key,len(data_frame_value.axes[0]),len(data_frame_value.axes[1])))
    f = open('ReportOfRunningScript.txt','a')
    f.write("Number of instances and number of attributes in %s dataset are %d and %d" % (data_frame_key,len(data_frame_value.axes[0]),len(data_frame_value.axes[1])))
    f.close()
    
    # Variable to initialize random_state attribute to select different train test split each time. Used in splitting inside the loop.
    random_sample_selection_number = [42, 17]
    
    for number_of_times_to_run in range(1,3):

        Xtrain, Xtest, ytrain, ytest = train_test_split(data_frame_value,classLabel,test_size=0.2,random_state=random_sample_selection_number.pop())

        for clf_key, clf_value in classifier_map.iteritems():
            clf = clf_value.fit(Xtrain, ytrain)
            score = clf.score(Xtest,ytest)
            print("Accuracy of %s after running with %s algorithm %d time is %f" % (data_frame_key, clf_key, number_of_times_to_run,score))
            f = open('ReportOfRunningScript.txt','a')
            f.write("Accuracy of %s after running with %s algorithm %d time is %f" % (data_frame_key, clf_key, number_of_times_to_run,score))
            f.close()

