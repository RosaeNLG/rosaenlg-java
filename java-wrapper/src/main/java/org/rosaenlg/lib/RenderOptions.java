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
 * RosaeNLG runtime options helper.
 * <p>
 * <a href="https://rosaenlg.org/rosaenlg/1.5.0/advanced/params.html#_rendering_parameters">Runtime options</a>
 * </p>
 * 
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class RenderOptions {
  private String language;
  private Integer forceRandomSeed;
  private String defaultSynoMode;
  private Integer defaultAmong;

  private static final String KEY_LANGUAGE = "language";
  private static final String KEY_FORCERANDOMSEED = "forceRandomSeed";
  private static final String KEY_DEFAULTSYNOMODE = "defaultSynoMode";
  private static final String KEY_DEFAULTAMONG = "defaultAmong";


  /**
   * Create an empty object.
   * 
   */
  public RenderOptions() {
  }
    

  /**
   * Create from a JSON object by picking relevant parameters.
   * 
   * @param jsonOptions json options which can contain runtime parameters
   */
  public RenderOptions(JSONObject jsonOptions) {

    language = jsonOptions.getString(KEY_LANGUAGE);
    if (jsonOptions.has(KEY_FORCERANDOMSEED)) {
      forceRandomSeed = jsonOptions.getInt(KEY_FORCERANDOMSEED);
    }
    if (jsonOptions.has(KEY_DEFAULTSYNOMODE)) {
      defaultSynoMode = jsonOptions.getString(KEY_DEFAULTSYNOMODE);
    }
    if (jsonOptions.has(KEY_DEFAULTAMONG)) {
      defaultAmong = jsonOptions.getInt(KEY_DEFAULTAMONG);
    }
  }


  /**
   * Serializes the object to a JSON String.
   * 
   * @return String the object as a JSON String
   */
  public String toJson() {
    JSONObject res = new JSONObject();
    // must not be null
    res.put(KEY_LANGUAGE, this.language);

    if (this.forceRandomSeed != null) {
      res.put(KEY_FORCERANDOMSEED, this.forceRandomSeed);
    }
    if (this.defaultSynoMode != null) {
      res.put(KEY_DEFAULTSYNOMODE, this.defaultSynoMode);
    }
    if (this.defaultAmong != null) {
      res.put(KEY_DEFAULTAMONG, this.defaultAmong);
    }
    return res.toString();
  }

  /**
   * sets the language, for instance 'en_US' or 'fr_FR'.
   * 
   * @param language the language
   * @return this to set further options
   */
  public RenderOptions setLanguage(String language) {
    this.language = language;
    return this;
  }

  /**
   * sets forceRandomSeed param, which is the random seed.
   * 
   * @param forceRandomSeed the random seed
   * @return this to set further options
   */
  public RenderOptions setForceRandomSeed(Integer forceRandomSeed) {
    this.forceRandomSeed = forceRandomSeed;
    return this;
  }

  /**
   * sets the defaultAmong value.
   * 
   * @param defaultAmong the default among value
   * @return this to set further options
   */
  public RenderOptions setDefaultAmong(Integer defaultAmong) {
    this.defaultAmong = defaultAmong;
    return this;
  }


  /**
   * gets the language.
   * 
   * @return the language
   */
  public String getLanguage() {
    return this.language;
  }

  /**
   * gets the random seed.
   * 
   * @return this to set further options
   */
  public Integer getRandomSeed() {
    return this.forceRandomSeed;
  }

  /**
   * gets the default synonym mode.
   * 
   * @return defaultSynoMode
   */
  public String getDefaultSynoMode() {
    return this.defaultSynoMode;
  }

  /**
   * gets the defaultAmong value.
   * 
   * @return defaultAmong
   */
  public Integer getDefaultAmong() {
    return this.defaultAmong;
  }  
}
