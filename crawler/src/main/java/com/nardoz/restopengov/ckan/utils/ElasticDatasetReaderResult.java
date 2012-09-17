package com.nardoz.restopengov.ckan.utils;

import com.nardoz.restopengov.ckan.models.MetadataResource;
import com.nardoz.restopengov.utils.ElasticIndexer;
import org.elasticsearch.client.Client;

public class ElasticDatasetReaderResult extends ElasticIndexer {

    public ElasticDatasetReaderResult(MetadataResource resource, Client client) {
        super(client, resource.metadata_name, resource.url.substring(resource.url.lastIndexOf('/') + 1, resource.url.lastIndexOf('.')));
    }

}
