package com.nardoz.restopengov.utils;

import com.nardoz.restopengov.Crawler;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

public class ElasticIndexer implements ICSVFetcherResult {

    protected String index;
    protected String type;
    protected String id;
    protected Integer maxPerBulk;
    protected Integer itemCounter = 0;
    protected Client client;
    protected BulkRequestBuilder bulkRequest;

    public ElasticIndexer(Client client, String type, String id) {
        this.client = client;
        this.index = ConfigFactory.load().getString("restopengov.index");
        this.type = type;
        this.id = id;
        this.maxPerBulk = ConfigFactory.load().getInt("restopengov.max-per-bulk");
    }

    public void onStart() {
        itemCounter = 0;
        bulkRequest = client.prepareBulk();
        Crawler.logger.info("***** Started: " + index + "/" + type + "/" + id + "-[..] *****");
    }

    public void add(String uuid, String json) {

        if(itemCounter.equals(maxPerBulk)) {
            onEnd();
            onStart();
        }

        String resourceId = id + "-" + uuid;

        bulkRequest.add(client.prepareIndex(index, type, resourceId).setSource(json));
        itemCounter++;

        Crawler.logger.debug("Adding item #" + itemCounter + ": " + index + "/" + type + "/" + resourceId);
    }

    public void onEnd() {
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        if(bulkResponse.hasFailures()) {
            Crawler.logger.error("Elasticsearch Failure: " + bulkResponse.buildFailureMessage());
        } else {
            Crawler.logger.info("***** " + itemCounter + " items saved in: " + index + "/" + type + "/" + id + "-[..] *****");
        }
    }

}
