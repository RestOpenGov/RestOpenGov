package com.nardoz.restopengov.utils;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.Gson;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.models.MetadataResource;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CSVDatasetReader implements IDatasetReader {

    private MetadataResource resource;
    private IDatasetReaderResult callback;
    private Gson gson = new Gson();

    public CSVDatasetReader(MetadataResource resource) {
        this(resource, new DatasetReaderResult());
    }

    public CSVDatasetReader(MetadataResource resource, IDatasetReaderResult callback) {
        this.resource = resource;
        this.callback = callback;
    }

    public IDatasetReaderResult read() throws Exception {

        URL url = new URL(resource.url.replace("https", "http"));
        InputStream stream = url.openStream();

        return read(stream);
    }

    public IDatasetReaderResult read(InputStream stream) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        CSVReader reader = new CSVReader(br, detectSeparator(br));

        String[] keys = reader.readNext();
        String[] nextLine;

        Integer id = 0;

        callback.onStart();

        while ((nextLine = reader.readNext()) != null) {
            callback.add(id.toString(), buildJson(keys, nextLine));
            id++;
        }

        callback.onEnd();

        reader.close();
        stream.close();

        return callback;
    }

    private char detectSeparator(BufferedReader isr) {

        char separator = ',';

        try {
            String line = isr.readLine();

            Integer len  = line.split(",").length;
            Integer len2 = line.split(";").length;
            Integer len3 = line.split("\\t").length;

            if(len2 > len && len2 > len3) {
                separator = ';';
            }

            if(len3 > len2 && len3 > len) {
                separator = '\t';
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Crawler.logger.debug("Detected separator: " + separator);

        return separator;
    }

    public String buildJson(String[] keys, String[] dataLine) throws Exception {

        if(keys.length != dataLine.length) {
            throw new Exception("There are not as much columns for the keys as for the rows");
        }

        Map<String, String> result = new HashMap<String, String>();

        for (int i = 0; i < dataLine.length; i++) {
            result.put(keys[i], dataLine[i]);
        }

        return gson.toJson(result);
    }

}