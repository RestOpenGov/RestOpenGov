package com.nardoz.restopengov.utils;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.Gson;
import com.nardoz.restopengov.models.MetadataResource;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CSVDatasetReader implements IDatasetReader {

    private IDatasetReaderResult callback;
    private InputStream stream;
    private Gson gson = new Gson();

    public CSVDatasetReader(MetadataResource resource) {
        this(resource, new DatasetReaderResult());
    }

    public CSVDatasetReader(MetadataResource resource, IDatasetReaderResult callback) {

        try {

            URL url = new URL(resource.url.replace("https", "http"));
            stream = url.openStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.callback = callback;

    }

    public IDatasetReaderResult read() {
        return read(this.stream);
    }

    public IDatasetReaderResult read(InputStream stream) {

        CSVReader reader = new CSVReader(new InputStreamReader(stream));

        callback.onStart();

        try {
            String[] keys = reader.readNext();
            String[] nextLine;

            Integer i = 0;
            while ((nextLine = reader.readNext()) != null) {
                callback.add(i.toString(), buildJson(keys, nextLine));
                i++;
            }

            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        callback.onEnd();

        return callback;
    }

    public IDatasetReaderResult read(String filename) {

        File file = new File(filename);

        IDatasetReaderResult result = null;

        try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream stream  = new DataInputStream(fstream);

            result = read(stream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String buildJson(String[] keys, String[] dataLine) {

        Map<String, String> result = new HashMap<String, String>();

        for (int i = 0; i < dataLine.length; i++) {
            result.put(keys[i], dataLine[i]);
        }

        return gson.toJson(result);
    }

}