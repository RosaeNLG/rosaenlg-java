package org.rosaenlg.lib;

/*-
 * #%L
 * RosaeNLG for Java
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

import org.json.JSONObject;

/**
 * RosaeNLG render result holder.
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class RenderResult {

  private String text;
  private String outputData;
  
  /** Constructor using a JSON string.
   * 
   * @param jsonString the string containing the JSON package
   */
  public RenderResult(String jsonString) {
    JSONObject jsonObj = new JSONObject(jsonString);
    this.text = jsonObj.getString("text");
    this.outputData = jsonObj.getJSONObject("outputData").toString();
  }

  /**
   * Returns the text.
   * 
   * @return text
   */  
  public String getText() {
    return this.text;
  }

  /**
   * Returns the output data as JSON string.
   * 
   * @return outputData
   */  
  public String getOutputData() {
    return this.outputData;
  }
}
