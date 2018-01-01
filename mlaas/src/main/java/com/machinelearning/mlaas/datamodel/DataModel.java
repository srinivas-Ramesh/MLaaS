package com.machinelearning.mlaas.datamodel;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class DataModel {

	private static Instances classificationTrainingDataSet;
	private static Instances classificationTestingDataSet;
	private static Instances clusteringDataSet;
	private static SimpleKMeans kMeans;
	private static J48 classifierTree;
	private static Evaluation classicationEvaluation;

	public static Evaluation getClassicationEvaluation() {
		return classicationEvaluation;
	}

	public static void setClassicationEvaluation(Evaluation classicationEvaluation) {
		DataModel.classicationEvaluation = classicationEvaluation;
	}

	public static J48 getClassifierTree() {
		return classifierTree;
	}

	public static void setClassifierTree(J48 classifierTree) {
		DataModel.classifierTree = classifierTree;
	}

	public static SimpleKMeans getkMeans() {
		return kMeans;
	}

	public static void setkMeans(SimpleKMeans kMeans) {
		DataModel.kMeans = kMeans;
	}

	public static Instances getClusteringDataSet() {
		return clusteringDataSet;
	}

	public static void setClusteringDataSet(Instances dataSet) {
		DataModel.clusteringDataSet = dataSet;
	}

	public static Instances getClassificationTrainingDataSet() {
		return classificationTrainingDataSet;
	}

	public static void setClassificationTrainingDataSet(Instances classificationDataSet) {
		DataModel.classificationTrainingDataSet = classificationDataSet;
	}

	public static Instances getClassificationTestingDataSet() {
		return classificationTestingDataSet;
	}

	public static void setClassificationTestingDataSet(Instances classificationTestingDataSet) {
		DataModel.classificationTestingDataSet = classificationTestingDataSet;
	}
}
