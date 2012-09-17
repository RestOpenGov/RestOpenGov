package com.nardoz.restopengov.ckan.utils;

import com.nardoz.restopengov.utils.ICSVFetcherResult;

import java.util.ArrayList;
import java.util.List;
public class DatasetReaderResult implements ICSVFetcherResult {

    private List<String> jsonList = new ArrayList<String>();

    public List<String> getJsonList() {
        return jsonList;
    }

    public void onStart() {

    }

    public void add(String id, String json) {
        jsonList.add(json);
    }

    public void onEnd() {

    }
}
