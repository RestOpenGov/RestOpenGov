package com.nardoz.restopengov.utils;

import com.nardoz.restopengov.models.MetadataResource;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

public class ElasticDatasetReaderResult implements IDatasetReaderResult {

    private String index;
    private Client client;
    private BulkRequestBuilder bulkRequest;
    private MetadataResource resource;

    public ElasticDatasetReaderResult(MetadataResource resource, Client client) {
        this.resource = resource;
        this.client = client;
        this.index = ConfigFactory.load().getString("restopengov.index");
    }

    @Override
    public void onStart() {
        this.bulkRequest = client.prepareBulk();
        System.out.println("***** Started: " + index + "/" + resource.metadata_name + "/" + resource.id + " *****");
    }

    @Override
    public void add(String id, String json) {
        bulkRequest.add(client.prepareIndex(index, resource.metadata_name, resource.id + "-" + id).setSource(json));
        //System.out.println("Adding: " + index + "/" + resource.metadata_name + "/" + resource.id + "-" + id);
    }

    @Override
    public void onEnd() {
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        if(bulkResponse.hasFailures()) {
            System.out.println(bulkResponse.buildFailureMessage());
        } else {
            System.out.println("***** Saved: " + index + "/" + resource.metadata_name + "/" + resource.id + " *****");
        }
    }

}
