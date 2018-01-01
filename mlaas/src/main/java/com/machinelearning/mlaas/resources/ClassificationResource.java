package com.machinelearning.mlaas.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.JsonObject;
import com.machinelearning.mlaas.datamodel.DataModel;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

@Path("/classification")
public class ClassificationResource {

	@GET
	public Response getClassifierInfo() {

		if (DataModel.getClassificationTrainingDataSet() == null) {
			return Response.status(500).entity("Training Data Not Provided").build();
		}

		if (DataModel.getClassicationEvaluation() == null) {
			try {
				String[] options = new String[1];
				options[0] = "-U";
				J48 classifierTree = new J48();
				classifierTree.setOptions(options);
				Evaluation evaluation = new Evaluation(DataModel.getClassificationTrainingDataSet());
				evaluation.crossValidateModel(classifierTree, DataModel.getClassificationTrainingDataSet(), 10,
						new Random(1));
				DataModel.setClassicationEvaluation(evaluation);
				
			} catch (Exception e) {
				return Response.status(500).entity(e.getMessage()).build();
			}
		}
		
		JsonObject response = prepareClassifierResponse(DataModel.getClassicationEvaluation());
		return Response.ok(response.toString(),MediaType.APPLICATION_JSON).build();
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
					DataModel.setClassificationTrainingDataSet(data);
					// clear the evaluation for previous dataSet
					if (DataModel.getClassicationEvaluation() != null) {
						DataModel.setClassicationEvaluation(null);
					}
				} else if (dataSet.equalsIgnoreCase("testingSet")) {
					DataModel.setClassificationTestingDataSet(data);
				} else {
					return Response.status(404).entity("Not found!").build();
				}

			} catch (IOException e) {
				return Response.status(500).entity(e.getMessage()).build();
			}
			return Response.ok("Classification DataSet has been saved in the system", MediaType.TEXT_PLAIN).build();
		}
	}
	
	private JsonObject prepareClassifierResponse(Evaluation evaluation) {
		
		JsonObject response = new JsonObject();
		response.addProperty("correct instances classified", evaluation.correct());
		response.addProperty("incorrect instances", evaluation.incorrect());
		response.addProperty("percent correct classification", evaluation.pctCorrect());
		return response;
	}

}
