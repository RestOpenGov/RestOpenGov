package ar.com.restba.utils;

import ar.com.restba.json.JsonArray;
import ar.com.restba.json.JsonObject;


/**
 * Utility Class con funciones comunes y sin comportamiento a todo el proyecto.
 * 
 * @author Nicolás Meléndez |
 * Email: nfmelendez@gmail.com |
 * Twitter: @nfmelendezs
 *
 */
public class RestBAUtils {

	private RestBAUtils() {
	}

	public static JsonObject findResourceById(JsonArray resources, String id) {
		if (null == id) {
			throw new RuntimeException("Id can't be null.");
		}

		if (null == resources) {
			throw new RuntimeException("resources can't be null");
		}

		for (int i = 0; i < resources.length(); i++) {
			JsonObject jsonObject = resources.getJsonObject(i);
			if (id.equals(jsonObject.getString("id"))) {
				return jsonObject;
			}
		}
		throw new RuntimeException("Could not find resource by id: " + id);
	}

}
