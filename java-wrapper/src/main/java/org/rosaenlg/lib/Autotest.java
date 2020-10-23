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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Autotest {

  private boolean activate;
  private String jsonInput;
  private List<String> expected;

  
  /** Classic constructor.
   * 
   * @param activate is activated or not
   * @param jsonInput input for the text (String containing JSON)
   * @param expected the rendered result should contain each of those String
   */
  public Autotest(boolean activate, String jsonInput, List<String> expected) {
    this.activate = activate;
    this.jsonInput = jsonInput;
    this.expected = expected;
  }

  
  /** Constructor from a JSON snippet.
   * 
   * <p>Will try to get each parameter (activate, input, expected) from the JSON snippet.
   * 
   * @param jsonConf JSON snippet containing the parameters.
   */
  public Autotest(JSONObject jsonConf) {
    this.activate = jsonConf.getBoolean("activate");
    this.jsonInput = jsonConf.getJSONObject("input").toString();

    this.expected = new ArrayList<String>();
    JSONArray jsonExpected = jsonConf.getJSONArray("expected");
    for (int i = 0; i < jsonExpected.length(); i++) {
      this.expected.add(jsonExpected.getString(i));
    }
  }

  
  /** Getter on activate.
   * 
   * @return autotest is activated or not
   */
  public boolean getActivate() {
    return activate;
  }

  
  /** Getter on jsonInput.
   *  
   * @return the JSON input for the auto test
   */
  public String getJsonInput() {
    return jsonInput;
  }

  
  /** Getter on expected result.
   * 
   * @return the expected strings that should be in the rendered output
   */
  public List<String> getExpected() {
    return expected;
  }

}
