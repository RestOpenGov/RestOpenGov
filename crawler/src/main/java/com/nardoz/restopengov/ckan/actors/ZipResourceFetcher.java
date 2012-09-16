package com.nardoz.restopengov.ckan.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.ckan.models.MetadataResource;
import com.nardoz.restopengov.ckan.utils.DatasetReader;
import com.nardoz.restopengov.ckan.utils.ElasticDatasetReaderResult;
import com.nardoz.restopengov.ckan.utils.IResourceFormatReader;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipResourceFetcher extends UntypedActor {

    private Client client;
    private ActorRef resourceFetcher;

    public ZipResourceFetcher(Client client) {
        this.client = client;
    }

    public void onReceive(Object message) {

        if(message instanceof MetadataResource) {

            final MetadataResource resource = (MetadataResource) message;

            String index = ConfigFactory.load().getString("restopengov.index");
            GetResponse response = client.prepareGet(index, resource.metadata_name, resource.id).execute().actionGet();

            if(response.getSource() != null) {
                String hash = (String) response.getSource().get("hash");

                if(resource.hash == hash) {
                    Crawler.logger.info(resource.name + " didn't change, not crawling");
                    return;
                }
            }

            try {
                URL url = new URL(resource.url.replace("https", "http"));
                InputStream stream = url.openStream();

                byte[] buf = new byte[1024];
                ZipInputStream zipinputstream = null;
                ZipEntry entry;
                zipinputstream = new ZipInputStream(stream);

                Integer id = 0;

                while((entry = zipinputstream.getNextEntry()) != null) {

                    id++;

                    Crawler.logger.info("Extracting: " + entry);

                    resource.id = resource.id + "-" + id;
                    resource.format = entry.getName().substring(entry.getName().lastIndexOf('.') + 1);

                    if(!DatasetReader.handles(resource.format)) {
                        continue;
                    }

                    FileOutputStream fos = new FileOutputStream("tmp/" + entry.getName());

                    int data;
                    while (0 < (data = zipinputstream.read(buf))){
                        fos.write(buf, 0, data);
                    }

                    fos.close();
                    zipinputstream.closeEntry();

                    Crawler.logger.info("Completed extraction for: " + entry);

                    ElasticDatasetReaderResult callback = new ElasticDatasetReaderResult(resource, client);

                    IResourceFormatReader datasetReader = DatasetReader.read(resource, callback);

                    if(datasetReader != null) {
                        datasetReader.readFromFile("tmp/" + entry.getName());
                    }

                }

                zipinputstream.close();

            } catch(Exception e) {
                e.printStackTrace();
            }

        } else {
            unhandled(message);
        }
    }
}
