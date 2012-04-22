package com.nardoz.restopengov.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Metadata implements Serializable {
    public String mantainer;
    public String id;
    public Date metadata_created;
    public List<String> relationships;
    public String license;
    public Date metadata_modified;
    public String author;
    public String author_email;
    public String download_url;
    public String state;
    public String version;
    public String licence_id;
    public List<MetadataResource> resources;
    public List<String> tags;
    public List<String> groups;
    public String name;
    public String isopen;
    public String notes_rendered;
    public String url;
    public String ckan_url;
    public String notes;
    public String title;
    public String ratings_average;
    public Map<String,String> extras;
    public String ratings_count;
    public String revision_id;
}
