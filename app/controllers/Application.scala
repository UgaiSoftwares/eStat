package controllers

import javax.inject.Inject

import play.api.mvc._

class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action {
//    Ok("It works!")
    Ok(views.html.index("Your new application is ready."))
  }
}
// object Application extends Controller {

//  def index = Action {
//    Ok(views.html.index("Your new application is ready."))
//  }

//}
