package com.nardoz.restopengov.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import us.monoid.web.Resty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatasetListFetcher extends UntypedActor {

    private ActorRef metadataFetcher;

    public DatasetListFetcher(ActorRef metadataFetcher) {
        this.metadataFetcher = metadataFetcher;
    }

    public static class Fetch {}

    public void onReceive(Object message) {

        if(message instanceof Fetch) {

            final String url = "http://data.buenosaires.gob.ar/api/rest/dataset/";

            try {
                String response = new Resty().text(url).toString();

                Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> datasetList = new Gson().fromJson(response, listType);

                for(String dataset : datasetList) {
                    metadataFetcher.tell(dataset, getSelf());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            getContext().stop(getSelf());

        } else {
            unhandled(message);
        }
    }
}
