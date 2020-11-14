package org.rosaenlg.lib;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

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

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * RosaeNLG compilation options helper.
 * <p>
 * <a href="https://rosaenlg.org/rosaenlg/1.18.1/advanced/params.html#_compiling_parameters">Compile options</a>
 * </p>
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class CompileInfo implements Cloneable {

  // private static final Logger logger = LoggerFactory.getLogger(CompileInfo.class);

  private String language;
  private String name;
  private Boolean compileDebug;
  private Boolean embedResources;
  private List<String> verbs;
  private List<String> adjectives;
  private List<String> words;


  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  /** Default constructor, does nothing.
   */
  public CompileInfo() {
  }

  
  /** Constructor from json parsed object.
   * 
   * <p>
   * Will find different properties in the json object and populate the object.
   * </p>
   * 
   * @param compileInfoJson json object containing the properties
   */
  public CompileInfo(JSONObject compileInfoJson) {
    
    if (compileInfoJson.has("language")) {
      this.language = compileInfoJson.getString("language");
    }
    if (compileInfoJson.has("name")) {
      this.name = compileInfoJson.getString("name");
    }
    if (compileInfoJson.has("compileDebug")) {
      this.compileDebug = compileInfoJson.getBoolean("compileDebug");
    }
    if (compileInfoJson.has("embedResources")) {
      this.embedResources = compileInfoJson.getBoolean("embedResources");
    }
    if (compileInfoJson.has("verbs")) {
      this.verbs = jsonArrayToStringArray(compileInfoJson.getJSONArray("verbs"));
    }
    if (compileInfoJson.has("adjectives")) {
      this.adjectives = jsonArrayToStringArray(compileInfoJson.getJSONArray("adjectives"));
    }
    if (compileInfoJson.has("words")) {
      this.words = jsonArrayToStringArray(compileInfoJson.getJSONArray("words"));
    }
  }

  private List<String> jsonArrayToStringArray(JSONArray arr) {
    List<String> res = new ArrayList<String>();
    for (int i = 0; i < arr.length(); i++) {
      res.add(arr.getString(i));
    }
    return res;
  }

  /**
   * Serializes the object to a JSON String.
   * 
   * @return String the object as a JSON String
   */
  public String toJson() {
    JSONObject res = new JSONObject();
    if (this.language != null) {
      res.put("language", this.language);
    }
    if (this.name != null) {
      res.put("name", this.name);
    }
    if (this.compileDebug != null) {
      res.put("compileDebug", this.compileDebug);
    }
    if (this.embedResources != null) {
      res.put("embedResources", this.embedResources);
    }
    if (this.verbs != null) {
      res.put("verbs", new JSONArray(this.verbs));
    }
    if (this.adjectives != null) {
      res.put("adjectives", new JSONArray(this.adjectives));
    }
    if (this.words != null) {
      res.put("words", new JSONArray(this.words));
    }
    return res.toString();
  }

  /**
   * Sets the language, for instance 'en_US' or 'fr_FR'.
   * 
   * @param language the language
   * @return this to set further options
   */
  public CompileInfo setLanguage(String language) {
    this.language = language;
    return this;
  }

  /**
   * Sets the name of the output function.
   * <p>
   * This is only useful for 'compileFileClient', 'compileClient' and 'compile'
   * methods.
   * </p>
   * 
   * @param name name of the output function
   * @return this to set further options
   */
  public CompileInfo setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Activates compile debug Pug option.
   * <p>
   * Is true by default in Pug.
   * </p>
   * 
   * @param compileDebug activates or not compileDebug
   * @return this to set further options
   */
  public CompileInfo setCompileDebug(Boolean compileDebug) {
    this.compileDebug = compileDebug;
    return this;
  }

  /**
   * Embed resources in the compiled function.
   * <p>
   * Useful for client-side rendering only.
   * </p>
   * 
   * @param embedResources true to embed resources
   * @return this to set further options
   */
  public CompileInfo setEmbedResources(Boolean embedResources) {
    this.embedResources = embedResources;
    return this;
  }

  /**
   * Set words to embed.
   * <p>
   * Useful for client-side rendering only.
   * </p>
   * 
   * @param words list of words to embed
   * @return this to set further options
   */
  public CompileInfo setWords(List<String> words) {
    this.words = words;
    return this;
  }

  /**
   * Set verbs to embed.
   * <p>
   * Useful for client-side rendering only.
   * </p>
   * 
   * @param verbs list of verbs to embed
   * @return this to set further options
   */
  public CompileInfo setVerbs(List<String> verbs) {
    this.verbs = verbs;
    return this;
  }

  /**
   * Set adjectives to embed.
   * <p>
   * Useful for client-side rendering only.
   * </p>
   * 
   * @param adjectives list of adjectives to embed
   * @return this to set further options
   */
  public CompileInfo setAdjectives(List<String> adjectives) {
    this.adjectives = adjectives;
    return this;
  }

  /**
   * Returns the language.
   * 
   * @return language
   */  
  public String getLanguage() {
    return this.language;
  }
}
