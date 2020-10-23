package org.rosaenlg.lib;

/*-
 * #%L
 * org.rosaenlg:java-wrapper
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

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import org.json.JSONObject;

import org.rosaenlg.lib.Autotest;
import org.rosaenlg.lib.JsonPackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper around RosaeNLG.
 * 
 * @see <a href="https://rosaenlg.org">RosaeNLG documentation</a>
 * @author Ludan Stoecklé ludan.stoeckle@rosaenlg.org
 */
public class RosaeContext {

  private static final Logger logger = LoggerFactory.getLogger(RosaeContext.class);

  private static Source sourceWrapper;
  private static final String exceptionMarker = "EXCEPTION ";
  private static Map<String, Source> sourcesRosaeNLG = new HashMap<String, Source>();

  private String language;
  private String templateId;
  private String entryTemplate;
  private CompileOptions compileInfo;
  private Map<String, String> templates = new HashMap<String, String>();
  private Autotest autotest;

  private Context context;

  private String originalJsonPackage;
  private Value compiledTemplateFct;

  static {
    logger.info("Entering static constructor...");

    try {
      sourceWrapper = Source.newBuilder("js",
          new InputStreamReader(
            RosaeContext.class.getResourceAsStream("/wrapper.js"), "UTF-8"), 
            "wrapper.js").build();
      try {
        // System.out.println("We are in a fat jar...");
        URL res = RosaeContext.class.getClassLoader().getResource("/META-INF/truffle/language");
        // initialize the file system for the language file
        FileSystems.newFileSystem(res.toURI(), new HashMap<>());
        logger.info("Fat jar FileSystem ok");
      } catch (Throwable ignored) {
        // in case of starting without fat jar
      }

    } catch (Exception e) {
      throw new RuntimeException("cannot find wrapper.js: " + e.toString());
    }

    logger.info("Constructor static done.");
  }

  /** Constructor, for very simple templates.
   * 
   * <p>
   * Will create a new GraalVM context for this template, compile the template,
   * and be ready to render multiple times.
   * </p>
   * 
   * @param template the content of the template
   * @param compileInfo how to compile the template
   * @throws Exception if compiling fails
   */
  public RosaeContext(
      String template,
      CompileOptions compileInfo) throws Exception {

    this.entryTemplate = "main";
    this.compileInfo = compileInfo;
    templates.put("main", template);
    
    this.init();
  }


  /** Constructor, based on traditional Pug templates.
   * 
   * <p>
   * Will create a new GraalVM context for this template, compile the template,
   * and be ready to render multiple times.
   * </p>
   * 
   * @param entryTemplate the name of the template to compile
   * @param includesPath the path where the templates to be included are located
   *      (including the entry template)
   * @param compileInfo how to compile the template
   * @throws Exception if compiling fails
   */
  public RosaeContext(
      String entryTemplate, 
      File includesPath, 
      CompileOptions compileInfo) throws Exception {

    this.entryTemplate = entryTemplate;
    this.compileInfo = compileInfo;

    Collection<File> files = FileUtils.listFiles(
        includesPath,
        TrueFileFilter.INSTANCE, 
        TrueFileFilter.INSTANCE);
    for (File file : files) {
      String content = FileUtils.readFileToString(file, "utf-8");
      templates.put(
          Paths.get(includesPath.toURI())
              .relativize(Paths.get(file.toURI()))
              .toString()
              .replace("\\", "/"), // must fit pug convention
          content);
    }
    // logger.debug(templates.toString());

    this.init();
  }


  /** Constructor, based on a String containing all the information on a template.
   * 
   * <p>
   * Will create a new GraalVM context for this template, compile the template,
   * test if autotest is activated, and be ready to render multiple times.
   * See
   * https://rosaenlg.org/rosaenlg/1.5.0/integration/gulp.html#_packagetemplatejson
   * to create such a package.
   * </p>
   * 
   * @param jsonPackageAsString contains a template and its parameters, JSON format
   * @throws Exception when the JSON package is not well formated, 
   *                   or if the autotest was activated and failed.
   */
  public RosaeContext(String jsonPackageAsString) throws Exception {
    JsonPackage jsonPackage = new JsonPackage(jsonPackageAsString);
    this.originalJsonPackage = jsonPackageAsString;
    this.templateId = jsonPackage.getTemplateId();
    this.entryTemplate = jsonPackage.getEntryTemplate();
    this.compileInfo = jsonPackage.getCompileInfo();
    this.templates = jsonPackage.getTemplates();
    this.autotest = jsonPackage.getAutotest();
  
    this.init();
  }

  private void init() throws Exception {
    logger.info("Constructor RosaeContext");

    this.language = this.compileInfo.getLanguage();
    if (this.language == null) {
      throw new Exception("language must be set!");
    }

    this.initGraalContext();
    this.compile();
    this.autotest();

    logger.info("Constructor RosaeContext done.");
  }

  private void initGraalContext() throws Exception {

    context = Context.newBuilder("js").allowAllAccess(false).build();

    context.eval(getSourcesForLanguage(this.language));
    context.eval(sourceWrapper);
  }

