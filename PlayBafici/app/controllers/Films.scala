package controllers

import play.api._
import play.api.mvc._

import play.api.libs.ws.WS
import play.api.libs.json.Json

import models.Film

object Films extends Controller {

  val FILMS_PAGE_LEN = 10

  def home() = Action {
    Ok(views.html.index())
  }

  def filmsForGame(points: Int) = Action { request =>
    
    request.session.get("counter").map { counter =>  
      val films = Film.findByRandom();
      val index =  scala.util.Random.nextInt(films.size);
      val winnerFilm = films(index);

      val sum = counter.toInt + points 
      Ok(views.html.show(winnerFilm, sum, films)).withSession("counter" -> sum.toString) 
    
    }.getOrElse {
      val films = Film.findByRandom();
      val index =  scala.util.Random.nextInt(films.size);
      val winnerFilm = films(index);

      Ok(views.html.show(winnerFilm, 0, films)).withSession(
         "counter" -> "0"
      )
    }
  }

}