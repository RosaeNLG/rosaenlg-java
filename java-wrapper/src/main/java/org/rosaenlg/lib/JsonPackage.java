package org.rosaenlg.lib;

import java.io.InputStream;
import java.util.List;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Will contain a deserialized JSON package of a template.
 * <p>
 * To generate a JSON package from plain templates using gulp, see <a href=
 * "https://rosaenlg.org/rosaenlg/1.18.1/integration/rosaenlg_packager.html">RosaeNLG Packager</a>
 * </p>
 * 
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class JsonPackage {

  private static final Logger logger = LoggerFactory.getLogger(JsonPackage.class);

  private String initialPackage;

  private String templateId;
  private JsonPackageSrc src;

  /** Constructor using a JSON string.
   * 
   * @param jsonPackageAsString the string containing the JSON package
   * @throws ValidationException if the JSON schema is not respected
   */
  public JsonPackage(String jsonPackageAsString) {

    this.initialPackage = jsonPackageAsString;

    var jsonPackage = new JSONObject(jsonPackageAsString);

    try {
      var inputStreamSchema = getClass().getResourceAsStream("/jsonPackage.schema.json");
      var rawSchema = new JSONObject(new JSONTokener(inputStreamSchema));
      var schema = SchemaLoader.load(rawSchema);
      schema.validate(jsonPackage);
    } catch (ValidationException ve) {
      logger.error("Errors in JSON template validation:");
      List<ValidationException> causes = ve.getCausingExceptions();
      for (var i = 0; i < causes.size(); i++) {
        logger.error(causes.get(i).toString());
      }      
      throw ve;
    }

    this.templateId = jsonPackage.getString("templateId");

    this.src = new JsonPackageSrc(jsonPackage.getJSONObject("src"));

  }

  
  /** Returns the template ID.
   * 
   * @return String template ID
   */
  public String getTemplateId() {
    return this.templateId;
  }

  /** Returns the template src.
   * 
   * @return JsonPackageSrc template src
   */
  public JsonPackageSrc getSrc() {
    return this.src;
  }


  /** Returns the initial value of the template, as a String.
   * @return String the initial template
   */
  public String getInitialPackage() {
    return this.initialPackage;
  }
  
}
