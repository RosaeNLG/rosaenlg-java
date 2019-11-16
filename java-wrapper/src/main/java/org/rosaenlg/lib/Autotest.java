package org.rosaenlg.lib;

/*-
 * #%L
 * org.rosaenlg:java-server
 * %%
 * Copyright (C) 2019 RosaeNLG.org, Ludan Stoecklé
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
