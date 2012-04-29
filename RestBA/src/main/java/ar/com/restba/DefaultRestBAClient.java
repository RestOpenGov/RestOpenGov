package ar.com.restba;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.com.restba.connectors.RestBAConnector;
import ar.com.restba.connectors.con.RestBAIterator;
import ar.com.restba.json.JsonArray;
import ar.com.restba.json.JsonObject;
import ar.com.restba.types.ObraRegistrada;
import ar.com.restba.utils.RestBAUtils;
import au.com.bytecode.opencsv.CSVReader;

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

	public DefaultRestBAClient() {
		restFbConnector = new RestBAConnector();
	}

	/**
	 * Busca todas la obras registradas en la ciudad de Buenos Aires. Obras en
	 * construcción, Obras en demolición, etc con sus respectivos datos como
	 * dirección de la obra, nombre del responsable, etc.
	 * 
	 * @return Todas las obras registradas en la Ciudad de Buenos Aires
	 */
	@Override
	public List<ObraRegistrada> fetchObrasRegistradas() {

//		com.restfb.json.JsonObject fetchObjectRestFb = restFbConnector
//				.fetchObject(
//						"obras-registradas/obras/_search?q=ESTADO_TRAMITE:REGISTRADO",
//						com.restfb.json.JsonObject.class);
		
		
		String query = "gcba/obras-registradas/_search?q=ESTADO_TRAMITE:REGISTRADO";
		RestBAIterator<com.restfb.json.JsonObject> fetchObjectRestFb = restFbConnector
				.fetchConnectionRestBA(query, com.restfb.json.JsonObject.class,0);

		for (List<com.restfb.json.JsonObject> list : fetchObjectRestFb) {
			System.out.println(list.toString());
			
		}
		
		
		

		return null;
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

}
