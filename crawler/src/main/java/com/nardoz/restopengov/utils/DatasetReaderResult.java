package com.nardoz.restopengov.utils;

import java.util.ArrayList;
import java.util.List;
public class DatasetReaderResult implements IDatasetReaderResult {

    private List<String> jsonList = new ArrayList<String>();

    public List<String> getJsonList() {
        return jsonList;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void add(String id, String json) {
        jsonList.add(json);
    }

    @Override
    public void onEnd() {

    }
}
