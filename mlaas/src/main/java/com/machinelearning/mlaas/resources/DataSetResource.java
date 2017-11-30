package com.machinelearning.mlaas.resources;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.machinelearning.mlaas.classification.DataImport;

import weka.core.Instances;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/data")
public class DataSetResource {

	
	@Path("/upload")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDataSet(String dataSet) {
    	
    	JsonParser parser = new JsonParser();
    	JsonObject dataSetObject = parser.parse(dataSet).getAsJsonObject();
    	JsonElement attributeJsonArray = dataSetObject.get("attributes");
    	JsonElement type = dataSetObject.get("type");
    	JsonElement dataSetJsonArray = dataSetObject.get("data");
    	
    	Type listType = new TypeToken<List<String>>(){}.getType();
    	List<String> attributeList = new Gson().fromJson(attributeJsonArray, listType);
    	
    	DataImport dataImport = new DataImport();
    	
    	Instances data = dataImport.createDataSet(attributeList);
    	dataImport.addDataToDataSet(data, (JsonArray) dataSetJsonArray, type.getAsString());
    	return  Response.ok("{\"Response\":\"successful\"", MediaType.APPLICATION_JSON).build();
    }
}
