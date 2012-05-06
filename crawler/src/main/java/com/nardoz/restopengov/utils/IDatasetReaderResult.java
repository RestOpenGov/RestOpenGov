package com.nardoz.restopengov.utils;

public interface IDatasetReaderResult {
    public void onStart();
    public void add(String id, String json);
    public void onEnd();
}
