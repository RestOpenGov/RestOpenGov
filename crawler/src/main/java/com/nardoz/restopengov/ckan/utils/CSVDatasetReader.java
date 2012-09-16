package com.nardoz.restopengov.ckan.utils;

import com.nardoz.restopengov.ckan.models.MetadataResource;
import com.nardoz.restopengov.utils.CSVFetcher;
import com.nardoz.restopengov.utils.ICSVFetcherResult;
import com.nardoz.restopengov.utils.IFormatReader;

public class CSVDatasetReader extends CSVFetcher implements IFormatReader, IResourceFormatReader {

    private MetadataResource resource;
    private ICSVFetcherResult callback;

    public CSVDatasetReader(MetadataResource resource) {
        this(resource, new DatasetReaderResult());
    }

    public CSVDatasetReader(MetadataResource resource, ICSVFetcherResult callback) {
        this.resource = resource;
        this.callback = callback;
    }

    public ICSVFetcherResult readFromResourceURL() throws Exception {
        return readFromURL(resource.url.replace("https", "http"));
    }

}