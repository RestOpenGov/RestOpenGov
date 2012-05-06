RestBA
==========================

**RestBA en una libreria java pensada para acceder a los datos de la ciudad de buenos aires de una manera simple y flexible.**

*Tiene Licencia Apache, versión 2.0 , por lo que esta libreria se puede utilizar para cualquier actividad comercial*

Este blog post tiene información interesante sobre el uso de RestBA:
http://blog.melendez.com.ar/gobierno-abierto-accediendo-a-los-datos-de-la-ciudad-de-buenos-aires-usando-java/


Quick start
==========================
Para empezar hay que descargar nuestra libreria y sus dependencias. Luego agregarlas al proyecto y listo.<br>
<b>Libreria:</b><br>
RestBA 1.0 :  https://github.com/melendeznicolas/RestBA/raw/master/download/lib/restba-1.0.jar <br>
<b>Dependencias:</b><br>
Open CSV 2.3: https://github.com/melendeznicolas/RestBA/raw/master/download/lib/opencsv-2.3.jar <br>
Rest FB 1.6.9 https://github.com/melendeznicolas/RestBA/raw/master/download/lib/restfb-1.6.9.jar <br>

*Ahora tu proyecto puede acceder a los datos de la ciudad de Buenos Aires y ser feliz como una lombriz :)*


Ejemplos
===========================
*Lista todas las direcciones de todas las obras registradas en la Ciudad de Buenos Aires. En demolición, obra, ampliación parcial, etc...*

	public static void main(String[] args) {
		RestBAClient dataBairesClient = new DefaultRestBAClient();
		List<ObrasRegistradas> fetchObrasRegistradas = dataBairesClient
				.fetchObrasRegistradas();

		for (ObrasRegistradas obrasRegistradas : fetchObrasRegistradas) {
			System.out.println(obrasRegistradas.getDireccion());
		}

	}


