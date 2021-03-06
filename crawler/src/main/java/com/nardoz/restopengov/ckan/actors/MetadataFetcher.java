package com.nardoz.restopengov.ckan.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.nardoz.restopengov.ckan.models.Metadata;
import com.nardoz.restopengov.ckan.models.MetadataResource;
import us.monoid.web.Resty;

public class MetadataFetcher extends UntypedActor {

    public void onReceive(Object message) {

        if(message instanceof String) {

            ActorRef metadataPersist = getContext().actorFor("/user/ckanMetadataPersist");
            ActorRef resourceFetcher = getContext().actorFor("/user/ckanResourceFetcher");
            ActorRef zipResourceFetcher = getContext().actorFor("/user/ckanZipResourceFetcher");

            String url = (String) message;

            try {
                String response = new Resty().text(url).toString();
                Metadata metadata = new Gson().fromJson(response, Metadata.class);

                metadataPersist.tell(metadata, getSelf());

                for(MetadataResource resource : metadata.resources) {
                    resource.metadata_name = metadata.name;

                    if(metadata.extras != null) {
                        if(metadata.extras.containsKey("encoding")) {
                            resource.encoding = metadata.extras.get("encoding");
                        }
                    }

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
