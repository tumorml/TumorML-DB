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

class TumorDbServlet extends TumorDbStack {

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
    contentType="application/xml"
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



  // if landed at root, redirect to user search page
  get("/") {
    <html>
      <head>
        <title>TumorML DB Seach</title>
        <meta name="ROBOTS" content="NOINDEX, NOFOLLOW" />
        <link rel="stylesheet" type="text/css" href="../css/style.css" />
      </head>
      <body>
        <div align="center">
          <h1>TumorML Search</h1>
          <form action="/tumorml/search" method="GET">
            <input type="text" class="tftextinput" name="q" /><input type="submit" value="Search terms" class="tfbutton" />
          </form>
        </div>
      </body>
    </html>
  }
}
