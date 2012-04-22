package com.nardoz.restopengov.actors;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.nardoz.restopengov.models.Metadata;
import com.nardoz.restopengov.models.MetadataResource;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class Persist extends UntypedActor {

    private Node node;
    private Client client;

    public Persist() {
        node = nodeBuilder().client(true).node();
        client = node.client();
    }

    public void onReceive(Object message) {

        if(message instanceof Metadata) {

            Metadata metadata = (Metadata) message;
            String json = new Gson().toJson(metadata);

            System.out.println(json);

            BulkRequestBuilder bulkRequest = client.prepareBulk();
            bulkRequest.add(client.prepareIndex("gcba", "metadata", metadata.name).setSource(json));

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();

            if(bulkResponse.hasFailures()) {
                System.out.println(bulkResponse.buildFailureMessage());
            }

            for(MetadataResource resource : metadata.resources) {
                getSelf().tell(resource);
            }

        } else if(message instanceof MetadataResource) {

            MetadataResource resource = (MetadataResource) message;
            String json = new Gson().toJson(resource);

            System.out.println(json);

            BulkRequestBuilder bulkRequest = client.prepareBulk();
            bulkRequest.add(client.prepareIndex("gcba", "metadata", resource.name).setSource(json));

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();

            if(bulkResponse.hasFailures()) {
                System.out.println(bulkResponse.buildFailureMessage());
            }

        } else {
            unhandled(message);
        }
    }
}
