RestOpenGov
===========

El proyecto RestOpenGov surge inicialmente para proveer acceso programático a la información que el Gobierno de la Ciudad de Buenos Aires expone a través de http://data.buenosaires.gob.ar/.

Luego de una primera iteración el objetivo de RestOpenGov se ha vuelto más general, y se propone proveer una API pública de tipo REST, que permita acceder de una manera estándar a información que los gobiernos de diversos países y ciudades expongan a partir de fuentes heterogéneas de datos.

Por lo tanto, RestOpenGov estará compuesto por una serie de proyectos que interactuarán para lograr este fin.

## Proyectos

#### RestOpenGov Crawler
Es el encargado de acceder periódicamente a los diversos endpoints publicados por los gobiernos, extraer la información que allí publican, procesarla, indexarla y almacenarla en un servidor elasticsearch para su posterior consulta.

#### RestBA
API java que brinda acceso programático a la información expuesta por el Gobierno de la Ciudad de Buenos, accediendo a RestOpenGov. Permite a los desarrolladores acceder de manera simple y type-safe a la información expuesta.

#### RestOpenGov.js
Es un simple cliente de RestOpenGov escrito en Javascript. Permite explorar los datos y realizar búsquedas.

## Comunidad
* [Wiki](https://github.com/Nardoz/RestOpenGov/wiki)
* [Mailing List](http://groups.google.com/group/restopengov)
* [Issue Tracking](https://github.com/Nardoz/RestOpenGov/issues)

## Autores
* Nicolás Melendez ([@nfmelendez](http://twitter.com/nfmelendez))
* Alan Reid ([@alan_reid](http://twitter.com/alan_reid))
* Sebastián Scarano ([@develsas](http://twitter.com/develsas))
* Marcos Della Pittima ([@mdellapittima](http://twitter.com/mdellapittima))
