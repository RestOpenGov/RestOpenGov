package com.nardoz.restopengov.utils;

import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.models.MetadataResource;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

import java.net.URL;

public class ElasticDatasetReaderResult implements IDatasetReaderResult {

    private String index;
    private Integer maxPerBulk;
    private Integer itemCounter = 0;
    private Client client;
    private BulkRequestBuilder bulkRequest;
    private MetadataResource resource;

    public ElasticDatasetReaderResult(MetadataResource resource, Client client) {
        this.resource = resource;
        this.client = client;
        this.index = ConfigFactory.load().getString("restopengov.index");
        this.maxPerBulk = ConfigFactory.load().getInt("restopengov.max-per-bulk");
    }

    @Override
    public void onStart() {
        itemCounter = 0;
        bulkRequest = client.prepareBulk();
        Crawler.logger.info("***** Started: " + index + "/" + resource.metadata_name + "/" + resource.id + "-[..] *****");
    }

    @Override
    public void add(String id, String json) {

        if(itemCounter.equals(maxPerBulk)) {
            onEnd();
            onStart();
        }

        String resourceId = resource.id + "-" + id;

        try {
            URL url = new URL(resource.url);
            resourceId = url.getFile().split("\\.")[0] + "-" + id;

        } catch (Exception e) {

        }

        bulkRequest.add(client.prepareIndex(index, resource.metadata_name, resourceId).setSource(json));
        itemCounter++;

        Crawler.logger.debug("Adding item #" + itemCounter + ": " + index + "/" + resource.metadata_name + "/" + resourceId);
    }

    @Override
    public void onEnd() {
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        if(bulkResponse.hasFailures()) {
            Crawler.logger.error("Elasticsearch Failure: " + bulkResponse.buildFailureMessage());
        } else {
            Crawler.logger.info("***** " + itemCounter + " items saved in: " + index + "/" + resource.metadata_name + "/" + resource.id + "-[..] *****");
        }
    }

}
