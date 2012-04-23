package com.nardoz.restopengov;

import akka.actor.*;
import akka.routing.RoundRobinRouter;
import com.nardoz.restopengov.actors.*;
import org.elasticsearch.node.Node;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class Crawler {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("crawler");

        // Startup Elasticsearch connection
        final Node node = nodeBuilder().client(true).node();

        // Metadata persistence actor
        final ActorRef metadataPersist = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new MetadataPersist(node);
            }
        }).withRouter(new RoundRobinRouter(5)), "metadataPersist");


        // Metadata persistence actor
        final ActorRef resourceFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ResourceFetcher(node);
            }
        }).withRouter(new RoundRobinRouter(5)), "resourceFetcher");


        // Metadata fetcher actor
        final ActorRef metadataFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new MetadataFetcher(metadataPersist, resourceFetcher);
            }
        }).withRouter(new RoundRobinRouter(5)), "metadataFetcher");


        // Dataset list fetcher actor
        ActorRef datasetListFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new DatasetListFetcher(metadataFetcher);
            }
        }), "datasetListFetcher");


        // Go, go, go!
        datasetListFetcher.tell(new DatasetListFetcher.Fetch());

    }


}
