package com.nardoz.restopengov.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typesafe.config.ConfigFactory;
import us.monoid.web.Resty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatasetListFetcher extends UntypedActor {

    private ActorRef metadataFetcher;

    public DatasetListFetcher(ActorRef metadataFetcher) {
        this.metadataFetcher = metadataFetcher;
    }

    public static class FetchAll {}
    public static class ListAll {}

    public void onReceive(Object message) {

        if(message instanceof ListAll) {

            final String url = ConfigFactory.load().getString("restopengov.dataset-list");

            try {
                String response = new Resty().text(url).toString();

                Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> datasetList = new Gson().fromJson(response, listType);

                for(String dataset : datasetList) {
                    System.out.println(dataset);
                }

                getContext().system().shutdown();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if(message instanceof FetchAll) {

            final String url = ConfigFactory.load().getString("restopengov.dataset-list");

            try {
                String response = new Resty().text(url).toString();

                Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> datasetList = new Gson().fromJson(response, listType);

                getSelf().tell(datasetList);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (message instanceof List) {

            List<String> datasetList = (List<String>) message;

            for (String dataset : datasetList) {
                metadataFetcher.tell(dataset, getSelf());
            }

            getContext().stop(getSelf());

        } else {
            unhandled(message);
        }
    }
}
