package com.machinelearning.mlaas.datamodel;

import java.util.ArrayList;

import com.google.gson.JsonArray;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class DataModel {

	public static ArrayList<String> attributeList;
	public static String type;
	public static JsonArray dataArray;
	private static Instances clusteringDataSet;
	private static SimpleKMeans kMeans;

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
}
