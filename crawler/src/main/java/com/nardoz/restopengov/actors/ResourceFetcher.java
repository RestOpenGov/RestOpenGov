package com.nardoz.restopengov.actors;

import akka.actor.UntypedActor;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.models.MetadataResource;
import com.nardoz.restopengov.utils.DatasetReader;
import com.nardoz.restopengov.utils.ElasticDatasetReaderResult;
import com.nardoz.restopengov.utils.IDatasetReader;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;

public class ResourceFetcher extends UntypedActor {

    private Client client;

    public ResourceFetcher(Client client) {
        this.client = client;
    }

    public void onReceive(Object message) {

        if (message instanceof MetadataResource) {

            final MetadataResource resource = (MetadataResource) message;

            String index = ConfigFactory.load().getString("restopengov.index");
            GetResponse response = client.prepareGet(index, resource.metadata_name, resource.id).execute().actionGet();

            if(response.getSource() != null) {
                String hash = (String) response.getSource().get("hash");

                if(resource.hash == hash) {
                    Crawler.logger.info(resource.name + " didn't change, not crawling");
                    return;
                }
            }

            ElasticDatasetReaderResult callback = new ElasticDatasetReaderResult(resource, client);

            try {

                IDatasetReader datasetReader = DatasetReader.factory(resource, callback);

                if(datasetReader != null) {
                    datasetReader.read();
                }

            } catch (Exception e) {
                Crawler.logger.error(e.getMessage());
                e.printStackTrace();
            }

        } else {
            unhandled(message);
        }
    }
}
