package org.tumorml.db

import org.scalatra.test.specs2._
import org.tumorml.db.api.TumorMLDataService

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class TumorDbServletSpec extends ScalatraSpec { def is =
  "GET / on TumorDbServlet"                     ^
    "should return status 200"                  ! root200^
                                                end

  addServlet(classOf[TumorMLDbServlet], "/*")

  def root200 = get("/") {
    status must_== 200
  }
}
