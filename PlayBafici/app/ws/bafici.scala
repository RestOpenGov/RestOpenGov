package ws

import utils.Common.isNull
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.Play.application
import play.api.libs.ws.WS
import play.api.Logger

import java.net.URLEncoder

object Bafici {

  val fields: String = 
    "id,title,title_es,url_ticket,year,generes_list,cast,id_youtube,filepic1," +
    "prodteam,synopsis_es,synopsis_en,duration,director,updated_ts," + 
    "name_en,name_es,description_en,description_es"

  val endpoint = isNull(application.configuration.getString("bafici.endpoint"), "")  

  val defaultYear = isNull(application.configuration.getString("bafici.year"), "2012")

  private def getJson(url: String, body: String = ""): JsValue = {
    Logger.info("about to run: '%s'".format(url))

    if (body=="") {
      WS.url(url).get().await.get.json  
    } else {
      WS.url(url).post(body).await.get.json  
    }

  }

  // ej: b6f980d6-5070-48b7-aeea-41d945b34175-96
  // fj: b6f980d6-5070-48b7-aeea-41d945b34175-130
  // http://zenithsistemas.com:9200/gcba/bafici/b6f980d6-5070-48b7-aeea-41d945b34175-130
  def queryById(id: String): JsValue = {
    val url = endpoint + id + "?fields=" + fields
    getJson(url)
  }

  def queryByRandom(count: Long = 3, year: String = ""): JsValue = {

  val url = endpoint + "_search?" +
      "size=" + count.toString + 
      "&fields=" + fields

  val body = """
{
  "query": { 
    "custom_score": { 
      "script" : "random()*20",
      "query" : {
        "query_string": { 
          "query" : "_id:bafici%s-films-*"
        }
      }
    }
  },
  "sort": {
    "_score": { 
      "order":"desc"
    }
  }
}""".format(year.takeRight(2))

    (getJson(url,body) \ "hits" \ "hits").as[JsValue]

  }

  // endpoint: bafici.endpoint="http://zenithsistemas.com:9200/gcba/bafici/"
  // http://zenithsistemas.com:9200/gcba/bafici/_search?q=synopsis_es:'buenos%20aires'&from=1&size=4&fields=title
  def query(query: String = "", from: Long = 0, size: Int = 10, year: String = ""): JsValue = {

    val q = buildQuery(query, year)

    val url = endpoint + "_search?" +
      "from=" + from + "&size=" + size + 
      "&fields=" + fields + 
      (if (q != "") "&q=" + URLEncoder.encode(q, "UTF-8") else "")

    (getJson(url) \ "hits" \ "hits").as[JsValue]

  }

  def count(query: String = "", year: String = ""): Long = {
    val q = buildQuery(query, year)

    val url = endpoint + "_search?" +
      "from=0&size=1" + 
      "&fields=id" + fields + 
      (if (q != "") "&q=" + URLEncoder.encode(q, "UTF-8") else "")

    (getJson(url) \ "hits" \ "total").as[Long]
  }

  private def buildQuery(query: String = "", year: String = ""):String = {
    "" + (
      if (year!="") "_id:bafici%s-films-* AND ".format(year.takeRight(2)) else ""
    ) + (
      if (query!="") query  + " AND " else ""
    ).stripSuffix(" AND ")
  }

}