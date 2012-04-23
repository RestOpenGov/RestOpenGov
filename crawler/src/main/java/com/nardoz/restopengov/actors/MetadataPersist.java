package com.nardoz.restopengov.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.nardoz.restopengov.models.Metadata;
import com.nardoz.restopengov.models.MetadataResource;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.node.Node;

public class MetadataPersist extends UntypedActor {

    private Node node;

    public MetadataPersist(Node node) {
        this.node = node;
    }

    public void onReceive(Object message) {

        if(message instanceof Metadata) {

            Metadata metadata = (Metadata) message;
            String json = new Gson().toJson(metadata);

            BulkRequestBuilder bulkRequest = node.client().prepareBulk();
            bulkRequest.add(node.client().prepareIndex("gcba", "metadata", metadata.name).setSource(json));

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();

            if(bulkResponse.hasFailures()) {
                System.out.println(bulkResponse.buildFailureMessage());
            }

        } else {
            unhandled(message);
        }
    }
}
