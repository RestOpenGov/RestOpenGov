package com.nardoz.restopengov.utils;

import com.nardoz.restopengov.models.MetadataResource;

public class DatasetReader {

    public static IDatasetReader factory(MetadataResource resource) throws Exception {

        if(resource.format.equals("CSV")) {
            return new CSVDatasetReader();
        } else {
            throw new Exception("DatasetReader: Unhandled format");
        }

    }

}