RestBA
==========================

**RestBA en una libreria java pensada para acceder a los datos de la ciudad de buenos aires de una manera simple y flexible.**

*Tiene Licencia Apache, versión 2.0 , por lo que esta libreria se puede utilizar para cualquier actividad comercial*

Este blog post tiene información interesante sobre el uso de RestBA:
http://blog.melendez.com.ar/gobierno-abierto-accediendo-a-los-datos-de-la-ciudad-de-buenos-aires-usando-java/


Quick start
==========================

Usando maven se debe agregar en <dependecies>
	
	<dependency>
	  <groupId>ar.com.restba</groupId>
	  <artifactId>restba</artifactId>
	  <version>1.0.4</version>
	</dependency>
	
y luego debemos agregar el repositorio púplico de RestOpenGov,
adentro del tag <repositories> poner:
	
	  <repository>
	    <id>RestOpenGov</id>
	    <url>http://maven.restopengov.org/nexus/content/repositories/RestOpenGov</url>
	  </repository>
	
Un Ejemplo de como quedaria es:
===================================
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.mycompany.app</groupId>
  <artifactId>my-app</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>my-app</name>
  <url>http://maven.apache.org</url>
  <dependencies>
	
	<dependency>
	  <groupId>ar.com.restba</groupId>
	  <artifactId>restba</artifactId>
	  <version>1.0.4</version>
	</dependency>
	
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  
  <repositories>
	  <repository>
	    <id>RestOpenGov</id>
	    <url>http://maven.restopengov.org/nexus/content/repositories/RestOpenGov</url>
	  </repository>
  </repositories>
</project>


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


