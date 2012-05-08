package ar.com.restba.examples;

import java.util.List;

import ar.com.restba.DefaultRestBAClient;
import ar.com.restba.RestBAClient;
import ar.com.restba.connectors.con.RestBAConnection;
import ar.com.restba.json.JsonObject;

/**
 * Imprime los autores de todos los recursos publicos de la ciudad de buenos
 * aires
 * 
 */
public class ListMetadatosJson {

	public static void main(String[] args) {

		RestBAClient dataBairesClient = new DefaultRestBAClient();
		String query = "gcba/metadata/_search?";

		RestBAConnection<JsonObject> fetchConnectionRestBA = dataBairesClient
				.fetchConnectionRestBA(query, JsonObject.class);

		for (List<JsonObject> page : fetchConnectionRestBA) {
			for (JsonObject metadato : page) {
				System.out.println("Author: " + metadato.getString("author")
						+ " Title: " + metadato.getString("title") + " _id: "
						+ metadato.getString("_id"));

			}

		}

	}

}
