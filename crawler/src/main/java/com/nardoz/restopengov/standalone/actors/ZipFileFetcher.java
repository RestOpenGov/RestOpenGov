package com.nardoz.restopengov.standalone.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.ckan.models.MetadataResource;
import com.nardoz.restopengov.ckan.utils.DatasetReader;
import com.nardoz.restopengov.ckan.utils.ElasticDatasetReaderResult;
import com.nardoz.restopengov.ckan.utils.IResourceFormatReader;
import com.nardoz.restopengov.standalone.models.RemoteFile;
import com.nardoz.restopengov.standalone.utils.FileReader;
import com.nardoz.restopengov.utils.ElasticIndexer;
import com.nardoz.restopengov.utils.ICSVFetcherResult;
import com.nardoz.restopengov.utils.IFormatReader;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFileFetcher extends UntypedActor {

        private Client client;
        private ActorRef resourceFetcher;

        public ZipFileFetcher(Client client) {
            this.client = client;
        }

        public void onReceive(Object message) {

            if(message instanceof RemoteFile) {

                RemoteFile file = (RemoteFile) message;

                try {
                    URL url = new URL(file.url.toString().replace("https", "http"));
                    InputStream stream = url.openStream();

                    byte[] buf = new byte[1024];
                    ZipInputStream zipinputstream = null;
                    ZipEntry entry;
                    zipinputstream = new ZipInputStream(stream);

                    Integer id = 0;

                    while((entry = zipinputstream.getNextEntry()) != null) {

                        id++;

                        Crawler.logger.info("Extracting: " + entry);

                        file.id = file.id + "-" + id;
                        file.format = entry.getName().substring(entry.getName().lastIndexOf('.') + 1);

                        if(!DatasetReader.handles(file.format)) {
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

                        ICSVFetcherResult callback = new ElasticIndexer(client, file.type, file.id);
                        IFormatReader fileReader = FileReader.read(file, callback);

                        if(fileReader != null) {
                            fileReader.readFromFile("tmp/" + entry.getName());
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

}
