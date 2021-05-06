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

/**
 * RosaeNLG compilation options helper.
 * <p>
 * <a href="https://rosaenlg.org/rosaenlg/1.18.1/advanced/params.html#_compiling_parameters">Compile options</a>
 * </p>
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class CompileInfo {

  private static final String KEY_NAME = "name";
  private static final String KEY_LANGUAGE = "language";
  private static final String KEY_COMPILEDEBUG = "compileDebug";
  private static final String KEY_EMBEDRESOURCES = "embedResources";
  private static final String KEY_VERBS = "verbs";
  private static final String KEY_ADJECTIVES = "adjectives";
  private static final String KEY_WORDS = "words";

  private String language;
  private String name;
  private Boolean compileDebug;
  private Boolean embedResources;
  private List<String> verbs;
  private List<String> adjectives;
  private List<String> words;

  /** Default constructor, does nothing.
   */
  public CompileInfo() {
  }

  /** Constructor that clones an existing CompileInfo object.
   * 
   * @param compileInfo original to clone
   */
  public CompileInfo(CompileInfo compileInfo) {
    this.language = compileInfo.language;
    this.name = compileInfo.name;
    this.compileDebug = compileInfo.compileDebug;
    this.embedResources = compileInfo.embedResources;
    if (compileInfo.verbs != null) {
      this.verbs = new ArrayList<>(compileInfo.verbs);
    }
    if (compileInfo.adjectives != null) {
      this.adjectives = new ArrayList<>(compileInfo.adjectives);
    }
    if (compileInfo.words != null) {
      this.words = new ArrayList<>(compileInfo.words);
    }
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

    // no need to check as language is mandatory
    this.language = compileInfoJson.getString(KEY_LANGUAGE);

    if (compileInfoJson.has(KEY_NAME)) {
      this.name = compileInfoJson.getString(KEY_NAME);
    }
    if (compileInfoJson.has(KEY_COMPILEDEBUG)) {
      this.compileDebug = compileInfoJson.getBoolean(KEY_COMPILEDEBUG);
    }
    if (compileInfoJson.has(KEY_EMBEDRESOURCES)) {
      this.embedResources = compileInfoJson.getBoolean(KEY_EMBEDRESOURCES);
    }
    if (compileInfoJson.has(KEY_VERBS)) {
      this.verbs = jsonArrayToStringArray(compileInfoJson.getJSONArray(KEY_VERBS));
    }
    if (compileInfoJson.has(KEY_ADJECTIVES)) {
      this.adjectives = jsonArrayToStringArray(compileInfoJson.getJSONArray(KEY_ADJECTIVES));
    }
    if (compileInfoJson.has(KEY_WORDS)) {
      this.words = jsonArrayToStringArray(compileInfoJson.getJSONArray(KEY_WORDS));
    }
  }

  private List<String> jsonArrayToStringArray(JSONArray arr) {
    List<String> res = new ArrayList<>();
    for (var i = 0; i < arr.length(); i++) {
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
    var res = new JSONObject();
    if (this.language != null) {
      res.put(KEY_LANGUAGE, this.language);
    }
    if (this.name != null) {
      res.put(KEY_NAME, this.name);
    }
    if (this.compileDebug != null) {
      res.put(KEY_COMPILEDEBUG, this.compileDebug);
    }
    if (this.embedResources != null) {
      res.put(KEY_EMBEDRESOURCES, this.embedResources);
    }
    if (this.verbs != null) {
      res.put(KEY_VERBS, new JSONArray(this.verbs));
    }
    if (this.adjectives != null) {
      res.put(KEY_ADJECTIVES, new JSONArray(this.adjectives));
    }
    if (this.words != null) {
      res.put(KEY_WORDS, new JSONArray(this.words));
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
