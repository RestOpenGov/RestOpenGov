package com.nardoz.restopengov.utils;

import java.io.InputStream;

public interface IDatasetReader {
    public IDatasetReaderResult read();
    public IDatasetReaderResult read(InputStream stream);
    public IDatasetReaderResult read(String filename);
}
