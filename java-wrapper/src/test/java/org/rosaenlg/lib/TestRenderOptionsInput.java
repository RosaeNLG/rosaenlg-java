package org.rosaenlg.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*-
 * #%L
 * RosaeNLG for Java
 * %%
 * Copyright (C) 2020 RosaeNLG.org, Ludan Stoecklé
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

// import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

public class TestRenderOptionsInput {

  @Test
  void testEmptyConstructor() throws Exception {
    RenderOptionsInput roi = new RenderOptionsInput();
    roi.setLanguage("fr_FR");
    roi.setDefaultSynoMode("sequence");
    roi.setDefaultAmong(3);
    roi.setForceRandomSeed(42);
    roi.setRenderDebug(true);

    JSONObject json = roi.toJsonObj();

    assertEquals("sequence", json.get("defaultSynoMode"));
    assertEquals(3, json.get("defaultAmong"));
    assertEquals("fr_FR", json.get("language"));
    assertEquals(42, json.get("forceRandomSeed"));
    assertEquals(true, json.get("renderDebug"));
  }

  @Test
  void testJsonConstructor() throws Exception {
    JSONObject json = new JSONObject();
    json.put("language", "fr_FR");
    json.put("defaultSynoMode", "sequence");
    json.put("defaultAmong", 3);
    json.put("renderDebug", true);

    RenderOptionsInput roi = new RenderOptionsInput(json);

    assertEquals("fr_FR", roi.getLanguage());
    assertEquals("sequence", roi.getDefaultSynoMode());
    assertEquals(3, roi.getDefaultAmong());
    assertEquals(true, roi.getRenderDebug());
  }


}
