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

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * RosaeNLG compilation options helper.
 * <p>
 * <a href="https://rosaenlg.org/rosaenlg/1.4.0/advanced/params.html#_compiling_parameters">Compile options</a>
 * </p>
 * @author Ludan Stoecklé ludan.stoeckle@rosaenlg.org
 */
public class CompileOptions implements Cloneable {

  // private static final Logger logger = LoggerFactory.getLogger(CompileOptions.class);

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
  public CompileOptions() {
  }

  
  /** Constructor from json parsed object.
   * 
   * <p>
   * Will find different properties in the json object and populate the object.
   * </p>
   * 
   * @param compileOptionsJson json object containing the properties
   */
  public CompileOptions(JSONObject compileOptionsJson) {
    
    if (compileOptionsJson.has("language")) {
      this.language = compileOptionsJson.getString("language");
    }
    if (compileOptionsJson.has("name")) {
      this.name = compileOptionsJson.getString("name");
    }
    if (compileOptionsJson.has("compileDebug")) {
      this.compileDebug = compileOptionsJson.getBoolean("compileDebug");
    }
    if (compileOptionsJson.has("embedResources")) {
      this.embedResources = compileOptionsJson.getBoolean("embedResources");
    }
    if (compileOptionsJson.has("verbs")) {
      this.verbs = jsonArrayToStringArray(compileOptionsJson.getJSONArray("verbs"));
    }
    if (compileOptionsJson.has("adjectives")) {
      this.adjectives = jsonArrayToStringArray(compileOptionsJson.getJSONArray("adjectives"));
    }
    if (compileOptionsJson.has("words")) {
      this.words = jsonArrayToStringArray(compileOptionsJson.getJSONArray("words"));
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
  public CompileOptions setLanguage(String language) {
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
  public CompileOptions setName(String name) {
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
  public CompileOptions setCompileDebug(Boolean compileDebug) {
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
  public CompileOptions setEmbedResources(Boolean embedResources) {
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
  public CompileOptions setWords(List<String> words) {
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
  public CompileOptions setVerbs(List<String> verbs) {
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
  public CompileOptions setAdjectives(List<String> adjectives) {
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
