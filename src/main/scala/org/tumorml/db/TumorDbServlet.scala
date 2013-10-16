package org.tumorml.db

import org.scalatra._
import scalate.ScalateSupport

class TumorDbServlet extends TumorDbStack {

  get("/search/:query") {
    contentType="application/xml"
  }

  get("/download/:lsid") {
    // download TumorML document identified by LSID
    contentType="application/xml"
    // echo lsid back
    <lsid>{params({"lsid"})}</lsid>
  }

  get("/metrics") {
    // get DB metrics
    contentType="application/xml"
  }
  
}
