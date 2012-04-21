package com.nardoz.restopengov.utils;

import au.com.bytecode.opencsv.CSVReader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DatasetReader {

    public DatasetReaderResult read(String filename) {

        DatasetReaderResult result = new DatasetReaderResult();
        File file = new File(filename);

        try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);

            CSVReader reader = new CSVReader(new InputStreamReader(in));

            String[] nextLine;
            String[] keys = reader.readNext();

            while((nextLine = reader.readNext()) != null) {
                result.add(buildMap(keys, nextLine));
            }

            in.close();

        } catch(Exception e) {
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