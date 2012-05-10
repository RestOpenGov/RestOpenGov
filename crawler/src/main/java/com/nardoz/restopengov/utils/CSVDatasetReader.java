package com.nardoz.restopengov.utils;

import au.com.bytecode.opencsv.CSVReader;
import com.glaforge.i18n.io.SmartEncodingInputStream;
import com.google.gson.Gson;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.models.MetadataResource;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class CSVDatasetReader implements IDatasetReader {

    private MetadataResource resource;
    private IDatasetReaderResult callback;
    private Gson gson = new Gson();
    private char separator = ',';

    public CSVDatasetReader(MetadataResource resource) {
        this(resource, new DatasetReaderResult());
    }

    public CSVDatasetReader(MetadataResource resource, IDatasetReaderResult callback) {
        this.resource = resource;
        this.callback = callback;
    }

    public IDatasetReaderResult readFromResourceURL() throws Exception {

        URL url = new URL(resource.url.replace("https", "http"));
        separator = detectSeparator(url.openStream());

        return read(url.openStream());
    }

    public IDatasetReaderResult readFromFile(String path) throws Exception {

        separator = detectSeparator(new FileInputStream(path));

        return read(new FileInputStream(path));
    }

    public IDatasetReaderResult read(InputStream stream) throws Exception {

        SmartEncodingInputStream smart = new SmartEncodingInputStream(stream);

        CSVReader reader = new CSVReader(new InputStreamReader(smart), separator);

        String[] keys = reader.readNext();
        String[] nextLine;


        callback.onStart();

        Integer id = 0;
        while ((nextLine = reader.readNext()) != null) {
            callback.add(id.toString(), buildJson(keys, nextLine));
            id++;
        }

        callback.onEnd();

        reader.close();

        return callback;
    }

    private char detectSeparator(InputStream stream) {

        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        String line = "";

        try {
            line = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return detectSeparator(line);
    }

    private char detectSeparator(String line) {

        Crawler.logger.debug("CSV header: " + line);

        String[] separators = new String[] { ",", ";", "\t", "|" };

        TreeMap tm = new TreeMap();
        for(String s : separators) {
            tm.put(line.split(Pattern.quote(s)).length, s);
        }

        char separator = tm.lastEntry().getValue().toString().charAt(0);

        Crawler.logger.debug("Detected separator: " + separator);

        return separator;
    }

    private String buildJson(String[] keys, String[] dataLine) throws Exception {

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