## RestOpenGov Crawler

El Crawler es el encargado de acceder periódicamente a los diversos endpoints publicados por los gobiernos, extraer la información que allí publican, procesarla y almacenarla en un servidor elasticsearch para su posterior consulta.

### Tecnologías
El procesamiento de los datasets es realizado utilizando la API Java de Akka (www.akka.io), un framework de procesamiento distribuído basado en Actores.
Para la búsqueda e indexación de la información, utilizamos Elasticsearch (www.elasticsearch.org). 

### Setup

1. Bajar las fuentes:
```
git clone git@github.com:Nardoz/RestOpenGov.git 
cd RestOpenGov/crawler
```

2. Compilar y bajar dependencias:
```
mvn compile
```
Para crear el proyecto para Eclipse ejecutar ```mvn eclipse:eclipse``` o ```mvn idea:idea``` para IntelliJ. 

3. Correr Elasticsearch:
```
../tools/elasticsearch-0.19.9/bin/elasticsearch
```

4. Ejecutar el crawler:
```
mvn exec:java -Dexec.args="ckan fetch-all"
```

5. Verlo en acción:
Abrir un explorador y navegar a ```http://localhost:9200/_plugin/head/```.


El crawler tiene dos modos de ejecución por CLI. Uno es para trabajar con CKAN y otro como standalone:

CKAN:
```
ckan list
ckan fetch-all
ckan fetch <dataset1 dataset2 dataset3 ...>
```

Standalone:
```
standalone fetch-url <url>
```

### Configuración
Es posible configurar tanto Akka como el crawler desde el mismo archivo de configuración:
```
src/main/java/resources/application.conf
```

En lo que respecta al crawler, podemos definir el entrypoint (actualmente sólo soporta uno) de la API REST de metadatos de CKAN (la que contiene el listado de datasets disponibles).

También se puede configurar cantidad máxima de elementos a indexar enviados en un sólo bulk.

```
restopengov {
    ckan-rest-api = "http://data.buenosaires.gob.ar/api/rest/dataset/"
    index = "gcba"
    max-per-bulk = 500
}
```

Por otro lado, se puede setear el nombre del índice que va a utilizar el crawler (en este ejemplo gcba), si bien actualmente el servicio no se encargará de crearlo automáticamente. La manera más fácil de hacerlo es siguiendo esta guía: http://www.elasticsearch.org/guide/reference/api/admin-indices-create-index.html

```
$ curl -XPUT 'http://localhost:9200/gcba/'

$ curl -XPUT 'http://localhost:9200/gcba/' -d '
index :
    number_of_shards : 3
    number_of_replicas : 2
'
```

Más detalles de cómo configurar Akka aquí:
http://doc.akka.io/docs/akka/2.0.1/general/configuration.html

### Formatos soportados
Hasta el momento, el crawler sólo soporta datasets en CSV (separados por coma, punto y coma o tabs) o varios CSVs comprimidos en un ZIP.

## Actores
![Actors](http://f.cl.ly/items/042M1m1b320I1f2f0S3v/Image%202012.04.22%2011:10:38%20PM.png)

## Licencia
Este software es distribuído bajo la licencia Apache 2.0: http://www.apache.org/licenses/LICENSE-2.0