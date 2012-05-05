package com.nardoz.restopengov.utils;

import java.io.InputStream;

public interface IDatasetReader {
    public IDatasetReaderResult readFromResourceURL() throws Exception;
    public IDatasetReaderResult readFromFile(String path) throws Exception;
    public IDatasetReaderResult read(InputStream stream) throws Exception;
}
