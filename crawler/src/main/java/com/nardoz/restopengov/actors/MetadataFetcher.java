package com.nardoz.restopengov.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.nardoz.restopengov.models.Metadata;
import com.nardoz.restopengov.models.MetadataResource;
import us.monoid.web.Resty;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetadataFetcher extends UntypedActor {

    private ActorRef metadataPersist;
    private ActorRef resourceFetcher;

    public MetadataFetcher(ActorRef metadataPersist, ActorRef resourceFetcher) {
        this.metadataPersist = metadataPersist;
        this.resourceFetcher = resourceFetcher;
    }

    public void onReceive(Object message) {

        if(message instanceof String) {

            String url = "http://data.buenosaires.gob.ar/api/rest/dataset/" + message;

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                Date result = null;

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                String date = json.getAsJsonPrimitive().getAsString();

                try {
                    result = format.parse(date);
                } catch (ParseException e) {
                    format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                    try {
                        result = format.parse(date);
                    } catch (ParseException e2) {
                        throw new RuntimeException(e2);
                    }
                }

                return result;
                }
            });

            Gson gson = builder.create();

            try {
                String response = new Resty().text(url).toString();

                Type metadataType = new TypeToken<Metadata>() {}.getType();
                Metadata metadata = gson.fromJson(response, metadataType);

                metadataPersist.tell(metadata, getSelf());

                for(MetadataResource resource : metadata.resources) {
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
