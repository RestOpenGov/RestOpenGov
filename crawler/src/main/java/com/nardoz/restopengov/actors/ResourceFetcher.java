package com.nardoz.restopengov.actors;

import akka.actor.UntypedActor;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.models.MetadataResource;
import com.nardoz.restopengov.utils.DatasetReader;
import com.nardoz.restopengov.utils.ElasticDatasetReaderResult;
import com.nardoz.restopengov.utils.IDatasetReader;
import org.elasticsearch.client.Client;

public class ResourceFetcher extends UntypedActor {

    private Client client;

    public ResourceFetcher(Client client) {
        this.client = client;
    }

    public void onReceive(Object message) {

        if (message instanceof MetadataResource) {

            final MetadataResource resource = (MetadataResource) message;

            ElasticDatasetReaderResult callback = new ElasticDatasetReaderResult(resource, client);

            try {

                IDatasetReader datasetReader = DatasetReader.factory(resource, callback);

                if (datasetReader != null) {
                    datasetReader.read();
                }

            } catch (Exception e) {
                Crawler.logger.error("Format error: " + e.getMessage());
            }

        } else {
            unhandled(message);
        }
    }
}
