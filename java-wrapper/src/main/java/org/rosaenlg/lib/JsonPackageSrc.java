package org.rosaenlg.lib;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;

import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Will contain a deserialized JSON package of a template, "src" part.
 * <p>
 * To generate a JSON package from plain templates using gulp, see <a href=
 * "https://rosaenlg.org/rosaenlg/1.18.1/integration/rosaenlg_packager.html">RosaeNLG Packager</a>
 * </p>
 * 
 * @author Ludan Stoecklé ludan.stoeckle@rosaenlg.org
 */
public class JsonPackageSrc {

  private String entryTemplate;
  private CompileInfo compileInfo;
  private Map<String, String> templates = new HashMap<String, String>();
  private Autotest autotest;

  /** Constructor using a JSON object.
   * 
   * @param src the JSON object containing the src part of the JSON package
   */
  public JsonPackageSrc(JSONObject src) {

    this.entryTemplate = src.getString("entryTemplate");
    
    JSONObject compileInfoJson = src.getJSONObject("compileInfo");
    this.compileInfo = new CompileInfo(compileInfoJson);

    JSONObject templatesJson = src.getJSONObject("templates");
    Iterator<String> keys = templatesJson.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      templates.put(key, templatesJson.getString(key));
    }

    if (src.has("autotest")) {
      this.autotest = new Autotest(src.getJSONObject("autotest"));
    }
  }
  
  /** Returns the entry template.
   * 
   * <p>The entry template is the one that is indicated when compiling, and which can include other
   * templates (in the same JSON package).
   * </p>
   * 
   * @return String the name of the entry template
   */
  public String getEntryTemplate() {
    return this.entryTemplate;
  }
  
  /** Getter on the compile options.
   * @return CompileInfo
   */
  public CompileInfo getCompileInfo() {
    return this.compileInfo;
  }
  
  /** Getter on the template content.
   * 
   * <p>
   * Key value where the key is the template name, and the value the content of the template.
   * </p>
   * 
   * @return template name and template content map
   */
  public Map<String, String> getTemplates() {
    return this.templates;
  }
  
  /** Getter on the auto test part of the package.
   * @return Autotest the auto test, if exists
   */
  public Autotest getAutotest() {
    return this.autotest;
  }
  

}
