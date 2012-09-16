package com.nardoz.restopengov.ckan;

import akka.actor.*;
import akka.routing.FromConfig;
import com.nardoz.restopengov.ckan.actors.*;
import org.elasticsearch.client.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CkanActorHandler {

    private ActorSystem system;
    public ActorRef resourceFetcher;
    public ActorRef zipResourceFetcher;
    public ActorRef metadataPersist;
    public ActorRef metadataFetcher;
    public ActorRef datasetListFetcher;

    public CkanActorHandler(ActorSystem system, final Client client) {

        this.system = system;

        resourceFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ResourceFetcher(client);
            }
        }).withRouter(new FromConfig()), "ckanResourceFetcher");


        zipResourceFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ZipResourceFetcher(client);
            }
        }).withRouter(new FromConfig()), "ckanZipResourceFetcher");


        metadataPersist = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new MetadataPersist(client);
            }
        }).withRouter(new FromConfig()), "ckanMetadataPersist");


        metadataFetcher = system.actorOf(new Props(MetadataFetcher.class).withRouter(new FromConfig()), "ckanMetadataFetcher");

        datasetListFetcher = system.actorOf(new Props(DatasetListFetcher.class), "ckanDatasetListFetcher");

    }

    public void handle(String[] args) {
        if(args[1].equals("list")) {
            datasetListFetcher.tell(new DatasetListFetcher.ListAll());
        }
        else if(args[1].equals("fetch-all")) {
            datasetListFetcher.tell(new DatasetListFetcher.FetchAll());
        }
        else if(args[1].equals("fetch") && args.length > 2) {
            List<String> list = new ArrayList<String>(Arrays.asList(args));
            list.remove(0);
            list.remove(1);
            datasetListFetcher.tell(list);
        }
        else {
            System.out.println("Command not found");
            system.shutdown();
        }
    }

}
