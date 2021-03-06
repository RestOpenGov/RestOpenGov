package com.nardoz.restopengov.ckan.actors;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.ckan.models.Metadata;
import com.nardoz.restopengov.utils.DateChecker;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;

public class MetadataPersist extends UntypedActor {

    private Client client;

    public MetadataPersist(Client client) {
        this.client = client;
    }

    public void onReceive(Object message) {

        if (message instanceof Metadata) {

            Metadata metadata = (Metadata) message;

            String index = ConfigFactory.load().getString("restopengov.index");

            GetResponse response = client.prepareGet(index, "metadata", metadata.name).execute().actionGet();

            String modified = null;

            if(response.getSource() != null) {
                modified = (String) response.getSource().get("metadata_modified");

                if(DateChecker.compare(metadata.metadata_modified, modified) <= 0) {
                    Crawler.logger.info("Metadata for " + metadata.name + " didn't change, not crawling");
                    return;
                }
            }

            String json = new Gson().toJson(metadata);

            BulkRequestBuilder bulkRequest = client.prepareBulk();
            bulkRequest.add(client.prepareIndex(index, "metadata", metadata.name).setSource(json));

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();

            if(bulkResponse.hasFailures()) {
                Crawler.logger.error("Elastic Search Failure: " + bulkResponse.buildFailureMessage());
            }

        } else {
            unhandled(message);
        }
    }
}
