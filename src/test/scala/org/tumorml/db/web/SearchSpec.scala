package org.tumorml.db.web

import org.scalatra.test.scalatest._
import org.scalatest.FunSuite

class SearchSpec extends ScalatraFunSuite {
  addServlet(classOf[Search], "/*")

  // this test checks the get on root returns a webpage with a title and some html of a form as below
  test("get on root") {
    get("/") {
      response.mediaType.get should be ("text/html")
      status should equal (200)
      body should include (
        <title>TumorML Database Search</title>.toString
      )
      body should include (
        <img src="/images/tumorml-banner.png"/>.toString
      )
    }
  }

}
