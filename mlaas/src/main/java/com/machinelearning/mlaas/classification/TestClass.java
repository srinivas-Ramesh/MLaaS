package com.machinelearning.mlaas.classification;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class TestClass {

	private static ArrayList<Attribute> attributeList = new ArrayList<>();
	public static void main(String[] args) {
//	
//		for(int i = 0; i < 3; i++) {
//			attributeList.add(new Attribute("Attribute"+i, i));
//		}
		
		
		 ArrayList<Attribute> atts = new ArrayList<Attribute>(2);
	        ArrayList<String> classVal = new ArrayList<String>();
	        classVal.add("A");
	        classVal.add("B");
	        atts.add(new Attribute("content",(ArrayList<String>)null));
	        atts.add(new Attribute("@@class@@",classVal));

	        Instances dataRaw = new Instances("TestInstances",atts,0);
	        System.out.println("Before adding any instance");
	        System.out.println("--------------------------");
	        System.out.println(dataRaw);
	        System.out.println("--------------------------");

	        double[] instanceValue1 = new double[dataRaw.numAttributes()];

	        instanceValue1[0] = dataRaw.attribute(0).addStringValue("This is a string!");
	        instanceValue1[1] = 0;

	        dataRaw.add(new DenseInstance(1.0, instanceValue1));

	        System.out.println("After adding a instance");
	        System.out.println("--------------------------");
	        System.out.println(dataRaw);
	        System.out.println("--------------------------");

	        double[] instanceValue2 = new double[dataRaw.numAttributes()];

	        instanceValue2[0] = dataRaw.attribute(0).addStringValue("This is second string!");
	        instanceValue2[1] = 1;

	        dataRaw.add(new DenseInstance(1.0, instanceValue2));

	        System.out.println("After adding second instance");
	        System.out.println("--------------------------");
	        System.out.println(dataRaw);
	        System.out.println("--------------------------");


	}

}
