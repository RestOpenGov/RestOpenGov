RestOpenGov
===========

El proyecto RestOpenGov surge inicialmente para proveer acceso programático a la información que el Gobierno de la Ciudad de Buenos Aires expone a través de http://data.buenosaires.gob.ar/.

Luego de una primera iteración el objetivo de RestOpenGov se ha vuelto más general, y se propone proveer una API pública de tipo REST, que permita acceder de una manera estándar a información que los gobiernos de diversos países y ciudades expongan a partir de fuentes heterogéneas de datos.

Por lo tanto, RestOpenGov estará compuesto por una serie de proyectos que interactuarán para lograr este fin.

## Proyectos

#### [RestOpenGov Crawler](https://github.com/RestOpenGov/RestOpenGov/tree/master/crawler)
Es el encargado de acceder periódicamente a los diversos endpoints publicados por los gobiernos, extraer la información que allí publican, procesarla, indexarla y almacenarla en un servidor elasticsearch para su posterior consulta.

#### [RestBA](https://github.com/RestOpenGov/RestOpenGov/tree/master/RestBA)
API java que brinda acceso programático a la información expuesta por el Gobierno de la Ciudad de Buenos, accediendo a RestOpenGov. Permite a los desarrolladores acceder de manera simple y type-safe a la información expuesta.

## Aplicaciones de ejemplo

#### [RestOpenGov.js](https://github.com/RestOpenGov/RestOpenGov/tree/master/RestOpenGov.js)
Es un simple cliente de RestOpenGov escrito en Javascript. Permite explorar los datos y realizar búsquedas.

#### [openBafici](https://openbafici-rog.rhcloud.com/) 
Una aplicación web mobile, desarrollada con restOpenGov, Play Framework 2.0 y Scala, desplegada en Openshift, para que puedas consultar toda la información del BAFICI desde tu celular. (fork me at [github](https://github.com/Nardoz/RestOpenGov/tree/master/openBafici))

#### [playDemo](https://playdemo-rog.rhcloud.com/) 
Tutorial paso a paso que que muestra cómo utilizar el servicio de restOpenGov, creando una aplicación Play 2.0 desde cero y poniéndola en línea en Openshift. Consultá el [tutorial](https://github.com/Nardoz/RestOpenGov/blob/master/playdemo/README.md)

## Primeros pasos
Para comenzar a utilizar una instalación de RestOpenGov hemos preparado [este tutorial](https://github.com/RestOpenGov/RestOpenGov/wiki/Hackaton).

## Comunidad
* [Wiki](https://github.com/RestOpenGov/RestOpenGov/wiki)
* [Mailing List](http://groups.google.com/group/restopengov)
* [Issue Tracking](https://github.com/RestOpenGov/RestOpenGov/issues)
* [Seguinos en twitter](https://twitter.com/#!/RestOpenGov)

## Autores
* Nicolás Melendez ([@nfmelendez](http://twitter.com/nfmelendez))
* Alan Reid ([@alan_reid](http://twitter.com/alan_reid))
* Sebastián Scarano ([@develsas](http://twitter.com/develsas))
* Marcos Della Pittima ([@mdellapittima](http://twitter.com/mdellapittima))
* Pablo Paladino ([@palamago](http://twitter.com/palamago))
* Walter J. Franck ([@wfranck](http://twitter.com/wfranck))

## Licencia
Este software es distribuído bajo la licencia Apache 2.0: http://www.apache.org/licenses/LICENSE-2.0