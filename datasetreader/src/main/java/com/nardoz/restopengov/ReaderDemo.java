package com.nardoz.restopengov;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

import com.nardoz.restopengov.utils.DatasetReader;

public class ReaderDemo {

	public static void main(String[] args) {

		// Read the specified datasets
		DatasetReader dataset = new DatasetReader();
		String path = "src/main/resources/";
		

		List<String> events = dataset.read(
				path + "data/bafici-2010/bafici10-events.csv").getJSONList();
		List<String> countries = dataset.read(
				path + "data/bafici-2010/bafici10-countries.csv").getJSONList();
		List<String> obrasRegistradas = dataset.read(
				path + "data/obras-registradas/obras-registradas.csv")
				.getJSONList();

		// Elasticsearch magic
		Node node = nodeBuilder().client(true).node();
		Client client = node.client();

		BulkRequestBuilder bulkRequest = client.prepareBulk();

		for (int i = 0; i < events.size(); i++) {
			bulkRequest.add(client.prepareIndex("bafici-2010", "events",
					String.valueOf(i)).setSource(events.get(i)));
		}

		for (int i = 0; i < countries.size(); i++) {
			bulkRequest.add(client.prepareIndex("bafici-2010", "countries",
					String.valueOf(i)).setSource(countries.get(i)));
		}

		for (int i = 0; i < obrasRegistradas.size(); i++) {
			bulkRequest.add(client.prepareIndex("obras-registradas", "obras",
					String.valueOf(i)).setSource(obrasRegistradas.get(i)));
		}

		BulkResponse bulkResponse = bulkRequest.execute().actionGet();

		if (bulkResponse.hasFailures()) {
			System.out.println(bulkResponse.buildFailureMessage());
		}

		node.close();

	}
}