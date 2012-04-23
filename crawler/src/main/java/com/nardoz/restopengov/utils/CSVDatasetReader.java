package com.nardoz.restopengov.utils;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CSVDatasetReader implements IDatasetReader {

    public DatasetReaderResult read(InputStream stream) {

        DatasetReaderResult result = new DatasetReaderResult();

        CSVReader reader = new CSVReader(new InputStreamReader(stream));

        try {
            String[] keys = reader.readNext();
            String[] nextLine;

            while((nextLine = reader.readNext()) != null) {
                result.add(buildMap(keys, nextLine));
            }

            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public DatasetReaderResult read(String filename) {

        File file = new File(filename);

        DatasetReaderResult result = null;

        try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream stream = new DataInputStream(fstream);

            result = read(stream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, String> buildMap(String[] keys, String[] dataLine) {

        Map<String, String> result = new HashMap<String, String>();

        for(int i = 0; i < dataLine.length; i++) {
            result.put(keys[i], dataLine[i]);
        }

        return result;
    }

}