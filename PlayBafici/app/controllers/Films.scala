package controllers

import play.api._
import play.api.mvc._

import play.api.libs.ws.WS
import play.api.libs.json.Json

import play.api.data.Form
import play.api.data.Forms.number
import play.api.data.Forms.optional

import models.Film

import play.Logger

import scala.util.Random

object Films extends Controller {

  val FILMS_PAGE_LEN = 10

  def home() = Action {
    Ok(views.html.index())
  }

  def filmsForGame() = Action { implicit request =>
    
    val points = Form("points" -> optional(number)).bindFromRequest.get.getOrElse(0)

    val score = request.session.get("score").map { score =>
      score.toInt + points 
    }.getOrElse(points)

    val films = Film.findByRandom();
    val winnerFilm = films(Random.nextInt(films.size));

    Ok(views.html.show(winnerFilm, score, films)).withSession("score" -> score.toString) 

  }

}