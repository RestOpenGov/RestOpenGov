package com.nardoz.restopengov.standalone;

import akka.actor.*;
import akka.routing.FromConfig;
import com.nardoz.restopengov.ckan.actors.ResourceFetcher;
import com.nardoz.restopengov.standalone.models.RemoteFile;
import org.elasticsearch.client.Client;

public class StandaloneActorHandler {

    private ActorSystem system;
    public ActorRef fileFetcher;

    public StandaloneActorHandler(ActorSystem system, final Client client) {

        this.system = system;

        fileFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ResourceFetcher(client);
            }
        }).withRouter(new FromConfig()), "standaloneFileFetcher");

    }


    public void handle(String[] args) {

        if(args[1].equals("fetch-url") && args.length > 2) {
            RemoteFile file = new RemoteFile(args[1]);
            fileFetcher.tell(file);
        }
        else {
            System.out.println("Command not found");
            system.shutdown();
        }
    }

}
