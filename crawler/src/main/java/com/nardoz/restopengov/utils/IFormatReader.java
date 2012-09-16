package com.nardoz.restopengov.utils;

import java.io.InputStream;

public interface IFormatReader {
    public ICSVFetcherResult readFromURL(String url) throws Exception;
    public ICSVFetcherResult readFromFile(String path) throws Exception;
    public ICSVFetcherResult read(InputStream stream) throws Exception;
}
