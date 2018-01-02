package com.machinelearning.mlaas.classification;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Enumeration;

import com.machinelearning.mlaas.datamodel.DataModel;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instances;

@SuppressWarnings("deprecation")
public class Classification {

	public static void buildClassifier() throws Exception {
		String[] options = new String[1];
		options[0] = "-U";
		J48 classifierTree = new J48();
		classifierTree.setOptions(options);
		Instances trainingData = DataModel.getClassificationTrainingDataSet();
		trainingData.setClassIndex(trainingData.numAttributes() - 1);
		classifierTree.buildClassifier(trainingData);
		DataModel.setClassifierTree(classifierTree);
	}

	public static Instances classifyTestingData() throws Exception {

		J48 classifierTree = DataModel.getClassifierTree();
		Instances testingData = DataModel.getClassificationTestingDataSet();
		Instances classifiedTestingData = new Instances(testingData);
		Enumeration<Object> classValues = testingData.classAttribute().enumerateValues();
		List<Object> classAttributeValues = Collections.list(classValues);

		for (int i = 0; i <= testingData.size() - 1; i++) {
			Double value = classifierTree.classifyInstance(testingData.instance(i));
			String classValueString = classAttributeValues.get(value.intValue()).toString();
			classifiedTestingData.instance(i).setClassValue(classValueString);
		}
		return classifiedTestingData;
	}

}
