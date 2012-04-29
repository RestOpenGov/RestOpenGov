package ar.com.restba.examples;

import java.util.List;

import ar.com.restba.DefaultRestBAClient;
import ar.com.restba.RestBAClient;
import ar.com.restba.types.ObraRegistrada;

/**
 *  Ejemplo que lista la dirección de todas las obras registradas.
 *  */
public class ListObrasRegistradas {
	
	
	public static void main(String[] args) {
		RestBAClient dataBairesClient = new DefaultRestBAClient();
		List<ObraRegistrada> fetchObrasRegistradas = dataBairesClient
				.fetchObrasRegistradas(); //aca pide datos al server.

//		for (ObraRegistrada obrasRegistradas : fetchObrasRegistradas) {
//			System.out.println(obrasRegistradas.getDireccion());
//		}
	}

}
