package org.rosaenlg.lib;

/*-
 * #%L
 * org.rosaenlg:java-server
 * %%
 * Copyright (C) 2019 RosaeNLG.org, Ludan Stoecklé
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestJsonPackage {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(TestJsonPackage.class);

  private String readInRepo(String templateId) throws Exception {
    return new String(Files.readAllBytes(Paths.get("test-templates-repo", templateId + ".json")), StandardCharsets.UTF_8);
  }

  @Test
  void testNormal() throws Exception {
    String jsonPackageString = this.readInRepo("chanson");

    JsonPackage jsonPackage = new JsonPackage(jsonPackageString);

    assertEquals("chanson", jsonPackage.getTemplateId());
    assertNotNull(jsonPackage.getSrc().getAutotest());
    assertEquals(jsonPackageString, jsonPackage.getInitialPackage());
  }

  @Test
  void testNoAutotest() throws Exception {
    String jsonPackageString = this.readInRepo("chanson");

    JSONObject parsed = new JSONObject(jsonPackageString);
    parsed.getJSONObject("src").remove("autotest");

    JsonPackage jsonPackage = new JsonPackage(parsed.toString());

    assertNull(jsonPackage.getSrc().getAutotest());
  }


}
