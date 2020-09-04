package com.tanto;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ElasticSimple {
	static final Settings settings = Settings.builder().put("cluster.name", "cluster-ex")
			.put("client.transport.sniff", true).put("client.transport.ignore_cluster_name", false)
			.put("client.transport.ping_timeout", "30s").put("client.transport.nodes_sampler_interval", "5s").build();
	
	public static void main(String[] args) {
		TransportClient client = null;
		
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.OFF);
		
		try {
//			client = new TransportClient()
//					.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9201))
			
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9200));
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		String id = "idTanto";
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("first_name", "aryo");
		jsonMap.put("last_name", "bayu");
		jsonMap.put("address", "USA");
		
		IndexResponse response = client.prepareIndex("consumer", "_doc", id)
				.setSource(jsonMap, XContentType.JSON).get();
		System.out.println("OKE");
		
	}

}
