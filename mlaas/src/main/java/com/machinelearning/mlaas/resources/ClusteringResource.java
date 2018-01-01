package com.machinelearning.mlaas.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.JsonObject;
import com.machinelearning.mlaas.datamodel.DataModel;
import com.machinelearning.mlass.clustering.Clustering;

import weka.core.Instances;

@Path("/clustering")
public class ClusteringResource {

	@GET
	public Response getResults(@HeaderParam("cluster-count") Integer count) {

		Clustering clustering = new Clustering();

		if (count == null) {
			return Response.status(400).entity("No cluster Count Provided").build();
		}
		if (DataModel.getClusteringDataSet() == null) {
			return Response.status(409).entity("No DataSet provided!").build();
		}
		if (DataModel.getkMeans() == null
				|| (DataModel.getkMeans() != null && count.intValue() != DataModel.getkMeans().getNumClusters())) {
			clustering.xMeancluster(DataModel.getClusteringDataSet(), count.intValue());
		}

		try {
			JsonObject result = clustering.buildClusterResult(DataModel.getkMeans(), DataModel.getClusteringDataSet());
			return Response.ok(result.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.serverError().entity(e).build();
		}
	}

	@Path("/upload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		if (uploadedInputStream == null) {
			return Response.status(400).entity("File not provided").build();
		} else {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(uploadedInputStream));
				Instances data = new Instances(reader);
				DataModel.setClusteringDataSet(data);
				
				//clear the k-means calculation for previous dataSet
				if(DataModel.getkMeans() !=null) {
					DataModel.setkMeans(null);
				}
			} catch (IOException e) {
				return Response.status(500).entity(e.getMessage()).build();
			}
			return Response.ok("Clustering DataSet has been saved in the system", MediaType.TEXT_PLAIN).build();
		}
	}
}
