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