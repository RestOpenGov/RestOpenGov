package ar.com.restba;

import ar.com.restba.connectors.RestBAConnector;
import ar.com.restba.connectors.con.RestBAConnection;
import ar.com.restba.exception.RestBAException;

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

	public DefaultRestBAClient() {
		restFbConnector = new RestBAConnector();
	}

	/**
	 * Accede a los datos de la ciudad de Buenos Aires mediante una Query. Esta
	 * query tiene que ser como se describe en
	 * http://www.elasticsearch.org/guide/reference/api/search/uri-request.html
	 * 
	 * Y se tiene que evitar los parametros from y size en la URL, ya que son
	 * manejados internamente por el Iterator.
	 * 
	 * @param query
	 *            Una query para traer datos, nunca nula o null. Que cumple con
	 *            http://www.elasticsearch.org/guide/reference/api/search/uri-
	 *            request.html y no envie como parametro ni from y ni size. Ya
	 *            que esto lo maneja internamente el iterator.
	 * @param connectionType
	 *            El Tipo de dato que va a tratar de Mapear RestBA. Si se quiere
	 *            json usar fetchConnectionRestBaAsJson. Nunca null.
	 * @return Devuelve una puntero que trae paginas de a 10 elementos.
	 */
	@Override
	public <T> RestBAConnection<T> fetchConnectionRestBA(String query,
			Class<T> connectionType) {

		if (null == query || "".equals(query)) {
			throw new RestBAException("The query can't be null or empty");
		}

		if (null == connectionType) {
			throw new RestBAException("The connectionType can't be null");
		}

		if (query.toLowerCase().contains("from=")) {
			throw new RestBAException(
					"From parameter should not be in the query"
							+ " because connection pointer abstractions use that.");
		}

		if (query.toLowerCase().contains("size=")) {
			throw new RestBAException(
					"Size parameter should not be in the query"
							+ " because connection pointer abstractions use that.");
		}

		RestBAConnection<T> fetchConnectionRestBA = restFbConnector
				.fetchConnectionRestBA(query, connectionType, 0);
		return fetchConnectionRestBA;
	}

	@Override
	public <T> T executeQuery(String query, Class<T> objectType) {
		T result = restFbConnector.executeQuery(query, objectType);
		return result;
	}

}
