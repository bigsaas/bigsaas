package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Akka
import com.twitter.cassie.Cluster
import com.twitter.finagle.stats.NullStatsReceiver
import com.twitter.cassie.codecs.Utf8Codec
import com.twitter.cassie.Column
import play.api.Play.current
import akka.dispatch.Promise
import play.api.libs.concurrent.AkkaPromise
import com.twitter.cassie.ReadConsistency
import com.twitter.cassie.WriteConsistency


object Person {
  def apply(firstName : String) = new Person(firstName, "???")
}

case class Person(firstName : String, lastName : String)

object Application extends Controller {
//  def index = Action {
//    Ok(views.html.index("Your new application is ready."))
//  }
  
  val p = Person("paul")
  val p2 = p.copy(lastName = "Sabou")
  
  val cluster = new Cluster("127.0.0.1", NullStatsReceiver)
  val keyspace = cluster.keyspace("BigSaasAdmin").connect()
  val name : Option[String] = Some("Hi")
  name match {
    case Some(s) => println(s)
    case None => println("No string")
  }
  for (n <- name) {
    
  }
  val s2 : Option[String] = name.map(_ + "!")
  
  val strings = keyspace.columnFamily("LocationInfo", Utf8Codec, Utf8Codec, Utf8Codec).
  consistency(ReadConsistency.One).
  consistency(WriteConsistency.Any)
  println(strings)
//  strings.insert("newstring", Column("colname", "colvalue"))

  def index = Action {
    val p = strings.getRow("Cookiesq")
    println(p)
    implicit val executionContext = Akka.system 
    val promise : Promise[String] = Promise[String]
    p.onFailure(t => t.printStackTrace())
    p.onSuccess(s => {println(s.size()); promise.success(s.toString)})
    Async {
      new AkkaPromise(promise.map(result => Ok("Result: " + result)))
    }
  }
}