package com.machinelearning.mlaas.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.machinelearning.mlaas.classification.Classification;
import com.machinelearning.mlaas.datamodel.DataModel;

import weka.core.Instances;

@Path("/classification")
public class ClassificationResource {

	@GET
	public Response getClassifierInfo() {

		if (DataModel.getClassificationTrainingDataSet() == null) {
			return Response.status(500).entity("Training Data Not Provided").build();
		}

		else if (DataModel.getClassifierTree() == null) {
			try {
				Classification.buildClassifier();
				return Response.ok("Classifier has been Built").build();
			} catch (Exception e) {
				return Response.status(500).entity(e.getMessage()).build();
			}
		}

		else {
			return Response.ok("Classifier has been Built").build();
		}
	}

	@Path("/upload/{dataSet}")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("dataSet") String dataSet) {

		if (uploadedInputStream == null) {
			return Response.status(400).entity("File not provided").build();
		} else {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(uploadedInputStream));
				Instances data = new Instances(reader);
				if (dataSet.equalsIgnoreCase("trainingSet")) {
					data.setClassIndex(data.numAttributes() - 1);
					DataModel.setClassificationTrainingDataSet(data);

					// clear the evaluation and classifier tree for previous
					// dataSet
					if (DataModel.getClassicationEvaluation() != null) {
						DataModel.setClassicationEvaluation(null);
					}
					if (DataModel.getClassifierTree() != null) {
						DataModel.setClassifierTree(null);
					}
					// Build Classifier
					Classification.buildClassifier();
					return Response.ok("Classification DataSet has been saved in the system and Classifier is trained",
							MediaType.TEXT_PLAIN).build();
				} else if (dataSet.equalsIgnoreCase("testingSet")) {
					data.setClassIndex(data.numAttributes() - 1);
					DataModel.setClassificationTestingDataSet(data);
					return Response.ok("Testing DataSet has been saved in the system", MediaType.TEXT_PLAIN).build();
				} else {
					return Response.status(404).entity("Not found!").build();
				}

			} catch (Exception e) {
				return Response.status(500).entity(e.getMessage()).build();
			}

		}
	}

	@Path("/validation")
	@GET
	public Response validateTestingDataSet() {

		if (DataModel.getClassificationTestingDataSet() == null) {
			return Response.status(400).entity("No Testing dataset provided").build();
		} else if (DataModel.getClassifierTree() == null) {
			return Response.status(400).entity("Calssifier has not been trained").build();
		} else if (DataModel.getClassificationTrainingDataSet() == null) {
			return Response.status(400).entity("No Training dataset provided").build();
		} else {
			try {
				Instances classifiedTestingData = Classification.classifyTestingData();
				JsonArray result = prepareTestingDataResult(classifiedTestingData);
				return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(500).entity(e.getMessage()).build();
			}
		}
	}

	private JsonArray prepareTestingDataResult(Instances classifiedTestingData) {

		JsonArray instances = new JsonArray();
		
		for(int i = 0; i<=classifiedTestingData.numInstances()-1;i++){
			JsonObject instance = new JsonObject();
			for(int j = 0;j<=classifiedTestingData.numAttributes()-1; j++){
				if(classifiedTestingData.attribute(j).isNumeric()){
					instance.addProperty(classifiedTestingData.attribute(j).name(), classifiedTestingData.instance(i).value(j));
				}
				else if(classifiedTestingData.attribute(j).isNominal()){
					instance.addProperty(classifiedTestingData.attribute(j).name(), classifiedTestingData.instance(i).stringValue(j));
				}
			}
			instances.add(instance);
		}
		return instances;
	}

}
