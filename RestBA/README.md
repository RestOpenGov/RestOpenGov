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


