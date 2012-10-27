package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
//  def index = Action {
//    Ok(views.html.index("Your new application is ready."))
//  }
  
//  val cluster = new Cluster("host1,host2", OstrichStatsReceiver)
  
  def index = Action {
	  Ok("Hello world")
	}
}