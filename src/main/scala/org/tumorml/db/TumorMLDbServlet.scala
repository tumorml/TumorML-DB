package org.tumorml.db

import org.basex.client.api.BaseXClient
import scala.collection.mutable.ListBuffer
import scala.xml.{XML, Elem}
import javax.xml.parsers._
import javax.xml.transform._
import javax.xml.transform.dom._
import javax.xml.transform.stream._
import java.io._
import java.net.URL

/**
 *
 * Copyright 2013 David Johnson.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

class TumorMLDbServlet extends TumorMLDbStack {

  before() {
    contentType="text/html"
  }

  // search across DB and return TumorML documents matching query
  get ("/search") {
    if (params.get("q")==None)
      redirect("/tumorml")
    else
      redirect("/tumorml/search/" + params({"q"}))
  }

  // search across DB and return TumorML documents matching query
  get("/search/:query") {
    val session = new BaseXClient("localhost", 1984, "admin", "admin")
    val queryDocsByTitle = "db:open('tumorml')//tumorml[./header/title contains text '" + params({"query"}) +
      "' using fuzzy]"
    val docsByTitleResults = session.query(queryDocsByTitle)
    val resultsList = new ListBuffer[Elem]

    // transform docs into HTML to render using stylesheet
    val factory = TransformerFactory.newInstance()
    val stylesheet = getClass.getClassLoader.getResource("tumorDocToHtml.xsl")
    val template = factory.newTemplates(new StreamSource(new FileInputStream(stylesheet.getPath)))
    val xformer = template.newTransformer()
    while (docsByTitleResults.more()) {
      val source = new StreamSource(new ByteArrayInputStream(docsByTitleResults.next.getBytes))
      val out = new ByteArrayOutputStream()
      val result = new StreamResult(out)
      xformer.transform(source, result)
      resultsList+=XML.loadString(new String(out.toByteArray))
    }

    // generate the output XML from searchResultsList
    var output =
      <html>
        <head>
          <title>{params({"query"})} - TumorML Database Search</title>
          <meta name="ROBOTS" content="NOINDEX, NOFOLLOW" />
          <link rel="stylesheet" type="text/css" href="../../css/style.css" />
        </head>
        <body class="tftextresults">
          <form action="/tumorml/search" method="GET">
            <a href="/tumorml/"><img src="../../images/tumorml-logo-small.png" width="50" height="50" /></a><input type="text" class="tftextinput" name="q" value={"\"" + params({"query"}) + "\""} onclick="this.value='';" /><input type="submit" value="Search models" class="tfbutton" />
          </form>
          <p>You searched for <strong>{params({"query"})}</strong></p>
          <p>No results.</p>
        </body>
      </html>

    if (resultsList.size>0) {
      output =
        <html>
          <head>
            <title>{params({"query"})} - TumorML Database Search</title>
            <meta name="ROBOTS" content="NOINDEX, NOFOLLOW" />
            <link rel="stylesheet" type="text/css" href="../../css/style.css" />
          </head>
          <body class="tftextresults">
            <form action="/tumorml/search" method="GET">
              <a href="/tumorml/"><img src="../../images/tumorml-logo-small.png" width="50" height="50" /></a><input type="text" class="tftextinput" name="q" value={"\"" + params({"query"}) + "\""} onclick="this.value='';" /><input type="submit" value="Search models" class="tfbutton" />
            </form>
            <p>You searched for <strong>{params({"query"})}</strong></p>
            <ol>
              {for (searchResult <- resultsList.distinct) yield searchResult}
            </ol>
          </body>
        </html>
    }
    session.close()
    output
  }

  // if landed at root, redirect to user search page
  get("/") {
    <html>
      <head>
        <title>TumorML Database Search</title>
        <meta name="ROBOTS" content="NOINDEX, NOFOLLOW" />
        <link rel="stylesheet" type="text/css" href="../css/style.css" />
      </head>
      <body>
        <div align="center">
          <img src="../images/tumorml-banner.png" />
          <form action="/tumorml/search" method="GET">
            <input type="text" class="tftextinput" name="q" /><input type="submit" value="Search models" class="tfbutton" />
          </form>
        </div>
      </body>
    </html>
  }
}
