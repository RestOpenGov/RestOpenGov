package ar.com.restba.examples;

import java.util.List;

import ar.com.restba.DefaultRestBAClient;
import ar.com.restba.RestBAClient;
import ar.com.restba.connectors.con.RestBAConnection;
import ar.com.restba.types.ObraRegistrada;

/**
 * Ejemplo que lista la dirección de todas las obras registradas. Busca todas la
 * obras registradas en la ciudad de Buenos Aires. Obras en construcción, Obras
 * en demolición, etc con sus respectivos datos como dirección de la obra,
 * nombre del responsable, etc.
 * */
public class ListObrasRegistradas {

	public static void main(String[] args) {
		RestBAClient dataBairesClient = new DefaultRestBAClient("http://elastic.restopengov.org:9200/");
		String query = "gcba/obras-registradas/_search?";

		RestBAConnection<ObraRegistrada> fetchConnectionRestBA = dataBairesClient
				.fetchConnectionRestBA(query, ObraRegistrada.class, 0);

		for (List<ObraRegistrada> page : fetchConnectionRestBA) {
			for (ObraRegistrada obraRegistrada : page) {
				System.out.println(obraRegistrada.getDireccion());
			}

		}
	}

}
