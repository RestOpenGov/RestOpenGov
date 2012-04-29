package ar.com.restba;

import java.util.List;


import ar.com.restba.json.JsonObject;
import ar.com.restba.types.ObraRegistrada;

/**
 * Esta es la interfase principal de RestBA y define como el cliente debe operar.
 * Usá una implementación de esta interfaz para pedir datos a Buenos Aires Data.
 * La clase por default es {@link DefaultRestBAClient}s
 * 
 * @author nfmelendez
 * Mail: nfmelendez@gmail.com
 * Twitter: @nfmelendezs
 *
 */
public interface RestBAClient {

	/**
	 * Busca todas la obras registradas en la ciudad de Buenos Aires.
	 * Obras en construcción, Obras en demolición, etc 
	 * con sus respectivos datos como dirección de la obra, nombre del responsable, etc.
	 * 
	 * @return Todas las obras registradas en la Ciudad de Buenos Aires
	 */
	public List<ObraRegistrada> fetchObrasRegistradas();

	/**
	 * Accede a los datos de la Ciudad sin ningun tipo de procesamiento.
	 * Solo devuelve jsons  que hay que parsearos para darle algún sentido.
	 * Recomendamos usar este metodo cuando no se encuentre una abstracción más alta.
	 * Este es el metodo que usamos los desarrolladores de RestBA para hacer abstracciones 
	 * mas comodas de los datos de la Ciudad. Si vos los estas usando, estamos casi seguro 
	 * que tenes algo para colaborar a etsa libreria :) Te esperamos, manda un mail a nfmelendez@gmail.com
	 * 
	 * @param dataset Es el nombre del dataset que se quiere acceder. No puede ser ni null, ni vacio.
	 *  Los nombres estan en 
	 * esta URL <a href="http://data.buenosaires.gob.ar/api/rest/dataset">http://data.buenosaires.gob.ar/api/rest/dataset</a>
	 * @return las obras registradas de la Ciudad de Buenos Aires.
	 */
	public JsonObject fetchDataset(String dataset);

}
