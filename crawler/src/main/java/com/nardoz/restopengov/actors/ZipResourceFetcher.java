package com.nardoz.restopengov.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.models.MetadataResource;
import com.nardoz.restopengov.utils.DatasetReader;
import com.nardoz.restopengov.utils.ElasticDatasetReaderResult;
import com.nardoz.restopengov.utils.IDatasetReader;
import org.elasticsearch.client.Client;

import java.io.FileInputStream;
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

        if (message instanceof MetadataResource) {

            final MetadataResource resource = (MetadataResource) message;

            try {
                URL url = new URL(resource.url.replace("https", "http"));
                InputStream stream = url.openStream();

                byte[] buf = new byte[1024];
                ZipInputStream zipinputstream = null;
                ZipEntry entry;
                zipinputstream = new ZipInputStream(stream);

                while((entry = zipinputstream.getNextEntry()) != null) {
                    Crawler.logger.info("Extracting: " + entry);

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

                    IDatasetReader datasetReader = DatasetReader.factory(resource, callback);

                    if(datasetReader != null) {
                        datasetReader.read(new FileInputStream("tmp/" + entry.getName()));
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
