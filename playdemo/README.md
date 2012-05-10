# Desarrollando una aplicación mobile con restOpenGov usando Play Framework 2.0 y Scala

Para mostrarles cómo pueden utilizar la API de restOpenGov, haremos un ejemplo completo, paso a paso, en el cual desarrollaremos una aplicación mobile usando Play Framework 2.0 y Scala, para luego desplegarla en Openshift, la plataforma cloud-computing libre (y gratuita) de Red Hat.

## Instalación de Play Framework

Para ello deben tener instalado un [jdk de Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

Luego descarguen [Play Framework](http://www.playframework.org/) de su sitio web, [aquí](http://download.playframework.org/releases/play-2.0.1.zip) tienen un link.

O desde la línea de comandos pueden hacer:

wget http://download.playframework.org/releases/play-2.0.1.zip
unzip play-2.0.1.zip

Para mayor comodidad agréguen el directorio play-2.0.1 al path.

## Creando la aplicación

Luego escriban

```
play new playdemo
``` 

Como nombre de aplicación ingresen 'playdemo' y elijan crear una aplicación simple con scala. Luego deben iniciar la aplicación:

```
cd playdemo
play
```

Con esa orden ingresarán a la consola de play.

```
             
play! 2.0.1, http://www.playframework.org

> Type "help play" or "license" for more information.
> Type "exit" or use Ctrl+D to leave this console.

[playdemo] $ 

```

Para iniciar la aplicación, desde la consola de play, escribimos "~ run"

``` 
[playdemo] $ ~ run

[info] Updating {file:/home/sas/dev/apps/tmp/playdemo/}playdemo...

--- (Running the application from SBT, auto-reloading is enabled) ---

[info] play - Listening for HTTP on port 9000...

(Server started, use Ctrl+D to stop and go back to the console...)
```

Abran un explorador en [http://localhost:9000/](http://localhost:9000/) y verán la página de bienvenida de Play Framework. En esta página nos dan una brevísima introducción al funcionamiento del framework, explicando cómo interactúan el archivo de rutas, los controladores y las vistas para mostrarnos la página de bienvenida.

Desde cualquier editor de texto, abran el archivo 'app/views/index.scala.html' y cambien `@play20.welcome(message)` por algo como '¡Hola desde restOpenGov!'. Vuelvan al explorador y refrequen la página.

> Play recompila automáticamente nuestra aplicación cada vez que detecta un cambio en los archivos. Esto nos permite trabajar con un simple editor de texto, modificar los archivos y ver los cambios reflejados en nuestro explorador. Ya sabemos, si están acostumbrados a trabajar con php, ruby, python o similares, todo esto les parecerá algo natural, pero en el mundo Java esto es bastante novedoso.

Si desean [configurar un IDE](https://github.com/opensas/Play20Es/wiki/IDE), tan sólo tienen que frenar el servidor con ctrl-d, ejecutar el comando 'eclipsify' o 'idea' para generar el proyecto para eclipse o IntelliJ respectivamente, volver a iniciar el servidor con '~ run', e importar el proyecto desde el IDE.

##Accediendo a restOpenGov

Nuestra aplicación de ejemplo simplemente nos mostrará una lista con el título y el resumen de las películas del festival de Buenos Aires (bafici) y nos permitirá filtrar por texto buscando en el resumen de la película.

Nuestro controlador recibirá una parámetro 'q' a través del querystring con los términos a utilizar para filtrar. Usaremos el valor de este parámetro para armar la consultar para acceder al web service de restOpenGov.

En nuestro archivo de rutas, especificaremos el parámetro que llegará a nuestro controlador y le asignaremos un string vacío como valor por defecto:

/conf/routes
```
# Home page
GET     /                           controllers.Application.index(q: String ?= "")
``` 

Para acceder a la informacion precisaremos armar un url como el siguiente:

[http://elastic.restopengov.org/gcba/bafici/_search?fields=title_es,synopsis_es&q=id_film:*+AND+title_es:*](http://elastic.restopengov.org/gcba/bafici/_search?fields=title_es,synopsis_es&q=id_film:*+AND+title_es:*)

Podés probarlo con cualquier navegador (agregale un parámetro 'pretty=1' para más comodidad) o desde la línea de comandos:

```
curl http://elastic.restopengov.org/gcba/bafici/_search?pretty=1&fields=title_es,synopsis_es&q=id_film:*+AND+title_es:*
```

Y obtendrás el siguiente mensaje [json](http://json.org/):

```
{
  "took" : 102,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 1011,
    "max_score" : 1.4142135,
    "hits" : [ {
      "_index" : "gcba",
      "_type" : "bafici",
      "_id" : "bafici11-films-4",
      "_score" : 1.4142135,
      "fields" : {
        "title_es" : "Cerdo hormiguero",
        "synopsis_es" : "La opera prima de Kitao Sakurai[...]"
      }
    }, {
      "_index" : "gcba",
      "_type" : "bafici",
      "_id" : "bafici11-films-9",
      "_score" : 1.4142135,
      "fields" : {
        "title_es" : "Pink Saris",
        "synopsis_es" : "La camara de Kim Longinotto[...]"
      }
      [... más películas ...]
    } ]
  }
}

```

## El modelo

Ahora lo que tendremos que hacer es definir un objeto Film para almacenar esta información, al final del controlador agregamos la siguiente línea:

/app/controllers/Application.scala
```
case class Film(id: String, titulo: String, resumen: String)

```

Con esto ya hemos definido una clase, con sus getters y setter, su constructor por defecto y métodos 'Hash' y 'Equals'

## Controladores: Todo bajo control

Luego accederemos al web service, para ello tendremos que generar la consulta al web service. Básicamente queremos traer todos los films, que en su título o resumen contengan la frase especificada. Nuestra consulta debería tener la siguiente forma: 'id_film:* AND title_es:* AND (title_es:'termino' OR synopsis_es:'termino')

```
import java.net.URLEncoder

object Application extends Controller {
  
  def index(q: String = "") = Action {
    val elasticQuery = "http://elastic.restopengov.org/gcba/bafici/_search?" + 
      "fields=title_es,synopsis_es&" + 
      "q=" + formatQuery("cerdo")
      

    Ok(views.html.index("Hola desde restOpenGov"))
  }
  
  private def formatQuery(q:String = ""):String = {
    val query = if (q=="") {
      "id_film:* AND title_es:*"
    } else {
      "id_film:* AND (title_es:%s OR synopsis_es:%s)".format(q, q)
    }
    URLEncoder.encode(query, "UTF-8")
  }

}

case class Film(id: String, titulo: String, resumen: String)
```

y parsearemos su resultado como un json

```
    // accedemos al web service y parseamos su resultado como un json
    val json: JsValue = WS.url(elasticQuery).get().await.get.json

```

Ahora lo que debemos hacer es acceder al array de hits, instanciar un film por cada elemento del array de hits, y pasar esta información a nuestra vista. Así es como quedaría nuestro controlador completo

app/controllers/Application.scala
```
package controllers

import play.api._
import play.api.mvc._

import play.api.libs.ws.WS
import play.api.libs.json.JsValue

import java.net.URLEncoder

import play.Logger

object Application extends Controller {
  
  def index(q: String = "") = Action {

    val elasticQuery = "http://elastic.restopengov.org/gcba/bafici/_search?" + 
      "fields=title_es,synopsis_es&" + 
      "q=" + formatQuery(q)

    Logger.info("[info] about to fetch %s".format(elasticQuery) )

    // accedemos al web service y parseamos su resultado como un json
    val json: JsValue = WS.url(elasticQuery).get().await.get.json

    // get the hits array
    val hits = (json \ "hits" \ "hits").as[Seq[JsValue]]

    // convert json hits to Seq[Film]
    val films: Seq[Film] = hits.map { hit => 
      Film( 
        (hit \ "_id").as[String], 
        (hit \ "fields" \ "title_es").as[String], 
        (hit \ "fields" \ "synopsis_es").as[String] 
      )
    }

    Ok(views.html.index(q, films, elasticQuery))
  }
  
  private def formatQuery(q:String = ""):String = {
    val query = if (q=="") {
      "id_film:* AND title_es:*"
    } else {
      "id_film:* AND (title_es:%s OR synopsis_es:%s)".format(q, q)
    }
    URLEncoder.encode(query, "UTF-8")
  }

}

case class Film(id: String, titulo: String, resumen: String)

```

## La Vista

Ahora bien, si vuelven al explorador y refrescan la página se encontrarán con el siguiente error:

```
too many arguments for method apply: (message: String)play.api.templates.Html in object index
```

Play nos avisa que tenemos que actualizar nuestro template y agregar los parámetros necesarios. Todas estas validaciones son realizadas por Play en tiempo de compilación.

En la vista lo único que haremos será crear un form para que ingresen el término a buscar, y luego iterar la lista de films para mostrarlos en pantalla. También crearemos links a la consulta de nuestro web service para que lo puedan ver en acción.

app/views/index.scala.html
```
@(q: String, films: Seq[Film], elasticQuery: String)

@main("Welcome to Play 2.0") {

<h1>Play Framework 2.0 simple demo for restOpenGov</h1><hr />

  <form method="get" action="@routes.Application.index()">
    <label for="query">Buscar:</label>
    <input type="text" name="q" value="@q" />

    <input type="submit" id="submit"value="Buscar" />

  </form>

  <h3><a target="_blank" href="@elasticQuery">Filmes</a></h3>
 
  <table class="table">
  @for(film <- films) {
    <tr>
      <th><a target="_blank" href="http://elastic.restopengov.org/gcba/bafici/@film.id?pretty=1">@film.titulo</a></th>
      <td>@film.resumen</td>
    </tr>
  }
  </table>
}
```

Pueden ir al explorador y refrescar la página. Ingresen un término de búsqueda y todo debería funcionar bien.

Como último paso, aplicaremos twitter bootstrap a nuestra aplicación para mejorar su apariencia.

Agreguen el estilo de twitter bootstrap y encierren en @content en un "container" div

app/views/main.scala.html
```
@(title: String)(content: Html)

<!DOCTYPE html>

<html>
    <head>
        <title>@title</title>
        <link href="http://twitter.github.com/bootstrap/assets/css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" media="screen" href="@routes.Assets.at("stylesheets/main.css")">
        <link rel="shortcut icon" type="image/png" href="@routes.Assets.at("images/favicon.png")">
        <script src="@routes.Assets.at("javascripts/jquery-1.7.1.min.js")" type="text/javascript"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> 
    </head>
    <body>
        <div class="container">
            @content
        </div>
    </body>
</html>
```

Y luego agreguemos algo de estilo a nustra página:

app/views/indes.scala.html
```
@(q: String, films: Seq[Film], elasticQuery: String)

@main("Welcome to Play 2.0") {


<h1>Play Framework 2.0 simple demo for restOpenGov</h1><hr />

  <form method="get" action="@routes.Application.index()" id="form" class="form-inline" >
    <p>
    <label for="query">Buscar:</label>
    <input type="text" name="q" value="@q" />

    <input type="submit" id="submit" class="btn btn-primary" value="Buscar" />

  </form>

  <h3><a target="_blank" href="@elasticQuery">Filmes</a></h3>
 
  <table class="table">
  @for(film <- films) {
    <tr>
      <th><a target="_blank" href="http://elastic.restopengov.org/gcba/bafici/@film.id?pretty=1">@film.titulo</a></th>
      <td>@film.resumen</td>
    </tr>
  }
  </table>
    
}
```

## ¡A la nube!

Ahora vamos a poner esta aplicación en producción en [Openshift](https://openshift.redhat.com/) la nube de Red Hat. Para ellos vamos a utilizar el siguiente [quickstart](https://github.com/opensas/play2-openshift-quickstart). Tambien tenés un [screen cast](http://playlatam.wordpress.com/2012/05/01/desplegando-aplicaciones-de-play-framework-2-con-java-y-scala-en-openshift/) en el que mostramos paso a paso como desarrollar una aplicación con Play, usando Java y Scala, para luego deplegarla en Openshift.

Antes que nada tendrás que sacar una cuenta en Openshift, instalar git y el el cliente de línea de comando de Openshift, registrar tu clave pública ssh y generar un dominio. Tan sólo sigan los pasos 3 al 5 de [este artículo](http://playlatam.wordpress.com/2012/02/09/play-framework-on-the-cloud-made-easy-openshift-module/)

Estando en el directorio de tu aplicación, ejecuta los siguientes comandos para crear un repositorio git:

```
git init
```

Luego deberás ejecutar este comando para crear una nueva aplicación en openshift:

```
rhc app create -a playdemo -t diy-0.1 --nogit -l yourlogin@openshift.com -p yoursecretpass
Creating application: playdemo
Now your new domain name is being propagated worldwide (this might take a minute)...

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
IMPORTANT: Since the -n flag was specified, no local repo has been created.
This means you can't make changes to your published application until after
you clone the repo yourself.  See the git url below for more information.
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

Confirming application 'playdemo' is available:  Success!

playdemo published:  http://playdemo-yourlogin.rhcloud.com/
git url:  ssh://2e2d7a222f7c44d9a529f940363bdb7f@playdemo-yourlogin.rhcloud.com/~/git/playdemo.git/
Disclaimer: This is an experimental cartridge that provides a way to try unsupported languages, frameworks, and middleware on Openshift.
```

Copia la dirección del repositorio recién creado para agregarlo como un repositorio remoto y traerte el contenido:

```
git remote add origin ssh://2e2d7a222f7c44d9a529f940363bdb7f@playdemo-yourlogin.rhcloud.com/~/git/playdemo.git/
git pull -s recursive -X theirs origin master
git add .
git commit -m "initial deploy"
```

Ahora traeremos el contenido del quickstart a nuestro repositorio local

```
git remote add quickstart -m master git://github.com/opensas/play2-openshift-quickstart.git
git pull -s recursive -X theirs quickstart master
```

Ahora tan sólo tendremos que compilar nuestra aplicación de play y luego agregar los cambios y enviarlos a nuestro repositorio remoto en openshift:

```
play clean compile stage

git add .
git commit -m "mi primer deploy"
git push origin
```

Opcionalmente puedes ejecutar un script que agregramos para facilitar esta tarea:

```
openshift_deploy
```

Tu aplicación estará lista en playdemo-yourlogin.rhcloud.com

Y eso es todo. La primera vez tardará unos cuantos minutos porque hay que enviar todas las librerías de Play (aproximadamente 30 MB) pero luego tan śolo se enviarán las diferencias.

Puedes ir monitoreando los logs de tu aplicación con el siguiente comando:

```
rhc app tail -a playdemo -l yourlogin@openshift.com -p yoursecretpass
``` 

Para más información pueden consultar [este artículo](http://playlatam.wordpress.com/2012/05/01/soporte-nativo-para-play-framework-en-openshift-con-el-nuevo-tipo-de-aplicacion-diy/).

Saludo y happy hacking

sas

