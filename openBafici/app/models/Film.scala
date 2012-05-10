package models

import formatters.FilmFormatter._

import ws.Bafici

case class Film(
  id: String            = "",
  title: String         = "no title",
  title_es: String      = "sin título",
  url_ticket: String    = "",
  year: Long            = 0,
  generes_list: String  = "ningún genero",
  cast: String          = "",
  id_youtube: String    = "",
  filepic1: String      = "",
  prodteam: String      = "",
  synopsis_es: String   = "sinopsis",
  synopsis_en: String   = "sinopsis",
  duration: Long        = 0,
  director: String      = "",
  updated_ts: String    = ""
)

object Film {
  
  // endpoint: bafici.endpoint="http://zenithsistemas.com:9200/gcba/bafici/"
  // http://zenithsistemas.com:9200/gcba/bafici/_search?q=synopsis_es:'buenos%20aires'&from=1&size=4&fields=title
  def findAll(query: String = "", page: Long = 1, size: Int = 10): Seq[Film] = {
    val from = ((page-1) * size) + 1
    Bafici.query(query, from, size).as[Seq[Film]]
  }

  // ej: b6f980d6-5070-48b7-aeea-41d945b34175-96
  // fj: b6f980d6-5070-48b7-aeea-41d945b34175-130
  def findById(id: String): Option[Film] = {
    return Bafici.queryById(id).asOpt[Film]
  }

}

