package controllers

import play.api._
import play.api.mvc._

import play.api.libs.ws.WS
import play.api.libs.json.Json

import models.Film

object FilmsRestBA extends Controller {

  def home() = Action {
    Ok(views.html.index())
  }

  def list(page: Long = 1, sort: String = "", filter: String = "", year: String = "") = Action {

    Ok(views.html.list(page, sort, filter, year, 0, 0, Seq[Film]()))
    
    val client = new ar.com.restba.DefaultRestBAClient("http://zenithsistemas.com:9200")
    val connection = client.fetchConnectionRestBaAsJson(filter, page)
    println(connection.getMaxPages())
    val firstPageIterator = connection.iterator()
    
    val firstPage = firstPageIterator.next().iterator()
    
    var l = List[Film]()
    
    while(firstPage.hasNext()) {
      val item = firstPage.next() 
      println("JSON: " + item)
      try {
      val film = Film(

	      item.getString("id"),
		  item.getString("title"),
		  item.getString("title_es"),
		  item.getString("url_ticket"),
		  item.getLong("year"),
		  item.getString("generes_list"), 
		  item.getString("cast"),
		  item.getString("id_youtube"), 
		  item.getString("filepic1"), 
		  item.getString("prodteam"), 
		  item.getString("synopsis_es"),
		  item.getString("synopsis_en"),
		  item.getLong("duration"),
		  item.getString("director"), 
		  item.getString("updated_ts")
      )
      
      l =  film :: l
       } catch { 
       case e:Exception => println("json apping error.")
       } 
      
    }
        
    Ok(views.html.list(page, sort, filter, year, 0, 0, l.asInstanceOf[Seq[Film]] ))
  }

  def show(id: String) = Action {
    val film = Film("1", "La banda del openBafici")
    Ok(views.html.show(film))
  }

}
