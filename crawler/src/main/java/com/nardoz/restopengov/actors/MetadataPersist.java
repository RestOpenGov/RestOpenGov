package com.nardoz.restopengov.actors;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.models.Metadata;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

public class MetadataPersist extends UntypedActor {

    private Client client;

    public MetadataPersist(Client client) {
        this.client = client;
    }

    public void onReceive(Object message) {

        if (message instanceof Metadata) {

            Metadata metadata = (Metadata) message;
            String json = new Gson().toJson(metadata);

            String index = ConfigFactory.load().getString("restopengov.index");

            BulkRequestBuilder bulkRequest = client.prepareBulk();
            bulkRequest.add(client.prepareIndex(index, "metadata", metadata.name).setSource(json));

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();

            if (bulkResponse.hasFailures()) {
                Crawler.logger.error("Elastic Search Failure: " + bulkResponse.buildFailureMessage());
            }

        } else {
            unhandled(message);
        }
    }
}
