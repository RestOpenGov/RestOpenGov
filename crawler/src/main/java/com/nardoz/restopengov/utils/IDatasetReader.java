package com.nardoz.restopengov.utils;

import java.io.InputStream;

public interface IDatasetReader {
    public DatasetReaderResult read(InputStream stream);
    public DatasetReaderResult read(String filename);
}
