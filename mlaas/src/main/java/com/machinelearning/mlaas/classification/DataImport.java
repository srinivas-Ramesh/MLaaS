package com.machinelearning.mlaas.classification;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class DataImport {

	public Instances createDataSet(List<String> attributeList) {

		ArrayList<Attribute> attributes = new ArrayList<>();
		for (String attributeName : attributeList) {
			attributes.add(new Attribute(attributeName,(ArrayList<String>)null));
		}

		Instances dataSet = new Instances("DataSet", attributes, 0);
		return dataSet;
	}

	public Instances addDataToDataSet(Instances dataSet, JsonArray dataArray, String type) {

		Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

		if (type.equalsIgnoreCase("String")) {
			String[][] stringDataArray = gsonPretty.fromJson(dataArray, String[][].class);

			for (int i = 0; i < stringDataArray.length; i++) {
				for (int j = 0; j < stringDataArray[i].length; j++) {
					dataSet.attribute(i).addStringValue(stringDataArray[i][j]);
				}
			}
		} else {
			double[][] numericDataArray = gsonPretty.fromJson(dataArray, double[][].class);
			
			for (int i = 0; i < numericDataArray[0].length; i++) {
				for (int j = 0; j < numericDataArray[1].length; j++) {
					dataSet.attribute(i).setWeight(numericDataArray[i][j]);
				}
			}
		}
		return dataSet;
	}

}
