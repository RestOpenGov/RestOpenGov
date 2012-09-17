package com.nardoz.restopengov.ckan.utils;

import com.nardoz.restopengov.ckan.models.MetadataResource;
import com.nardoz.restopengov.utils.ICSVFetcherResult;

public class DatasetReader {

    private static String[] handledFormats = new String[] {
        "csv"
    };

    public static IResourceFormatReader read(MetadataResource resource, ICSVFetcherResult callback) throws Exception {

        if(resource.format.toLowerCase().matches("csv")) {
            return new CSVDatasetReader(resource, callback);
        } else {
            throw new Exception("DatasetReader: Unhandled format " + resource.format + " for file " + resource.url);
        }

    }

    public static boolean handles(String format) {

        boolean result = false;

        for(String fmt : handledFormats) {
            if(format.equals(fmt)) {
                result = true;
                break;
            }
        }

        return result;
    }

}