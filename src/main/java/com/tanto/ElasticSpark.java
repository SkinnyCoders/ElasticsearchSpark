package com.tanto;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanto.model.MyModel;

import avro.shaded.com.google.common.collect.ImmutableMap;

public class ElasticSpark implements scala.Serializable {
	private static final ObjectMapper objMapper = new ObjectMapper();
			
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("belajar").set("es.index.auto.create", "true")
				.set("spark.driver.allowMultipleContexts", "true").set("es.nodes", "localhost:9201")
				.setMaster("local[2]");
		
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		
		JavaRDD<String> rddData = sparkContext.textFile("/home/baskara/eclipse-workspace/ElasticLearn/sources/exampledata.csv")
				.map(new Function<String, String>() {

					@Override
					public String call(String data1) throws Exception {
						// TODO Auto-generated method stub
						
						String json = null;
						try {
							String[] data = data1.split(",");
							MyModel model = new MyModel(data[0], data[0], data[1], Integer.valueOf(data[2]), data[3],
									Integer.valueOf(data[4]), Integer.valueOf(data[5]), Integer.valueOf(data[6]), data[7],
									data[8], Integer.valueOf(data[9]), data[10], data[11]);
							json = objMapper.writeValueAsString(model);
						}catch(Exception e) {
							e.printStackTrace();
						}
						
						return json;
					}
				});
		
		try {
			System.out.println("Saving...");
			JavaEsSpark.saveJsonToEs(rddData, "belajar/_doc", ImmutableMap.of("es.mapping.id", "id"));
		}catch (Exception e) {
			e.getMessage();
		}
		
	}

}
