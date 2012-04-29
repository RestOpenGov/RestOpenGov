package ar.com.restba;

import ar.com.restba.connectors.RestBAConnector;
import ar.com.restba.connectors.con.RestBAConnection;
import ar.com.restba.json.JsonObject;

/**
 * Es la implementación por default de {@link RestBAClient} Esta clase es una de
 * las más importante de todas, porque es la que hay que instanciar para poder
 * usar esta libreria.
 * 
 * 
 * @author Nicolás Mélendez | Email: nfmelendez@gmail.com | Twitter: @nfmelendez
 * 
 */
public class DefaultRestBAClient implements RestBAClient {

	/**
	 * Es el connector de RestFB que utilizamos para hacernos de todas las
	 * bondades de esta excelente libreria.
	 */
	private RestBAConnector restFbConnector;

	public DefaultRestBAClient(String host) {
		restFbConnector = new RestBAConnector(host);
	}

	/**
	 * Accede a los datos de la Ciudad sin ningun tipo de procesamiento. Solo
	 * devuelve jsons que hay que parsearos para darle algún sentido.
	 * Recomendamos usar este metodo cuando no se encuentre una abstracción más
	 * alta. Este es el metodo que usamos los desarrolladores de RestBA para
	 * hacer abstracciones mas comodas de los datos de la Ciudad. Si vos los
	 * estas usando, estamos casi seguro que tenes algo para colaborar a etsa
	 * libreria :) Te esperamos, manda un mail a nfmelendez@gmail.com
	 * 
	 * @param dataset
	 *            Es el nombre del dataset que se quiere acceder. No puede ser
	 *            ni null, ni vacio. Los nombres estan en esta URL <a
	 *            href="http://data.buenosaires.gob.ar/api/rest/dataset"
	 *            >http://data.buenosaires.gob.ar/api/rest/dataset</a>
	 * @return las obras registradas de la Ciudad de Buenos Aires.
	 */
	@Override
	public JsonObject fetchDataset(String dataset) {
		com.restfb.json.JsonObject fetchObject = restFbConnector.fetchObject(
				dataset, com.restfb.json.JsonObject.class);
		return new JsonObject(fetchObject.toString());
	}

	@Override
	public <T> RestBAConnection<T> fetchConnectionRestBA(String query,
			Class<T> connectionType, int fromPage) {
		RestBAConnection<T> fetchConnectionRestBA = restFbConnector
				.fetchConnectionRestBA(query, connectionType, fromPage);
		return fetchConnectionRestBA;
	}

}
