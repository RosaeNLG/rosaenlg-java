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

public class TestRenderOptionsOutput {

  @Test
  void testRandomSeed() throws Exception {
    RenderOptionsOutput roo = new RenderOptionsOutput();
    roo.setRandomSeed(42);
    assertEquals(42, roo.getRandomSeed());
  }

  @Test
  void testJsonWithoutRandomSeed() throws Exception {
    RenderOptionsOutput roo = new RenderOptionsOutput();
    roo.completeRenderOptionsOutput(new JSONObject());
    assert(!roo.toJsonObj().has("randomKey"));
  }


}
