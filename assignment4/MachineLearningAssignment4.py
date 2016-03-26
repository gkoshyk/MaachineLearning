
# coding: utf-8

# # Machine learning Assignment 4

# In[1]:

# Text classification is one of the fundamental tasks in data mining and machine learning. In this assignment,
# you will get an opportunity to train classifiers to recognize the topics represented by documents.
# You will use the 20 Newsgroups dataset. It contains newsgroup documents relating to 20 different topics. 
# It can be downloaded from: http://qwone.com/~jason/20Newsgroups/
# There are 3 different versions available for download. It is recommended that you use the "bydate" version.
# It has documents split up into training and testing sets.
# You can limit yourself to any 5 topics that are most interesting to you. 
# The topics can be inferred from the names of the directories.


# In[2]:

# Install python 2.7 

# Install jupyter command
#    pip install jupyter

# Install latest dev version of sklearn command
#    pip install git+git://github.com/scikit-learn/scikit-learn.git

# Start jupyter command 
#    ipython notebook


# In[3]:

from __future__ import print_function

import sys
from time import time
# Module to import the fetch_20newsgroups data
from sklearn.datasets import fetch_20newsgroups
import numpy as np


# In[4]:

# The five categories of docs which we are going to use to train the model.
categories = ['alt.atheism', 'soc.religion.christian','comp.graphics', 'sci.med','talk.politics.mideast']


# In[5]:

twenty_train = fetch_20newsgroups(subset='train',categories=categories, shuffle=True, random_state=42)
#The topic classes chosen
twenty_train.target_names


# # Extracting features from text files

# In[6]:

# Text preprocessing, tokenizing and filtering of stopwords are included in a high level component 
# that is able to build a dictionary of features and transform documents to feature vectors:
from sklearn.feature_extraction.text import CountVectorizer
count_vect = CountVectorizer()
X_train_counts = count_vect.fit_transform(twenty_train.data)
X_train_counts.shape


# In[7]:

# Both tf and tf–idf can be computed as follows:
from sklearn.feature_extraction.text import TfidfTransformer
tf_transformer = TfidfTransformer(use_idf=False).fit(X_train_counts)
X_train_tf = tf_transformer.transform(X_train_counts)
X_train_tf.shape

# we firstly use the fit(..) method to fit our estimator to the data and secondly the transform(..) 
# method to transform our count-matrix to a tf-idf representation. 
# These two steps can be combined to achieve the same end result faster by skipping redundant processing. 
# This is done through using the fit_transform(..) method as shown below, and as mentioned in the note 
# in the previous section:
tfidf_transformer = TfidfTransformer()
X_train_tfidf = tfidf_transformer.fit_transform(X_train_counts)
X_train_tfidf.shape


# # Importing the required machine learning modules

# In[8]:

from sklearn.naive_bayes import MultinomialNB
from sklearn.linear_model import SGDClassifier
from sklearn.svm import LinearSVC
from sklearn.linear_model import Perceptron
from sklearn.linear_model import PassiveAggressiveClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.neighbors import NearestCentroid
from sklearn import tree

from sklearn import metrics

import numpy as np
from sklearn.pipeline import Pipeline


# # Building a pipeline and training the classifier and Evaluation of the performance on the test set

# In[9]:

classifier_map = {"Multinomial Naive Bayes Algorithm" : MultinomialNB(),
                "Stochastic Gradient Descent Algorithm" : SGDClassifier(loss='hinge', penalty='l2',alpha=1e-3, n_iter=5, random_state=42),
                "Support Vector Classifier Algorithm" : LinearSVC(penalty="l1", dual=False, tol=1e-3),
                "Perceptron Algorithm" :Perceptron(n_iter=50),
                "Passive Aggressive Classifier Algorithm": PassiveAggressiveClassifier(n_iter=50),
                "K nearest neighbor Algorithm": KNeighborsClassifier(n_neighbors=10),
                "Random Forest Algorithm" : RandomForestClassifier(n_estimators=100),
                "Nearest Centroid Algorithm" : NearestCentroid(),
                "Decision Tree Algorithm" : tree.DecisionTreeClassifier()}


# In[10]:

twenty_test = fetch_20newsgroups(subset='test',categories=categories, shuffle=True, random_state=42)
docs_test = twenty_test.data

f = open('ReportOfRunningScript.txt','a')

for classifier_name, classifier in classifier_map.iteritems():
    text_clf = Pipeline([('vect', CountVectorizer()),
                     ('tfidf', TfidfTransformer()),
                     ('clf', classifier),
                    ])
    text_clf = text_clf.fit(twenty_train.data, twenty_train.target)
    predicted = text_clf.predict(docs_test)
    
    np.mean(predicted == twenty_test.target)
    print("The accuracy after running %s is %f\n" %(classifier_name,np.mean(predicted == twenty_test.target)))
    f.write("The accuracy after running %s is %f\n" %(classifier_name,np.mean(predicted == twenty_test.target)))
    print("The table metrics for %s is " %classifier_name)
    f.write("The table metrics for %s is " %classifier_name)
    print(metrics.classification_report(twenty_test.target, predicted,target_names=twenty_test.target_names))    
    f.write(metrics.classification_report(twenty_test.target, predicted,target_names=twenty_test.target_names))    
    print("****************************************************************")
    f.write("****************************************************************")    
f.close()


# In[ ]:



