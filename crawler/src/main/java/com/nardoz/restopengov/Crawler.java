package com.nardoz.restopengov;

import akka.actor.*;
import com.nardoz.restopengov.actors.DatasetListFetcher;
import com.nardoz.restopengov.actors.MetadataFetcher;
import com.nardoz.restopengov.actors.Persist;

public class Crawler {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("crawler");

        final ActorRef persist = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new Persist();
            }
        }), "persist");

        final ActorRef metadataFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new MetadataFetcher(persist);
            }
        }), "metadataFetcher");

        ActorRef datasetListFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new DatasetListFetcher(metadataFetcher);
            }
        }), "datasetListFetcher");


        datasetListFetcher.tell(new DatasetListFetcher.Fetch());

    }


}
