package com.nardoz.restopengov.utils;

import java.io.IOException;
import java.io.InputStream;

public interface IDatasetReader {
    public IDatasetReaderResult read() throws IOException;
    public IDatasetReaderResult read(InputStream stream) throws IOException;
}
