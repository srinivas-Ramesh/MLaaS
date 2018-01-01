package com.machinelearning.mlass.clustering;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machinelearning.mlaas.datamodel.DataModel;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class Clustering {

	public SimpleKMeans xMeancluster(Instances data, int clusterCount) {

		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setSeed(10);
		kmeans.setPreserveInstancesOrder(true);

		try {
			kmeans.setNumClusters(clusterCount);
			kmeans.buildClusterer(data);
			DataModel.setkMeans(kmeans);
			return kmeans;
			// This array returns the cluster number (starting with 0) for each instance
			// The array has as many elements as the number of instances
			// int[] assignments = kmeans.getAssignments();

		} catch (Exception e) {
			// handle the exception
			return null;
		}
	}

	public JsonObject buildClusterResult(SimpleKMeans kMeans, Instances data) throws Exception {

		JsonObject clusterResult = new JsonObject();

		/*
		 * Set the number of Clusters
		 */
		clusterResult.addProperty("clusters", kMeans.getNumClusters());

		/*
		 * Set the cluster Centroids
		 */
		Instances centroids = kMeans.getClusterCentroids();
		JsonArray centroidArray = new JsonArray();

		for (int i = 0; i < centroids.size(); i++) {
			centroidArray.add(centroids.get(i).toString());
		}
		clusterResult.add("centroids", centroidArray);

		/*
		 * Set the cluster number for instances
		 */
		JsonArray clusteredInstances = new JsonArray();
		int assignments[] = kMeans.getAssignments();

		for (int i = 0; i < data.size(); i++) {
			JsonObject instance = new JsonObject();
			instance.addProperty("instance", data.get(i).toString());
			instance.addProperty("cluster", assignments[i]);
			clusteredInstances.add(instance);
		}

		clusterResult.add("data", clusteredInstances);
		return clusterResult;
	}
}
