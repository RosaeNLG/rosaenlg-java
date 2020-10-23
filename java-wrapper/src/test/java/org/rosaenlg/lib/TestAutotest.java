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

import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAutotest {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(TestAutotest.class);

  @Test
  public void test() throws Exception {
    String json = "{\"activate\":true," 
        + "\"input\":{\"language\":\"en_US\"},"
        + "\"expected\":[\"Bla\",\"included\"]}";

    Autotest at = new Autotest(new JSONObject(json));

    assertEquals(at.getActivate(), true);
    assertEquals(at.getJsonInput(), "{\"language\":\"en_US\"}");

    assertEquals(at.getExpected().size(), 2);

  }

}
