package com.nardoz.restopengov.utils;

import java.io.InputStream;

public interface IDatasetReader {
    public IDatasetReaderResult read() throws Exception;
    public IDatasetReaderResult read(InputStream stream) throws Exception;
}
