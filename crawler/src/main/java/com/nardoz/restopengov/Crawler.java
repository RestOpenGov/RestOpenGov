package com.nardoz.restopengov;

import akka.actor.*;
import akka.routing.FromConfig;
import com.nardoz.restopengov.actors.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class Crawler {

    public static Logger logger = Logger.getLogger("restopengov");

    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("USAGE: crawler [command] <args>");
            System.out.println("Available commands:");
            System.out.println("list            Lists all datasets");
            System.out.println("fetch-all       Fetches all datasets");
            System.out.println("fetch <args>    Fetches all specified datasets (space separated)");
            return;
        }

        Config config = ConfigFactory.load();
        ActorSystem system = ActorSystem.create("crawler", config);

        system.registerOnTermination(new Runnable() {
            public void run() {
                nodeBuilder().client(true).node().close();
            }
        });

        // Startup Elasticsearch connection
        final Client client = nodeBuilder().client(true).node().client();

        // Metadata persistence actor
        final ActorRef metadataPersist = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new MetadataPersist(client);
            }
        }).withRouter(new FromConfig()), "metadataPersist");


        // Resource fetcher actor
        final ActorRef resourceFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ResourceFetcher(client);
            }
        }).withRouter(new FromConfig()), "resourceFetcher");


        // ZIP Resource fetcher actor
        final ActorRef zipResourceFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ZipResourceFetcher(client);
            }
        }).withRouter(new FromConfig()), "zipResourceFetcher");


        // Metadata fetcher actor
        final ActorRef metadataFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new MetadataFetcher(metadataPersist, resourceFetcher, zipResourceFetcher);
            }
        }).withRouter(new FromConfig()), "metadataFetcher");


        // Dataset list fetcher actor
        final ActorRef datasetListFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new DatasetListFetcher(metadataFetcher);
            }
        }), "datasetListFetcher");


        // Go, go, go!
        if(args[0].equals("list")) {
            datasetListFetcher.tell(new DatasetListFetcher.ListAll());
        }
        else if(args[0].equals("fetch-all")) {
            datasetListFetcher.tell(new DatasetListFetcher.FetchAll());
        }
        else if(args[0].equals("fetch") && args.length > 1) {
            List<String> list = new ArrayList<String>(Arrays.asList(args));
            list.remove(0);
            datasetListFetcher.tell(list);
        } else {
            System.out.println("Command not recognized");
            system.shutdown();
        }

    }

}
