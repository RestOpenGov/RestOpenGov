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

        String[] tmp = url.split("\\.");

        format = tmp[tmp.length - 1];
        id = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));

        if(url.contains("file://")) {
            type = id;
        } else {
            type = this.url.getHost().replaceAll("\\.", "-");
        }

    }
}
