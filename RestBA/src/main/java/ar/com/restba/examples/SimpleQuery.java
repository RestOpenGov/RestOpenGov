package ar.com.restba.examples;

import ar.com.restba.DefaultRestBAClient;
import ar.com.restba.RestBAClient;
import ar.com.restba.json.JsonObject;

/**
 * Muestra como hacer una query de elastic search
 * 
 */
public class SimpleQuery {

	public static void main(String[] args) {

		RestBAClient dataBairesClient = new DefaultRestBAClient();
		String query = "gcba/metadata/_search?&from=0";

		JsonObject q = dataBairesClient.executeQuery(query, JsonObject.class);
		
		System.out.println("JSON object: " + q.toString());
	}

}
