RestBA
==========================

**RestBA en una libreria java pensada para acceder a los datos de la ciudad de buenos aires de una manera simple y flexible.**

*Tiene Licencia Apache, versión 2.0 , por lo que esta libreria se puede utilizar para cualquier actividad comercial*

Este blog post tiene información interesante sobre el uso de RestBA:
http://blog.melendez.com.ar/gobierno-abierto-accediendo-a-los-datos-de-la-ciudad-de-buenos-aires-usando-java/


Quick start
==========================

Para bajarse la libreria y sus dependencias (pocas) usando <a href="http://maven.apache.org/">maven</a> se debe agregar adentro  del < dependecies > del pom.xml
	
	<dependency>
	  <groupId>ar.com.restba</groupId>
	  <artifactId>restba</artifactId>
	  <version>1.0.4</version>
	</dependency>
	
y luego debemos agregar el repositorio púplico de RestOpenGov,
adentro del tag < repositories > poner:
	
	  <repository>
	    <id>RestOpenGov</id>
	    <url>http://maven.restopengov.org/nexus/content/repositories/RestOpenGov</url>
	  </repository>
	  
<b>Si no queres usar maven</b> tambien te podes descargar la libreria con sus dependencias de github
en la carpeta <a href="https://github.com/Nardoz/RestOpenGov/tree/develop/RestBA/download">download</a>

*Ahora tu proyecto puede acceder a los datos de la ciudad de Buenos Aires y ser feliz como una lombriz :)*


Ejemplo 1 : Lista Obras Registradas con Mapping de json a objeto java.
=========================================================================

 *Ejemplo que lista la dirección de todas las obras registradas. Busca todas la obras registradas en la ciudad de Buenos Aires. Obras en construcción, Obras en demolición, etc con sus respectivos datos como dirección de la obra, nombre del responsable, etc.*
  
	public static void main(String[] args) {
		RestBAClient dataBairesClient = new DefaultRestBAClient();
		String query = "gcba/obras-registradas/_search?";

		RestBAConnection<ObraRegistrada> fetchConnectionRestBA = dataBairesClient
				.fetchConnectionRestBA(query, ObraRegistrada.class);

		for (List<ObraRegistrada> page : fetchConnectionRestBA) {
			for (ObraRegistrada obraRegistrada : page) {
				System.out.println(obraRegistrada.getDireccion());
			}

		}
	}


Ejemplo 2 : Lista los metadatos pidiendo solamente json, sin mappear.
=========================================================================
*Imprime los autor, título de todos los datasets de la ciudad de buenos aires*

public static void main(String[] args) {

		RestBAClient dataBairesClient = new DefaultRestBAClient();
		String query = "gcba/metadata/_search?";

		RestBAConnection<JsonObject> fetchConnectionRestBA = dataBairesClient
				.fetchConnectionRestBA(query, JsonObject.class);

		for (List<JsonObject> page : fetchConnectionRestBA) {
			for (JsonObject metadato : page) {
				System.out.println("Author: " + metadato.getString("author")
						+ " Title: " + metadato.getString("title") + " _id: "
						+ metadato.getString("_id"));

			}

		}

	}


Ejemplo 3 : Pedido simple a elastic search y devuelve un json normal
=========================================================================

public static void main(String[] args) {

		RestBAClient dataBairesClient = new DefaultRestBAClient();
		String query = "gcba/metadata/_search?&from=0";

		JsonObject q = dataBairesClient.executeQuery(query, JsonObject.class);
		
		System.out.println("JSON object: " + q.toString());
	}
