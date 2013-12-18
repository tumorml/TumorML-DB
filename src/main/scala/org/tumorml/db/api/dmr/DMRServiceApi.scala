package org.tumorml.db.api.dmr

import org.tumorml.db.WebStack

/**
 * DMR Service servlet. Implements CViT/caBIG DMR API as documented in
 * MGH_DMR-UserGuide-1_0_0.doc
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

class DMRServiceApi extends WebStack {

  before() {
    contentType = "application/xml"
  }

  get("/addEntry") {
    // unimplemented in demonstrator
  }

  get("/addDataToEntry") {
    // unimplemented in demonstrator
  }

  get("/addReferenceToEntry") {
    // unimplemented in demonstrator
  }

  get("/updateEntry") {
    // unimplemented in demonstrator
  }

  get("/updateData") {
    // unimplemented in demonstrator
  }

  get("/updateReference") {
    // unimplemented in demonstrator
  }

  get("/query") {

  }

}
