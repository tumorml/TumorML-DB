package org.tumorml.db

import org.basex.client.api.BaseXClient
import scala.collection.mutable.ListBuffer
import scala.xml.{XML, Elem}

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

  // search - looks in code lists, enumerated items and external code IDs
  // search across DB and return TumorML documents matching query
  get ("/search") {
    if (params.get("q")==None)
      redirect("/tumorml")
    else
      redirect("/tumorml/search/" + params({"q"}))
  }

  get("/search/:query") {
    val session = new BaseXClient("localhost", 1984, "admin", "admin")
//    val queryDocsByTitle = "db:open('tumorml')//tumorml[./header/title contains text '" + params({"query"}) +
//      "' using fuzzy]"
    val queryDocsByTitle = "for $model in db:open('tumorml')//tumorml where $model/header/title contains text '" +
      params({"query"}) + "' using fuzzy " +
      "let $title := $model/header/title/text() " +
      "let $creator := $model/header/creator/person/fullname/text() " +
      "let $publisher := $model/header/publisher/person/fullname/text() " +
      "let $datepublished := $model/header/date/text() " +
      "let $math := $model/header/math/text() " +
      "let $biocomplexityDirection := $model/header/biocomplexityDirection/text() " +
      "let $cancer := $model/header/cancer/text() " +
      "let $materialization := $model/header/materialization/text() " +
      "let $homogeneity := $model/header/homogeneity/text() " +
      "let $imageBasedDetectability := $model/header/imageBasedDetectability/text() " +
      "let $freeGrowth := $model/header/freeGrowth/text() " +
      "let $treatmentIncluded := $model/header/treatmentIncluded/text() " +
      "return <li><article>" +
        "<h1>Title: {$title}</h1>" +
        "<p>Creator: {$creator}</p>" +
        "<p>Publisher: {$publisher}</p>" +
        "<p>Date published: {$datepublished}</p>" +
        "<p>Math type: {$math}</p>" +
        "<p>Biocomplexity direction: {$biocomplexityDirection}</p>" +
        "<p>Cancer simulated: {$cancer}</p>" +
        "<p>Materialization: {$materialization}</p>" +
        "<p>Tumor homogeneity: {$homogeneity}</p>" +
        "<p>Image-based detectability: {$imageBasedDetectability}</p>" +
        "<p>Free growth simulated?: {$freeGrowth}</p>" +
        "<p>Treatment included: {$treatmentIncluded}</p>" +
        "</article></li>"
    val docsByTitleResults = session.query(queryDocsByTitle)
    val resultsList = new ListBuffer[Elem]
    while (docsByTitleResults.more()) resultsList += XML.loadString(docsByTitleResults.next())
    docsByTitleResults.close()

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
