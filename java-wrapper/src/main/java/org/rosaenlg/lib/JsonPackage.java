package org.rosaenlg.lib;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
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
 * Will contain a deserialized JSON package of a template.
 * <p>
 * To generate a JSON package from plain templates using gulp, see <a href=
 * "https://gitlab.com/rosaenlg-projects/rosaenlg/tree/master/packages/gulp-rosaenlg"></a>
 * </p>
 * 
 * @author Ludan Stoecklé ludan.stoeckle@rosaenlg.org
 */
public class JsonPackage {

  private String initialPackage;
  private String templateId;
  private String entryTemplate;
  private CompileOptions compileInfo;
  private Map<String, String> templates = new HashMap<String, String>();
  private Autotest autotest;

  /** Constructor using a JSON string.
   * 
   * @param jsonPackageAsString the string containing the JSON package
   * @throws ValidationException if the JSON schema is not respected
   */
  public JsonPackage(String jsonPackageAsString) throws ValidationException {

    this.initialPackage = jsonPackageAsString;

    JSONObject jsonPackage = new JSONObject(jsonPackageAsString);

    {
      InputStream inputStreamSchema = getClass().getResourceAsStream("/jsonPackage.schema.json");
      JSONObject rawSchema = new JSONObject(new JSONTokener(inputStreamSchema));
      Schema schema = SchemaLoader.load(rawSchema);
      schema.validate(jsonPackage);
    }

    this.templateId = jsonPackage.getString("templateId");
    this.entryTemplate = jsonPackage.getString("entryTemplate");
    
    JSONObject compileInfoJson = jsonPackage.getJSONObject("compileInfo");
    this.compileInfo = new CompileOptions(compileInfoJson);

    JSONObject templatesJson = jsonPackage.getJSONObject("templates");
    Iterator<String> keys = templatesJson.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      templates.put(key, templatesJson.getString(key));
    }

    if (jsonPackage.has("autotest")) {
      this.autotest = new Autotest(jsonPackage.getJSONObject("autotest"));
    }
  }

  
  /** Returns the template ID.
   * 
   * @return String template ID
   */
  public String getTemplateId() {
    return this.templateId;
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
   * @return CompileOptions
   */
  public CompileOptions getCompileInfo() {
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
  
  /** Returns the initial value of the template, as a String.
   * @return String the initial template
   */
  public String getInitialPackage() {
    return this.initialPackage;
  }
  
}
