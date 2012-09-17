package com.nardoz.restopengov.ckan.utils;

import com.nardoz.restopengov.utils.ICSVFetcherResult;

public interface IResourceFormatReader {
    ICSVFetcherResult readFromResourceURL() throws Exception;
    ICSVFetcherResult readFromFile(String s) throws Exception;
}
