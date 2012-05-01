package com.nardoz.restopengov.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.nardoz.restopengov.models.Metadata;
import com.nardoz.restopengov.models.MetadataResource;
import com.nardoz.restopengov.utils.DateChecker;
import com.typesafe.config.ConfigFactory;
import us.monoid.web.Resty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MetadataFetcher extends UntypedActor {

    private ActorRef metadataPersist;
    private ActorRef resourceFetcher;
    private ActorRef zipResourceFetcher;

    public MetadataFetcher(ActorRef metadataPersist, ActorRef resourceFetcher, ActorRef zipResourceFetcher) {
        this.metadataPersist = metadataPersist;
        this.resourceFetcher = resourceFetcher;
        this.zipResourceFetcher = zipResourceFetcher;
    }

    public void onReceive(Object message) {

        if(message instanceof String) {

            String url = ConfigFactory.load().getString("restopengov.dataset-list") + message;

            try {
                String response = new Resty().text(url).toString();
                Metadata metadata = new Gson().fromJson(response, Metadata.class);

                metadataPersist.tell(metadata, getSelf());

                for(MetadataResource resource : metadata.resources) {
                    resource.metadata_name = metadata.name;

                    if(resource.format.toLowerCase().equals("zip")) {
                        zipResourceFetcher.tell(resource, getSelf());
                    } else {
                        resourceFetcher.tell(resource, getSelf());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            unhandled(message);
        }
    }
}
