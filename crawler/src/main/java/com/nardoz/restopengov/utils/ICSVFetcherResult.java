package com.nardoz.restopengov.utils;

public interface ICSVFetcherResult {
    public void onStart();
    public void add(String id, String json);
    public void onEnd();
}
