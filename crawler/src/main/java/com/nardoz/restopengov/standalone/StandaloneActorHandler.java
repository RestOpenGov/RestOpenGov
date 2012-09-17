package com.nardoz.restopengov.standalone;

import akka.actor.*;
import akka.routing.FromConfig;
import com.nardoz.restopengov.standalone.actors.FileFetcher;
import com.nardoz.restopengov.standalone.actors.ZipFileFetcher;
import com.nardoz.restopengov.standalone.models.RemoteFile;
import org.elasticsearch.client.Client;

public class StandaloneActorHandler {

    private ActorSystem system;
    public ActorRef fileFetcher;
    public ActorRef zipFileFetcher;

    public StandaloneActorHandler(ActorSystem system, final Client client) {

        this.system = system;

        fileFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new FileFetcher(client);
            }
        }).withRouter(new FromConfig()), "standaloneFileFetcher");

        zipFileFetcher = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ZipFileFetcher(client);
            }
        }).withRouter(new FromConfig()), "standaloneZipFileFetcher");

    }


    public void handle(String[] args) {

        if(args[1].equals("fetch-url") && args.length > 2) {

            RemoteFile file = new RemoteFile(args[2]);

            if(file.format.toLowerCase().equals("zip")) {
                zipFileFetcher.tell(file);
            } else {
                fileFetcher.tell(file);
            }

        } else {
            System.out.println("Command not found");
            system.shutdown();
        }
    }

}
