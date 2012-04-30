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

    public IDatasetReaderResult read() throws IOException {

        URL url = new URL(resource.url.replace("https", "http"));
        InputStream stream = url.openStream();

        return read(stream);
    }

    public IDatasetReaderResult read(InputStream stream) throws IOException {

        CSVReader reader = new CSVReader(new InputStreamReader(stream), detectSeparator(stream));

        callback.onStart();

        String[] keys = reader.readNext();
        String[] nextLine;

        Integer id = 0;

        while ((nextLine = reader.readNext()) != null) {
            callback.add(id.toString(), buildJson(keys, nextLine));
            id++;
        }

        reader.close();
        stream.close();

        callback.onEnd();

        return callback;
    }

    private char detectSeparator(InputStream stream) {

        BufferedReader isr = new BufferedReader(new InputStreamReader(stream));

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

    public String buildJson(String[] keys, String[] dataLine) {

        Map<String, String> result = new HashMap<String, String>();

        for (int i = 0; i < dataLine.length; i++) {
            result.put(keys[i], dataLine[i]);
        }

        return gson.toJson(result);
    }

}