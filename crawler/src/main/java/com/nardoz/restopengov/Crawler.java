package com.nardoz.restopengov;

import akka.actor.ActorSystem;
import com.nardoz.restopengov.ckan.CkanActorHandler;
import com.nardoz.restopengov.standalone.StandaloneActorHandler;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class Crawler {

    public static Logger logger = Logger.getLogger("restopengov");

    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("USAGE: crawler [command] <args>");
            System.out.println("Available commands:\n");
            System.out.println("Standalone commands:");
            System.out.println("standalone fetch-url <url>  Fetches a single file\n");
            System.out.println("CKAN commands:");
            System.out.println("ckan list                   Lists all datasets");
            System.out.println("ckan fetch-all              Fetches all datasets");
            System.out.println("ckan fetch <ds1 ds2 ds3>    Fetches all specified datasets (space separated)");
            return;
        }


        Config config = ConfigFactory.load();
        ActorSystem system = ActorSystem.create("crawler", config);

        system.registerOnTermination(new Runnable() {
            public void run() {
                nodeBuilder().client(true).node().close();
            }
        });

        final Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(
                        config.getString("restopengov.elasticsearch-host"),
                        config.getInt("restopengov.elasticsearch-port")));


        if(args[0].equals("ckan")) {
            CkanActorHandler ckanHandler = new CkanActorHandler(system, client);
            ckanHandler.handle(args);
        }
        else if(args[0].equals("standalone")) {
            StandaloneActorHandler standaloneHandler = new StandaloneActorHandler(system, client);
            standaloneHandler.handle(args);
        }
        else {
            System.out.println("Command not found");
            system.shutdown();
        }

    }

}
