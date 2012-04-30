package com.nardoz.restopengov.utils;

import com.nardoz.restopengov.models.MetadataResource;

import java.util.ArrayList;
import java.util.List;

public class DatasetReader {

    private static String[] handledFormats = new String[] {
        "csv"
    };

    public static IDatasetReader factory(MetadataResource resource, IDatasetReaderResult callback) throws Exception {

        if(resource.format.toLowerCase().matches("csv")) {
            return new CSVDatasetReader(resource, callback);
        } else {
            throw new Exception("DatasetReader: Unhandled format " + resource.format + " for file " + resource.url);
        }

    }

    public static boolean handles(String format) {

        boolean result = false;

        for(String fmt : handledFormats) {
            if(format == fmt) {
                result = true;
                break;
            }
        }

        return result;
    }

}