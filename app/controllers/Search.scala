package controllers

//import play.api._
import play.api.mvc._
//import play.api.db.{DBApi, DefaultDBApi}
// import play.api.Play.current
// import virtuoso.jdbc4.VirtuosoRdfBox
//import virtuoso.jdbc4.VirtuosoExtendedString

import javax.inject.Inject

class Search @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /*
  def index = Action { implicit request =>
    var outString = "Number is"
    var retSeq: Seq[User] = Seq()
    var inputMap: Map[String, String] = Map()
    var oformat = ""
    request.body.asFormUrlEncoded match {
      case Some(params) => {
        DefaultDBApi.withConnection { conn =>
          var gaiyo: String = params("abstract")(0)
          var header1: String = params("header1")(0)
          var header2: String = params("header2")(0)
          var ttitle: String = params("t-title")(0)
          var city: String = params("city")(0)
          params.get("csv") match {
            case Some(oform) => oformat = oform(0)
            case None => {}
          }
          inputMap = Map("abstract" -> gaiyo,
            "header1" -> header1,
            "header2" -> header2,
            "t-title" -> ttitle,
            "city" -> city
          )
//          Logger.debug("Abstract: " + params("abstract")(0))
          val stmt = conn.createStatement
          var query = new StringBuffer
          query.append("SPARQL ")
          query.append("prefix estat: <http://mashu.iias.flab.fujitsu.co.jp/data/extracted_htdocs/eStat/estat-schema#>")
          query.append("select ?abstract ?csv ?rowid ?colid ?header1 ?header2 ?value ?title where { ")
          query.append("?tableInfo estat:tablePath ?csv . ")
          query.append("?tableInfo estat:abstract ?abstract . ")
          query.append("?tableInfo estat:tableOrigPath ?origPath . ")
          query.append("?origPath estat:title ?title . ")
          query.append(" ?cell estat:tableInfo ?tableInfo . ")
          query.append(" ?cell estat:colHeader ?col . ")
          query.append(" ?col estat:id ?colid . ")
          query.append(" ?col estat:headerNameShort ?header1 . ")
          query.append(" ?cell estat:rowHeader ?row . ")
          query.append(" ?row estat:id ?rowid . ")
          query.append(" ?row estat:headerNameShort ?header2 . ")
          query.append(" ?cell estat:value ?value . ")
          if (ttitle != "") {
            query.append("FILTER CONTAINS(?title, \"" + ttitle + "\")")
          }
          if (city == "city") {
            query.append("FILTER (REGEX(?header2, \"市$\") || REGEX(?header2, \"町$\") || REGEX(?header2, \"村$\"))")
          } else if (city == "pref") {
            query.append("FILTER (REGEX(?header2, \"都$\") || REGEX(?header2, \"道$\") || REGEX(?header2, \"府$\") || REGEX(?header2, \"県$\"))")
          }
          if (gaiyo != "") {
            query.append("?abstract bif:contains '\"" + gaiyo + "\"' .")
          }
          if (header1 != "") {
            query.append("?header1 bif:contains '\"" + header1 + "\"' .")
          }
          if (header2 != "") {
            query.append("?header2 bif:contains '\"" + header2 + "\"' .")
          }
          query.append("} limit 100 ")
          Logger.debug("Query: " + query.toString())
          val rs = stmt.executeQuery(query.toString())
          while (rs.next()) {
            val o = rs.getObject("abstract")
            val t = rs.getObject("title")
            var p = rs.getObject("csv").toString
            var header1 = rs.getObject("header1").toString
            var header2 = rs.getObject("header2").toString
            var rowid = rs.getObject("rowid").toString
            var colid = rs.getObject("colid").toString
            var valid = rs.getObject("value").toString
            p = p.replace("/disk/kpf01/public_dir/data/extracted_htdocs",
              "http://10.25.209.59/htdocs")
            o match {
              case _: VirtuosoRdfBox =>
                outString += o.asInstanceOf[VirtuosoRdfBox].getType()
              case _: VirtuosoExtendedString =>
                outString += o.asInstanceOf[VirtuosoExtendedString].iriType
            }
            outString += "(" + o + ")"
            retSeq = retSeq :+ User(t.toString(), o.toString(), p, header1, header2, rowid, colid, valid)
          }
        }
      }
      case None => {
      }
    }
//    Logger.debug("inputMap: " + inputMap.toString())
    if (oformat != "") {
      val csv = new StringBuffer
      for (items <- retSeq) {
        csv.append(items.title + "," + items.name + "," + items.key1 +
          "," + items.key2 + "," + items.valid + "\n")
      }
      Ok(csv.toString()).as("text/csv").withHeaders("Content-Disposition" -> "attachment; filename=xxxx.csv")
    } else {
      Ok(views.html.search("Search", retSeq, inputMap))
    }
  }
*/
  def index = Action {
    Ok("It works!")
  }
}
