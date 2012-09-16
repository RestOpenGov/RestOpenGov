package com.nardoz.restopengov.standalone.models;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteFile {
    public URL url = null;
    public String type;
    public String id;
    public String format;

    public RemoteFile(String url) {

        try {
            this.url = new URL(url);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        type = this.url.getHost().replaceAll("\\.", "-");
        id = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));
        format = url.split("\\.")[url.lastIndexOf('.')];

    }
}
