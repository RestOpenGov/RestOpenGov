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

  def list(page: Long = 1, sort: String = "", filter: String = "", year: String = "2012") = Action {
    val films = Film.findAll(query = filter, year = year, page = page, size = FILMS_PAGE_LEN)
    Ok(views.html.list(
      page, sort, filter, year, 
      Film.count(filter, year),     // found
      Film.count(year=""),          // total
      films)
    )
  }

  def show(id: String) = Action {
    //2732fbf4-4e55-4794-8e98-e5d5fa6a0419-4
    Film.findById(id).map { film: Film =>
      Ok(views.html.show(film))
    }.getOrElse {
      NotFound
    }

  }

  def test() = Action {
    Ok(views.html.test(Film.findAll()))
  }

}