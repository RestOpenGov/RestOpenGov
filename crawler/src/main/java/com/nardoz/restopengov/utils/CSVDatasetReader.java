package com.nardoz.restopengov.utils;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.Gson;
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

    public IDatasetReaderResult read() {

        InputStream stream = null;

        try {

            URL url = new URL(resource.url.replace("https", "http"));
            stream = url.openStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return read(stream);
    }

    public IDatasetReaderResult read(InputStream stream) {

        CSVReader reader = new CSVReader(new InputStreamReader(stream));

        callback.onStart();

        try {
            String[] keys = reader.readNext();
            String[] nextLine;

            Integer id = 0;
            while ((nextLine = reader.readNext()) != null) {
                callback.add(id.toString(), buildJson(keys, nextLine));
                id++;
            }

            reader.close();
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        callback.onEnd();

        return callback;
    }

    public String buildJson(String[] keys, String[] dataLine) {

        Map<String, String> result = new HashMap<String, String>();

        for (int i = 0; i < dataLine.length; i++) {
            result.put(keys[i], dataLine[i]);
        }

        return gson.toJson(result);
    }

}