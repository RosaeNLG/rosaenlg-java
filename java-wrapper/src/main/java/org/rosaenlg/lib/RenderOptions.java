package org.rosaenlg.lib;

/*-
 * #%L
 * RosaeNLG for Java
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

import org.json.JSONObject;

/**
 * RosaeNLG runtime options helper.
 * <p>
 * <a href="https://rosaenlg.org/rosaenlg/1.5.0/advanced/params.html#_rendering_parameters">Runtime options</a>
 * </p>
 * 
 * @author Ludan Stoecklé ludan.stoeckle@rosaenlg.org
 */
public class RenderOptions {
  private String language;
  private Integer forceRandomSeed;
  private String defaultSynoMode;
  private Integer defaultAmong;

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

    language = jsonOptions.getString("language");
    if (jsonOptions.has("forceRandomSeed")) {
      forceRandomSeed = jsonOptions.getInt("forceRandomSeed");
    }
    if (jsonOptions.has("defaultSynoMode")) {
      defaultSynoMode = jsonOptions.getString("defaultSynoMode");
    }
    if (jsonOptions.has("defaultAmong")) {
      defaultAmong = jsonOptions.getInt("defaultAmong");
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
    res.put("language", this.language);

    if (this.forceRandomSeed != null) {
      res.put("forceRandomSeed", this.forceRandomSeed);
    }
    if (this.defaultSynoMode != null) {
      res.put("defaultSynoMode", this.defaultSynoMode);
    }
    if (this.defaultAmong != null) {
      res.put("defaultAmong", this.defaultAmong);
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
