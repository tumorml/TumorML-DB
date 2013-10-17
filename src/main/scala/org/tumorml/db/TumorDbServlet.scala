package org.tumorml.db

import org.scalatra._
import org.basex.client.api.BaseXClient
import scala.collection.mutable.ListBuffer
import scala.xml.{XML, Elem}

class TumorDbServlet extends TumorDbStack {

  before() {
    contentType="application/xml"
  }

  get("/search/:query") {
    val session = new BaseXClient("localhost", 1984, "admin", "admin")
    val queryDocsByTitle = "db:open('tumorml')//tumorml[./header/title contains text '" + params({"query"}) +
      "' using fuzzy]"
    val docsByTitleResults = session.query(queryDocsByTitle)
    val docsByTitleResultsList = new ListBuffer[Elem]
    while (docsByTitleResults.more()) docsByTitleResultsList += XML.loadString(docsByTitleResults.next())
    docsByTitleResults.close()
    val output = <success>
      {for (doc <- docsByTitleResultsList) yield doc}
    </success>
    output
  }

  get("/download/:id") {
    // download TumorML document identified by an ID
  }

  get("/metrics") {
    // get DB metrics
  }
  
}
