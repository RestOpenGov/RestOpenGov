package ws

import utils.Common.isNull
import play.api.libs.json.JsValue
import play.Play.application
import play.api.libs.ws.WS
import play.api.Logger

object Bafici {

  val fields: String = 
    "id,title,title_es,url_ticket,year,generes_list,cast,id_youtube,filepic1," +
    "prodteam,synopsis_es,synopsis_en,duration,director,updated_ts," + 
    "name_en,name_es,description_en,description_es"

  val endpoint = isNull(application.configuration.getString("bafici.endpoint"), "")  

  private def getJson(url: String): JsValue = {
    Logger.info("about to run: '%s'".format(url))
    WS.url(url).get().await.get.json
  }

  // ej: b6f980d6-5070-48b7-aeea-41d945b34175-96
  // fj: b6f980d6-5070-48b7-aeea-41d945b34175-130
  // http://zenithsistemas.com:9200/gcba/bafici/b6f980d6-5070-48b7-aeea-41d945b34175-130
  def queryById(id: String): JsValue = {
    val query = endpoint + id + "?fields=" + fields
    getJson(query)
  }

  // endpoint: bafici.endpoint="http://zenithsistemas.com:9200/gcba/bafici/"
  // http://zenithsistemas.com:9200/gcba/bafici/_search?q=synopsis_es:'buenos%20aires'&from=1&size=4&fields=title
  def query(query: String = "", from: Long = 0, size: Int = 10): JsValue = {

    val url = endpoint + "_search?" +
      "from=" + from + "&size=" + size + 
      "&fields=" + fields + 
      (if (query != "") "&q=" + query else "")

    (getJson(url) \ "hits" \ "hits").as[JsValue]

  }

}