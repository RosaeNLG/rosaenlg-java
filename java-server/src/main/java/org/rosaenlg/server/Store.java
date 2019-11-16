package org.rosaenlg.server;

/*-
 * #%L
 * org.rosaenlg:java-server
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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;

import org.rosaenlg.lib.RosaeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Store {

  private static final Logger logger = LoggerFactory.getLogger(Store.class);

  private Path templatesPath;

  private Map<String, RosaeContext> rosaeContexts = new ConcurrentHashMap<String, RosaeContext>();

  /** Main store constructor.
   * 
   * <p>
   * Templates can be loaded from the disk (and new templates will be stored on the disk), in this
   * case provide a path through 'rosaenlg.homedir' system property or 'ROSAENLG_HOMEDIR'
   * environment property.
   * If you don't provide such a path, it will still work, but templates will not be loaded from 
   * the disk (you have to make REST calls to load them), and they will not be stored on the disk.
   * </p>
   * 
   * @throws Exception if templatesPath is set but templates cannot be loaded
   */
  @Autowired
  public Store() throws Exception {
    String propDir = System.getProperty("rosaenlg.homedir");
    if (propDir != null) {
      logger.info("using system property: {}", propDir);
      this.templatesPath = Paths.get(propDir);
    } else {
      String envDir = System.getenv("ROSAENLG_HOMEDIR");
      if (envDir != null) {
        logger.info("using env property: {}", envDir);
        this.templatesPath = Paths.get(envDir);
      } else {
        logger.info("did not find env nor system property");
      }
    }
    if (this.templatesPath != null) {
      this.reloadExistingTemplates();
    }

  }

  /** Store constructor for test purposes.
   * 
   * <p>
   * Templates path is forced and not taken using environement variables or system properties.
   * Can be null.
   * </p>
   * 
   * @param templatePathString the path on the disk where to store the templates (can be null)
   * @throws Exception if templatesPath is set but templates cannot be loaded
   */
  public Store(String templatePathString) throws Exception {
    if (templatePathString != null) {
      this.templatesPath = Paths.get(templatePathString);
      this.reloadExistingTemplates();
    }
  }
    
  /** Reloads all the templates from the disk.
   * 
   * @throws Exception if no templates path is configured
   */
  public void reloadExistingTemplates() throws Exception {
    if (templatesPath == null) {
      throw new Exception("cannot reload as templates path is not set!");  
    } else {
      logger.info("templates path set to: {}, refreshing...", templatesPath);
      this.unloadTemplates();
      this.loadExistingTemplates();
      logger.info("templates path refresh done, templates: {}", getTemplateIds().toString());
    }
  }

  
  /** Reloads a single template from the disk.
   * 
   * @param templateId the template to reload from the disk
   * @throws Exception if something wrong happens
   */
  public void reloadExistingTemplate(String templateId) throws Exception {
    if (templatesPath == null) {
      throw new Exception("cannot reload " + templateId + " as templates path is not set!");
    } else {
      this.loadExistingTemplate(getTemplateFile(templateId));
    }
  }

  /** Unloads a specific template.
   * 
   * @param templateId the ID of the template
   * @throws Exception if the template was not loaded
   */
  private void unloadTemplate(String templateId) throws Exception {
    if (rosaeContexts.get(templateId) == null) {
      throw new Exception(templateId + " does not exist");
    }
    rosaeContexts.get(templateId).destroy();
    rosaeContexts.remove(templateId);
  }

  private void unloadTemplates() {
    for (Entry<String, RosaeContext> contextElt : rosaeContexts.entrySet()) {
      String templateId = contextElt.getValue().getTemplateId();
      try {
        unloadTemplate(templateId);
      } catch (Exception e) {
        logger.warn("could not unload template {}: {}", templateId, e.toString());
      }
    }
    this.rosaeContexts = new ConcurrentHashMap<String, RosaeContext>();
  }

  
  /** Loads an existing template from the disk.
   * @param jsonFile the json file to load
   * @throws Exception if the file could not be loaded
   */
  private void loadExistingTemplate(File jsonFile) throws Exception {
    // read json file
    try {
      String jsonContent = FileUtils.readFileToString(jsonFile, "utf-8");
      logger.debug("json file {}: {}", jsonFile.toString(), jsonContent);

      RosaeContext rc = new RosaeContext(jsonContent);
      rosaeContexts.put(rc.getTemplateId(), rc);

    } catch (Exception e) {
      logger.error("could not load {}: {}", jsonFile.toString(), e.toString());
      throw e;
    }
    logger.debug("json file {} properly loaded", jsonFile.toString());

  }

  
  /** Loads all existing templates from the disk.
   * 
   * @throws Exception if templates path is not set. No exception if a template could not be loaded.
   */
  public void loadExistingTemplates() throws Exception {
    if (templatesPath == null) {
      throw new Exception("nothing to load as templates path is not set!");
    }
    for (final File file : templatesPath.toFile().listFiles()) {
      if (!file.isDirectory() && file.toString().endsWith(".json")) {
        try {
          loadExistingTemplate(file);
        } catch (Exception e) {
          logger.error("could not load {}: {}", file.toString(), e.toString());
        }
      }
    }
  }

  
  /** Indicates if a template exists and is loaded.
   * @param templateId the ID of the template
   * @return boolean true if the template is loaded
   */
  public boolean templateLoaded(String templateId) {
    return rosaeContexts.keySet().contains(templateId);
  }

  
  /** Gets the filename for a template ID.
   * 
   * <p>
   * Does not guarantee that the file exists. It just generates its name.
   * </p>
   * 
   * @param templateId the ID of the template
   * @return File the file that should contain the template
   */
  private File getTemplateFile(String templateId) {
    return new File(this.templatesPath + File.separator + templateId + ".json");
  }

  
  /** Deletes a template file and unloads it if it was loaded.
   * 
   * @param templateId the ID of the template
   * @throws Exception if the template could not be unloaded
   */
  public void deleteTemplateFileAndUnload(String templateId) throws Exception {
    if (templatesPath != null) {
      deleteTemplateFile(templateId);
    }
    unloadTemplate(templateId);
  }

  
  /** Deletes a template file on the disk.
   * @param templateId the ID of the template
   * @throws Exception if the file could not be deleted
   */
  private void deleteTemplateFile(String templateId) throws Exception {
    File fileToDelete = getTemplateFile(templateId);
    if (!fileToDelete.delete()) {
      throw new Exception("cannot delete file " + fileToDelete.toString());
    }
  }

  
  /** Saves a template on the disk and loads it.
   * 
   * <p>
   * Template is not saved in disk if a path was not provided. But it still will be loaded.
   * It can be used to either create or update a template.
   * </p>
   * 
   * @param template contains the JSON template package
   * @return the status of the created template (created or updated and its ID)
   * @throws Exception if the content of the JSON package is not well formed
   */
  public CreateTemplateStatus saveTemplateOnDiskAndLoad(String template) throws Exception {
    // load
    RosaeContext rc = new RosaeContext(template);

    // status
    final CreateStatus status = templateLoaded(rc.getTemplateId())
        ? CreateStatus.UPDATED 
        : CreateStatus.CREATED;

    rosaeContexts.put(rc.getTemplateId(), rc);

    // save
    if (templatesPath != null) {
      saveTemplateOnDisk(rc.getTemplateId(), template);
    }

    return new CreateTemplateStatus(rc.getTemplateId(), status);
  }

  
  /** Saves the template on the disk.
   * 
   * @param templateId the ID of the template (to generate the filename)
   * @param template the content of the template (JSON package)
   * @throws Exception if templates path is not set
   */
  private void saveTemplateOnDisk(String templateId, String template) throws Exception {
    // tmp file first
    File tmpFile = new File(this.templatesPath + File.separator + templateId + ".tmp");
    FileUtils.writeStringToFile(tmpFile, template, "utf-8");

    // then rename
    File targetFile = getTemplateFile(templateId);
    targetFile.delete(); // must delete first because renameTo often fails
    boolean renamed = tmpFile.renameTo(targetFile);

    if (!renamed) {
      throw new Exception("could not rename " + tmpFile.toString());
    }
  }

  
  /** Gets all the loaded templates.
   * 
   * <p>
   * Returns only the loaded templates: if a template is on the disk but not loaded it will not be
   * in that list.
   * </p>
   *  
   * @return a list of the ID of the loaded templates.
   */
  public List<String> getTemplateIds() {
    List<String> res = new ArrayList<String>();
    for (Entry<String, RosaeContext> contextElt : rosaeContexts.entrySet()) {
      res.add(((RosaeContext) contextElt.getValue()).getTemplateId());
    }
    return res;
  }

  
  /** Render a template based on the data.
   * 
   * @param templateId the ID of the template
   * @param jsonOptions the options (input data) for the template
   * @return String the rendered string
   * @throws Exception if the template does not exist, or if there was an exception during rendering
   */
  public String render(String templateId, String jsonOptions) throws Exception {
    if (!templateLoaded(templateId)) {
      throw new Exception("template not found: " + templateId);
    }
    RosaeContext rosaeContext = rosaeContexts.get(templateId);
    return rosaeContext.render(jsonOptions);
  }

  
  /** Returns the JSON package of a template.
   * 
   * <p>
   * The template must be loaded (not just on the disk).
   * </p>
   * 
   * @param templateId the ID of the template.
   * @return String the template (JSON package)
   * @throws Exception if the template was not loaded
   */
  public String getFullTemplate(String templateId) throws Exception {
    return rosaeContexts.get(templateId).getJsonPackageAsString();
  }

}