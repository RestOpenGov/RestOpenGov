package formatters

import play.api.libs.json.Format
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import models.Film

object FilmFormatter {

  implicit object JsonFilmFormatter extends Format[Film] {

   def writes(o: Film): JsValue = {
      toJson( Map(
        "Error" -> toJson("Not implemented")
      ))
    }

    def reads(json: JsValue): Film = {

      val fields = json \ "fields"

      Film(
        id                  = (json \   "_id").asOpt[String]          .getOrElse(""),

        title               = (fields \ "title").asOpt[String].getOrElse(
                              (fields \ "name_en").asOpt[String].getOrElse("no title")),
        
        title_es            = (fields \ "title_es").asOpt[String].getOrElse(
                              (fields \ "name_es").asOpt[String].getOrElse("sin título")),

        url_ticket          = (fields \ "url_ticket").asOpt[String]   .getOrElse(""),
        year                = (fields \ "year").asOpt[Long]           .getOrElse(0),
        generes_list        = (fields \ "generes_list").asOpt[String] .getOrElse("sin género"),
        cast                = (fields \ "cast").asOpt[String]         .getOrElse(""),
        id_youtube          = (fields \ "id_youtube").asOpt[String]   .getOrElse(""),
        filepic1            = (fields \ "filepic1").asOpt[String]     .getOrElse(""),
        prodteam            = (fields \ "prodteam").asOpt[String]     .getOrElse(""),

        synopsis_es         = (fields \ "synopsis_es").asOpt[String].getOrElse(
                              (fields \ "description_es").asOpt[String].getOrElse("")),

        synopsis_en         = (fields \ "synopsis_en").asOpt[String].getOrElse(
                              (fields \ "description_en").asOpt[String].getOrElse("")),

        duration            = (fields \ "duration").asOpt[Long]       .getOrElse(0),
        director            = (fields \ "director").asOpt[String]     .getOrElse(""),
        updated_ts          = (fields \ "updated_ts").asOpt[String]   .getOrElse("")
      )

    }

  }

}