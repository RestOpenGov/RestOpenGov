package com.nardoz.restopengov.utils;

import com.nardoz.restopengov.models.MetadataResource;

public class DatasetReader {

    public static IDatasetReader factory(MetadataResource resource, IDatasetReaderResult callback) throws Exception {

        if (resource.format.toLowerCase().matches("csv")) {
            return new CSVDatasetReader(resource, callback);
        } else {
            throw new Exception("DatasetReader: Unhandled format");
        }

    }

}