  private void compile() throws Exception {

    // logger.info("templates: {}", (new JSONObject(jsonPackage.getTemplates())).toString());
    // logger.info("compileInfo: {}", jsonPackage.getCompileInfo().toJson());

    Value compileFileFct = context.eval("js", "compileFile");
    assert compileFileFct.canExecute();

    compiledTemplateFct = compileFileFct.execute(
        entryTemplate, 
        this.language,
        (new JSONObject(templates)).toString(),
        compileInfo.toJson(), 
        exceptionMarker);
    
    if (compiledTemplateFct.toString().startsWith(exceptionMarker)) {
      throw new Exception(compiledTemplateFct.toString().replace(exceptionMarker, ""));
    }
  }

  private void autotest() throws Exception {
    if (autotest != null && autotest.getActivate()) {
      logger.info("auto test is activated");

      String rendered = this.render(autotest.getJsonInput());
      for (int i = 0; i < autotest.getExpected().size(); i++) {
        if (!rendered.contains(autotest.getExpected().get(i))) {
          throw new Exception(
            templateId
            + " autotest fail on "
            + autotest.getExpected().get(i)
            + " was "
            + rendered);
        }
      }
    }
  }

  private Source getSourcesForLanguage(String language) throws Exception {
    if (sourcesRosaeNLG.get(language) == null) {
      logger.info("will now load rosaenlg js for {}", language);

      final Properties properties = new Properties();
      properties.load(RosaeContext.class.getResourceAsStream("/project.properties"));
      String version = properties.getProperty("rosaenlg.version");
      String rosaejsFileName = "rosaenlg_tiny_" + language + "_" + version + "_comp.js";
      logger.info("using rosaenlg file: {}", rosaejsFileName);

      Source sourceRosaeNlg = Source.newBuilder(
          "js",
          new InputStreamReader(
              RosaeContext.class.getResourceAsStream("/" + rosaejsFileName), "UTF-8"), 
              rosaejsFileName)
          .build();

      sourcesRosaeNLG.put(language, sourceRosaeNlg);
      logger.info("RosaeNLG js lib read, lines {}", sourceRosaeNlg.getLineCount());
    } else {
      logger.info("rosaenlg js for {} is already loaded", language);
    }
    return sourcesRosaeNLG.get(language);
  }

  
  /** Render the template with input data.
   * 
   * @param jsonOptionsAsString JSON string containing all the input data to render the template.
   * @return String the rendered result
   * @throws Exception if an error occurs during rendering
   */
  public synchronized String render(String jsonOptionsAsString) throws Exception {

    // inject NlgLib into the options
    JSONObject jsonOptions = new JSONObject(jsonOptionsAsString);

    RenderOptions runtimeOptions = new RenderOptions(jsonOptions);

    // we keep original but add 'util'
    jsonOptions.put("util", "NLGLIB_PLACEHOLDER");
    String finalOptions = jsonOptions.toString().replace("\"NLGLIB_PLACEHOLDER\"",
        "new rosaenlg_"
        + this.language
        + ".NlgLib(JSON.parse('"
        + runtimeOptions.toJson() + "'))");
    String paramForge = "() => { return " + finalOptions + ";}";

    Value realParam = context.eval("js", paramForge).execute();
    String rendered = compiledTemplateFct.execute(realParam).asString();
    if (rendered.startsWith(exceptionMarker)) {
      throw new Exception(rendered.replace(exceptionMarker, ""));
    }
    return rendered;    
  }


  /**
   * Create a compiled file for client side rendering.
   * <p>
   * This is useful to compile a templates, bundle everything for later rendering,
   * typically client side in a browser.
   * </p>
   * 
   * @return String the compiled template (which is JavaScript code)
   * @throws Exception if a problem occurs
   */
  public String getCompiledClient() throws Exception {
    Value compileFileClientFct = context.eval("js", "compileFileClient");
    assert compileFileClientFct.canExecute();

    CompileOptions newCompileOpts = (CompileOptions)this.compileInfo.clone();
    newCompileOpts.setEmbedResources(true);    
    
    String compiled = compileFileClientFct.execute(
          entryTemplate, 
          language,
          (new JSONObject(templates)).toString(),
          newCompileOpts.toJson(), 
          exceptionMarker)
        .asString();
    
    if (compiled.startsWith(exceptionMarker)) {
      throw new Exception(compiled.replace(exceptionMarker, ""));
    }
    return compiled;
  }
  
  /** Getter on template ID.
   * 
   * @return String the template ID
   */
  public String getTemplateId() {
    return this.templateId;
  }

  
  /** Getter on the original JSON package containing the template.
   * 
   * @return String the JSON package containing the template.
   */
  public String getJsonPackageAsString() {
    return this.originalJsonPackage;
  }

  /** Destroys the object by closing its context.
   * 
   * <p>
   * It is recommended to use 'destroy' so that the GraalVM is explicitely closed.
   * </p>
   * 
   * @throws Exception if closing the context failed
   */
  public synchronized void destroy() throws Exception {
    context.close(false); // no cancel if still executing
  }
}
