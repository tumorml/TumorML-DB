package org.tumorml.db.api

import org.basex.client.api.BaseXClient
import scala.collection.mutable.ListBuffer
import scala.xml.{XML, Elem}
import org.tumorml.db.TumorMLDbStack

/**
 * TumorML DB API service servlet.
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

class TumorMLDataService extends TumorMLDbStack {

  before() {
    contentType="application/xml"
  }

  // search across DB and return TumorML documents matching query
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
    // download single TumorML document identified by an ID
    val session = new BaseXClient("localhost", 1984, "admin", "admin")
    val queryDocsByTitle = "db:open('tumorml')//tumorml[data(./@id) = '" + params({"id"}) + "']"
    val docsByTitleResults = session.query(queryDocsByTitle)
    val docsByTitleResultsList = new ListBuffer[Elem]
    while (docsByTitleResults.more()) docsByTitleResultsList += XML.loadString(docsByTitleResults.next())
    docsByTitleResults.close()
    val output = {for (doc <- docsByTitleResultsList) yield doc} // should only be one document
    output
  }

  get("/metrics") {
    // get DB metrics
  }
  
}
