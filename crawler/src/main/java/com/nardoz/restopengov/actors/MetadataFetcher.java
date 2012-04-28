package com.nardoz.restopengov.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nardoz.restopengov.models.Metadata;
import com.nardoz.restopengov.models.MetadataResource;
import com.typesafe.config.ConfigFactory;
import us.monoid.web.Resty;

import java.lang.reflect.Type;

public class MetadataFetcher extends UntypedActor {

    private ActorRef metadataPersist;
    private ActorRef resourceFetcher;

    public MetadataFetcher(ActorRef metadataPersist, ActorRef resourceFetcher) {
        this.metadataPersist = metadataPersist;
        this.resourceFetcher = resourceFetcher;
    }

    public void onReceive(Object message) {

        if (message instanceof String) {

            String url = ConfigFactory.load().getString("restopengov.dataset-list") + message;

            try {
                String response = new Resty().text(url).toString();

                Type metadataType = new TypeToken<Metadata>() {}.getType();
                Metadata metadata = new Gson().fromJson(response, metadataType);

                metadataPersist.tell(metadata, getSelf());

                for (MetadataResource resource : metadata.resources) {
                    resource.metadata_name = metadata.name;
                    resourceFetcher.tell(resource, getSelf());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            unhandled(message);
        }
    }
}
