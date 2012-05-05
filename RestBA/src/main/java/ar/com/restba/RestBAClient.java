package ar.com.restba;

import ar.com.restba.connectors.con.RestBAConnection;
import ar.com.restba.json.JsonObject;

/**
 * Esta es la interfase principal de RestBA y define como el cliente debe
 * operar. Usá una implementación de esta interfaz para pedir datos a Buenos
 * Aires Data. La clase por default es {@link DefaultRestBAClient}s
 * 
 * @author nfmelendez
 * 
 */
public interface RestBAClient {

	/**
	 * Accede a los datos de la ciudad de Buenos Aires mediante una Query de tipo String, 
	 * agnostica a este nivel de abstraccion de la tecnologia, pero es para el caso
	 * de RestOpenGov una query de ElasticSEarch.
	 *  
	 * @param query Una query para traer datos, nunca nula  o null.
	 * @param connectionType El Tipo de dato que va a tratar de Mapear RestBA.
	 * Si se quiere json usar fetchConnectionRestBaAsJson. Nunca null.
	 * @return Devuelve una puntero que trae paginas de a 10 elementos.
	 */
	public <T> RestBAConnection<T> fetchConnectionRestBa(String query,
			Class<T> connectionType);
	
	
	/**
	 * Accede a los datos de la ciudad de Buenos Aires mediante una Query de tipo String, 
	 * agnostica a este nivel de abstraccion de la tecnologia, pero es para el caso
	 * de RestOpenGov una query de ElasticSEarch.
	 *  
	 * @param query Una query para traer datos, nunca nula  o null.
	 * @return Devuelve una puntero que trae paginas de a 10 elementos en formato Json.
	 */
	public RestBAConnection<JsonObject> fetchConnectionRestBaAsJson(String query, int page);

}
