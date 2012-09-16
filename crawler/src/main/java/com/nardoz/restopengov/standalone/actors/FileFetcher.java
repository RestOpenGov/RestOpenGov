package com.nardoz.restopengov.standalone.actors;

import akka.actor.UntypedActor;
import com.nardoz.restopengov.Crawler;
import com.nardoz.restopengov.standalone.models.RemoteFile;
import com.nardoz.restopengov.standalone.utils.FileReader;
import com.nardoz.restopengov.utils.ElasticIndexer;
import com.nardoz.restopengov.utils.ICSVFetcherResult;
import com.nardoz.restopengov.utils.IFormatReader;
import org.elasticsearch.client.Client;

public class FileFetcher extends UntypedActor {

    private Client client;

    public FileFetcher(Client client) {
        this.client = client;
    }

    public void onReceive(Object message) {

        if(message instanceof RemoteFile) {

            RemoteFile file = (RemoteFile) message;

            ICSVFetcherResult callback = new ElasticIndexer(client, file.type, file.id);

            try {

                IFormatReader fileReader = FileReader.read(file, callback);

                if(fileReader != null) {
                    fileReader.readFromURL(file.url.toString());
                }

            } catch (Exception e) {
                Crawler.logger.error(e.getMessage());
                e.printStackTrace();
            }

        }
    }

}
