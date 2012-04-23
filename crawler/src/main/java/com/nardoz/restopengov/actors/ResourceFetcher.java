package com.nardoz.restopengov.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.nardoz.restopengov.models.Metadata;
import com.nardoz.restopengov.models.MetadataResource;
import com.nardoz.restopengov.utils.DatasetReader;
import com.nardoz.restopengov.utils.IDatasetReader;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.node.Node;
import us.monoid.web.Resty;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ResourceFetcher extends UntypedActor {

    private Node node;

    public ResourceFetcher(Node node) {
        this.node = node;
    }

    public void onReceive(Object message) {

        if(message instanceof MetadataResource) {

            MetadataResource resource = (MetadataResource) message;

            List<String> result = null;

            try {
                URL url = new URL(resource.url.replace("https", "http"));
                InputStream stream = url.openStream();

                IDatasetReader datasetReader = DatasetReader.factory(resource);

                if(datasetReader != null) {
                    result = datasetReader.read(stream).getJSONList();
                }

            } catch(Exception e) {

            }

            if(result != null) {
                BulkRequestBuilder bulkRequest = node.client().prepareBulk();


                for(int i = 0; i < result.size(); i++) {

                    String id = resource.id + "-" + i;

                    System.out.println("Saving: gcba/" + resource.metadata_name + "/" + id);
                    bulkRequest.add(node.client().prepareIndex("gcba", resource.metadata_name, id).setSource(result.get(i)));
                }

                BulkResponse bulkResponse = bulkRequest.execute().actionGet();

                if(bulkResponse.hasFailures()) {
                    System.out.println(bulkResponse.buildFailureMessage());
                }
            }

        } else {
            unhandled(message);
        }
    }
}
