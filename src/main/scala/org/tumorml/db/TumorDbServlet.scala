package org.tumorml.db

import org.scalatra._
import scalate.ScalateSupport

class TumorDbServlet extends TumorDbStack {

  get("/search/:query") {
    contentType="application/xml"
  }

  get("/download/:id") {
    // download TumorML document identified by an ID
    contentType="application/xml"
  }

  get("/metrics") {
    // get DB metrics
    contentType="application/xml"
  }
  
}
