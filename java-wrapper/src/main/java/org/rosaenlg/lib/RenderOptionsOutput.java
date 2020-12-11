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
 * RosaeNLG runtime options in the output helper.
 * <p>
 * <a href="https://rosaenlg.org/rosaenlg/2.1.0/advanced/params.html#_rendering_parameters">Runtime options</a>
 * </p>
 * 
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class RenderOptionsOutput extends RenderOptionsInput {
  private int randomSeed;

  private static final String KEY_RANDOMSEED = "randomSeed";

  /**
   * Create an empty object.
   * 
   */
  public RenderOptionsOutput() {
  }

  /**
   * Create from a JSON object by picking relevant parameters.
   * 
   * @param jsonOptions json options which can contain input runtime parameters
   */
  public RenderOptionsOutput(JSONObject jsonOptions) {
    super(jsonOptions);
  }

  /**
   * Completes existing object by picking additionnal output parameters.
   * 
   * @param jsonOptions json options which can contain output parameters
   */
  public void completeRenderOptionsOutput(JSONObject jsonOptions) {
    if (jsonOptions.has(KEY_RANDOMSEED)) {
      randomSeed = jsonOptions.getInt(KEY_RANDOMSEED);
    }
  }

  /**
   * Serializes the object to a JSON object.
   * 
   * @return JSONObject the object as a JSON String
   */
  @Override
  public JSONObject toJsonObj() {
    JSONObject res = super.toJsonObj();
    if (this.randomSeed > 0) {
      res.put(KEY_RANDOMSEED, this.randomSeed);
    }
    return res;
  }

  /**
   * sets random seed param.
   * 
   * @param randomSeed the random seed
   * @return this to set further options
   */
  public RenderOptionsOutput setRandomSeed(int randomSeed) {
    this.randomSeed = randomSeed;
    return this;
  }

  /**
   * gets the random seed.
   * 
   * @return this to set further options
   */
  public int getRandomSeed() {
    return this.randomSeed;
  }


}
