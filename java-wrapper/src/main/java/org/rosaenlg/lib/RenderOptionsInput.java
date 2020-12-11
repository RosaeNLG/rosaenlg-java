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
 * <a href="https://rosaenlg.org/rosaenlg/2.1.0/advanced/params.html#_rendering_parameters">Runtime options</a>
 * </p>
 * 
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class RenderOptionsInput {
  /**
   * input language
   */
  protected String language;
  /**
   * force random seed
   */
  protected Integer forceRandomSeed;
  /**
   * default synonym mode
   */
  protected String defaultSynoMode;
  /**
   * default among
   */
  protected Integer defaultAmong;
  /**
   * render debug
   */
  protected boolean renderDebug = false;

  /**
   * JSON key for language
   */
  protected static final String KEY_LANGUAGE = "language";
  /**
   * JSON key for force random seed
   */
  protected static final String KEY_FORCERANDOMSEED = "forceRandomSeed";
  /**
   * JSON key for default synonym mode
   */
  protected static final String KEY_DEFAULTSYNOMODE = "defaultSynoMode";
  /**
   * JSON key for default among
   */
  protected static final String KEY_DEFAULTAMONG = "defaultAmong";
  /**
   * JSON key for render debug
   */
  protected static final String KEY_RENDERDEBUG = "renderDebug";


  /**
   * Create an empty object.
   * 
   */
  public RenderOptionsInput() {
  }
    

  /**
   * Create from a JSON object by picking relevant parameters.
   * 
   * @param jsonOptions json options which can contain runtime parameters
   */
  public RenderOptionsInput(JSONObject jsonOptions) {

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
    if (jsonOptions.has(KEY_RENDERDEBUG)) {
      renderDebug = jsonOptions.getBoolean(KEY_RENDERDEBUG);
    }
  }

  /**
   * Serializes the object to a JSON String.
   * 
   * @return String the object as a JSON String
   */
  public String toJsonString() {
    return this.toJsonObj().toString();
  }

  /**
   * Serializes the object to a JSON object.
   * 
   * @return JSONObject the object as a JSON String
   */
  public JSONObject toJsonObj() {
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
    if (this.renderDebug) {
      res.put(KEY_RENDERDEBUG, this.renderDebug);
    }
    return res;
  }

  
  /**
   * sets the language, for instance 'en_US' or 'fr_FR'.
   * 
   * @param language the language
   * @return this to set further options
   */
  public RenderOptionsInput setLanguage(String language) {
    this.language = language;
    return this;
  }

  /**
   * sets forceRandomSeed param, which is the random seed.
   * 
   * @param forceRandomSeed the random seed
   * @return this to set further options
   */
  public RenderOptionsInput setForceRandomSeed(Integer forceRandomSeed) {
    this.forceRandomSeed = forceRandomSeed;
    return this;
  }

  /**
   * sets the defaultAmong value.
   * 
   * @param defaultAmong the default among value
   * @return this to set further options
   */
  public RenderOptionsInput setDefaultAmong(Integer defaultAmong) {
    this.defaultAmong = defaultAmong;
    return this;
  }

  /**
   * sets renderDebug value.
   * 
   * @param renderDebug the renderDebug value
   * @return this to set further options
   */
  public RenderOptionsInput setRenderDebug(boolean renderDebug) {
    this.renderDebug = renderDebug;
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
   * gets the forced random seed.
   * 
   * @return this to set further options
   */
  public Integer getForceRandomSeed() {
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

  /**
   * gets the renderDebug value.
   * 
   * @return renderDebug
   */
  public boolean getRenderDebug() {
    return this.renderDebug;
  }  
}
