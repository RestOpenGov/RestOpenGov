package com.nardoz.restopengov.standalone.utils;

import com.nardoz.restopengov.standalone.models.RemoteFile;
import com.nardoz.restopengov.utils.CSVFetcher;
import com.nardoz.restopengov.utils.ICSVFetcherResult;
import com.nardoz.restopengov.utils.IFormatReader;

public class FileReader {

    private static String[] handledFormats = new String[] {
        "csv"
    };

    public static IFormatReader read(RemoteFile file, ICSVFetcherResult callback) throws Exception {

        if(file.format.toLowerCase().matches("csv")) {
            return new CSVFetcher(callback);
        } else {
            throw new Exception("FileReader: Unhandled format " + file.format + " for file " + file.url);
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
