## RestOpenGov Crawler

 El Crawler encargado de acceder periódicamente a los diversos endpoints publicados por los gobiernos, extraer la información que allí publican, procesarla y almacenarla en un servidor elasticsearch para su posterior consulta.

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
../tools/elasticsearch-0.19.2/bin/elasticsearch
```

4. Ejecutar el crawler:
```
mvn exec:java -Dexec.args="fetch-all"
```

5. Verlo en acción:
Abrir un explorador y navegar a ```http://localhost:9200/_plugin/head/```.


El crawler tiene 3 modos de ejecución por CLI:

```
list
fetch-all
fetch <dataset1 dataset2 dataset3 ...>
```

### Configuración
Es posible configurar tanto Akka como el crawler desde el mismo archivo de configuración:
```
src/main/java/resources/application.conf
```

En lo que respecta al crawler, podemos definir el entrypoint (actualmente sólo soporta uno) de la API REST de metadatos de CKAN (la que contiene el listado de datasets disponibles).
Por otro lado, se puede setear el nombre del índice que va a utilizar el crawler y la cantidad máxima de elementos a indexar enviados en un sólo bulk.
```
restopengov {
    dataset-list = "http://data.buenosaires.gob.ar/api/rest/dataset/"
    index = "gcba"
    max-per-bulk = 500
}
```

Más detalles de cómo configurar Akka aquí:
http://doc.akka.io/docs/akka/2.0.1/general/configuration.html

### Formatos soportados
Hasta el momento, el crawler sólo soporta datasets en CSV (separados por coma, punto y coma o tabs) o varios CSVs comprimidos en un ZIP.

## Actores
![Actors](http://f.cl.ly/items/042M1m1b320I1f2f0S3v/Image%202012.04.22%2011:10:38%20PM.png)

