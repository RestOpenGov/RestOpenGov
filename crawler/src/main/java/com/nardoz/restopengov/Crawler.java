package com.nardoz.restopengov;

import akka.actor.*;
import akka.routing.FromConfig;
import com.nardoz.restopengov.actors.DatasetListFetcher;
import com.nardoz.restopengov.actors.MetadataFetcher;
import com.nardoz.restopengov.actors.MetadataPersist;
import com.nardoz.restopengov.actors.ResourceFetcher;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.elasticsearch.client.Client;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class Crawler {

    public static void main(String[] args) {

        Config config = ConfigFactory.load();
        ActorSystem system = ActorSystem.create("crawler", config);

        // Startup Elasticsearch connection
        final Client client = nodeBuilder().client(true).node().client();

        // Metadata persistence actor
        final ActorRef metadataPersist = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new MetadataPersist(client);
            }
        }).withRouter(new FromConfig()), "metadataPersist");


        // Metadata persistence actor
        final ActorRef resourceFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ResourceFetcher(client);
            }
        }).withRouter(new FromConfig()), "resourceFetcher");


        // Metadata fetcher actor
        final ActorRef metadataFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new MetadataFetcher(metadataPersist, resourceFetcher);
            }
        }).withRouter(new FromConfig()), "metadataFetcher");


        // Dataset list fetcher actor
        final ActorRef datasetListFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new DatasetListFetcher(metadataFetcher);
            }
        }), "datasetListFetcher");


        // Go, go, go!
        datasetListFetcher.tell(new DatasetListFetcher.Fetch());


        system.registerOnTermination(new Runnable() {
            public void run() {
                nodeBuilder().client(true).node().close();
            }
        });

    }


}
