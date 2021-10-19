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

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;

/**
 * Wrapper around RosaeNLG.
 * 
 * @see <a href="https://rosaenlg.org">RosaeNLG documentation</a>
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class RosaeContext {

  private static final Logger logger = LoggerFactory.getLogger(RosaeContext.class);

  private static Source sourceWrapper;
  private static Map<String, Source> sourcesRosaeNLG = new HashMap<>();

  private String language;
  private String templateId;
  private String entryTemplate;
  private CompileInfo compileInfo;
  private Map<String, String> templates = new HashMap<>();
  private Autotest autotest;

  private Context context;

  private String originalJsonPackage;
  private Value compiledTemplateFct;

  static {
    logger.info("Entering static constructor...");

    setWrapperJs();

    try {
      URL res = RosaeContext.class.getClassLoader().getResource("/META-INF/truffle/language");
      // initialize the file system for the language file
      FileSystems.newFileSystem(res.toURI(), new HashMap<>());
      logger.info("Fat jar FileSystem ok");
    } catch (Exception ignored) {
      // in case of starting without fat jar
    }

    logger.info("Constructor static done.");
  }

  private static void setWrapperJs() {
    try {
      sourceWrapper = Source.newBuilder("js",
          new InputStreamReader(RosaeContext.class.getResourceAsStream("/wrapper.js"), StandardCharsets.UTF_8),
          "wrapper.js").build();
    } catch (Exception e) {
      // must throw a RuntimeException as it is called from a static block
      throw new WrapperNotFoundException("cannot find wrapper.js", e);
    }
  }

  /**
   * Constructor, for very simple templates.
   * 
   * <p>
   * Will create a new GraalVM context for this template, compile the template,
   * and be ready to render multiple times.
   * </p>
   * 
   * @param template    the content of the template
   * @param compileInfo how to compile the template
   * @throws RosaeContextConstructorException if context construction fails
   */
  public RosaeContext(String template, CompileInfo compileInfo) throws RosaeContextConstructorException {
    try {
      this.entryTemplate = "main";
      this.compileInfo = compileInfo;
      templates.put("main", template);

      this.init();
    } catch (Exception e) {
      throw new RosaeContextConstructorException(e);
    }
  }

  /**
   * Constructor, based on traditional Pug templates but located in <b>project
   * resources</b>.
   * 
   * <p>
   * Will create a new GraalVM context for this template, compile the template,
   * and be ready to render multiple times.
   * </p>
   * 
   * @param entryTemplate     the name of the template to compile
   * @param resourcesLocation the subfolder in resources where the templates to be
   *                          included are located (including the entry template)
   *                          (could be "templates")
   * @param compileInfo       how to compile the template
   * @throws RosaeContextConstructorException if compiling fails, or if reading
   *                                          disk fails
   */
  public RosaeContext(String entryTemplate, String resourcesLocation, CompileInfo compileInfo)
      throws RosaeContextConstructorException {

    try {
      this.entryTemplate = entryTemplate;
      this.compileInfo = compileInfo;

      logger.info("resources path is {}", resourcesLocation);

      PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource[] resourcesArr = resourceResolver.getResources(resourcesLocation + "/**");

      Resource root = resourceResolver.getResource(resourcesLocation);

      for (Resource resource : resourcesArr) {
        logger.debug("one resource: {}", resource);
        if (resource.isReadable()) {
          logger.debug("{} is readable...", resource);
          String content = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8.name());
          logger.debug("content: {}", content);
          // relativize does not work on jar URI
          String correctedPath = resource.getURI().toString().replace(root.getURI().toString() + "/", "");
          logger.debug("corrected path: <{}>", correctedPath);
          templates.put(correctedPath, content);
        }
      }
      this.init();
    } catch (Exception e) {
      throw new RosaeContextConstructorException(e);
    }
  }

  /**
   * Constructor, based on traditional Pug templates.
   * 
   * <p>
   * Will create a new GraalVM context for this template, compile the template,
   * and be ready to render multiple times.
   * </p>
   * 
   * @param entryTemplate the name of the template to compile
   * @param includesPath  the path where the templates to be included are located
   *                      (including the entry template)
   * @param compileInfo   how to compile the template
   * @throws RosaeContextConstructorException if compiling fails, or if reading
   *                                          disk fails
   */
  public RosaeContext(String entryTemplate, File includesPath, CompileInfo compileInfo)
      throws RosaeContextConstructorException {

    try {
      this.entryTemplate = entryTemplate;
      this.compileInfo = compileInfo;

      Collection<File> files = FileUtils.listFiles(includesPath, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
      for (File file : files) {
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

        // must fit pug convention
        String correctedPath = includesPath.toURI().relativize(file.toURI()).toString().replace("\\", "/");
        logger.debug("corrected path: <{}>", correctedPath);
        templates.put(correctedPath, content);
      }

      this.init();
    } catch (Exception e) {
      throw new RosaeContextConstructorException(e);
    }
  }

  /**
   * Constructor, based on a String containing all the information on a template.
   * 
   * <p>
   * Will create a new GraalVM context for this template, compile the template,
   * test if autotest is activated, and be ready to render multiple times. See
   * https://rosaenlg.org/rosaenlg/1.18.1/integration/rosaenlg_packager.html to
   * create such a package.
   * </p>
   * 
   * @param jsonPackageAsString contains a template and its parameters, JSON
   *                            format
   * @throws RosaeContextConstructorException when the JSON package is not well
   *                                          formated, or if the autotest was
   *                                          activated and failed.
   */
  public RosaeContext(String jsonPackageAsString) throws RosaeContextConstructorException {
    try {
      JsonPackage jsonPackage = new JsonPackage(jsonPackageAsString);
      this.originalJsonPackage = jsonPackageAsString;
      this.templateId = jsonPackage.getTemplateId();
      this.entryTemplate = jsonPackage.getSrc().getEntryTemplate();
      this.compileInfo = jsonPackage.getSrc().getCompileInfo();
      this.templates = jsonPackage.getSrc().getTemplates();
      this.autotest = jsonPackage.getSrc().getAutotest();

      this.init();
    } catch (Exception e) {
      throw new RosaeContextConstructorException(e);
    }
  }

  private void init() throws InitRosaeContextException {
    try {
      logger.info("Constructor RosaeContext");

      this.language = this.compileInfo.getLanguage();
      if (this.language == null) {
        throw new LanguageNotDefinedException("language not defined in compileInfo");
      }

      this.initGraalContext();
      this.compile();
      this.autotest();

      logger.info("Constructor RosaeContext done.");
    } catch (Exception e) {
      throw new InitRosaeContextException(e);
    }
  }

  private void initGraalContext() throws InitGraalContextException {
    try {
      context = Context.newBuilder("js").allowAllAccess(false).build();

      context.eval(getSourcesForLanguage(this.language));
      context.eval(sourceWrapper);
    } catch (Exception e) {
      throw new InitGraalContextException(e);
    }

  }

  private void compile() throws CompileException {
    try {

      Value compileFileFct = context.eval("js", "compileFile");
      assert compileFileFct.canExecute();

      compiledTemplateFct = compileFileFct.execute(entryTemplate, this.language, (new JSONObject(templates)).toString(),
          compileInfo.toJson());
    } catch (Exception e) {
      throw new CompileException("cannot compile", e);
    }
  }

  private void autotest() throws AutotestException {
    if (autotest != null && autotest.getActivate()) {
      logger.info("auto test is activated");

      try {
        String rendered = this.render(autotest.getJsonInput()).getRenderedText();
        for (int i = 0; i < autotest.getExpected().size(); i++) {
          if (!rendered.contains(autotest.getExpected().get(i))) {
            throw new AutotestException(templateId, autotest.getExpected().get(i), rendered);
          }
        }
      } catch (RenderingException e) {
        throw new AutotestException(templateId, e);
      }
    }
  }

  private Source getSourcesForLanguage(String language) throws LoadLanguageException {
    try {
      if (sourcesRosaeNLG.get(language) == null) {
        logger.info("will now load rosaenlg js for {}", language);

        final Properties properties = new Properties();
        properties.load(RosaeContext.class.getResourceAsStream("/project.properties"));
        String version = properties.getProperty("rosaenlg.version");
        String rosaejsFileName = "rosaenlg_tiny_" + language + "_" + version + "_comp.js";
        logger.info("using rosaenlg file: {}", rosaejsFileName);

        Source sourceRosaeNlg = Source
            .newBuilder("js", new InputStreamReader(RosaeContext.class.getResourceAsStream("/" + rosaejsFileName),
                StandardCharsets.UTF_8), rosaejsFileName)
            .build();

        sourcesRosaeNLG.put(language, sourceRosaeNlg);
        logger.info("RosaeNLG js lib read, lines {}", sourceRosaeNlg.getLineCount());
      } else {
        logger.info("rosaenlg js for {} is already loaded", language);
      }
      return sourcesRosaeNLG.get(language);
    } catch (Exception e) {
      throw new LoadLanguageException("could not load language: " + language, e);
    }
  }

  /**
   * Render the template with input data.
   * 
   * @param jsonOptionsAsString JSON string containing all the input data to
   *                            render the template.
   * @return RenderResult the rendered result
   * @throws RenderingException if an error occurs during rendering
   */
  public synchronized RenderResult render(String jsonOptionsAsString) throws RenderingException {

    // inject NlgLib into the options
    JSONObject jsonOptions = new JSONObject(jsonOptionsAsString);

    RenderOptionsInput runtimeOptions = new RenderOptionsInput(jsonOptions);

    // we keep original but add 'util'
    jsonOptions.put("util", "NLGLIB_PLACEHOLDER");
    String finalOptions = jsonOptions.toString().replace("\"NLGLIB_PLACEHOLDER\"",
        "new rosaenlg_" + this.language + ".NlgLib(JSON.parse('" + runtimeOptions.toJsonString() + "'))");
    String paramForge = "() => { return " + finalOptions + ";}";

    Value realParam = context.eval("js", paramForge).execute();
    try {
      String jsonRenderedString = compiledTemplateFct.execute(realParam).asString();
      return new RenderResult(jsonOptions, jsonRenderedString);

    } catch (Exception e) {
      throw new RenderingException("cannot render", e);
    }
  }

  /**
   * Create a compiled file for client side rendering.
   * <p>
   * This is useful to compile a templates, bundle everything for later rendering,
   * typically client side in a browser.
   * </p>
   * 
   * @return String the compiled template (which is JavaScript code)
   * @throws CompiledClientException if a problem occurs
   */
  public String getCompiledClient() throws CompiledClientException {
    try {
      Value compileFileClientFct = context.eval("js", "compileFileClient");
      assert compileFileClientFct.canExecute();

      CompileInfo newCompileOpts = new CompileInfo(this.compileInfo);

      newCompileOpts.setEmbedResources(true);

      return compileFileClientFct
          .execute(entryTemplate, language, (new JSONObject(templates)).toString(), newCompileOpts.toJson()).asString();
    } catch (Exception e) {
      throw new CompiledClientException("cannot compile", e);

    }

  }

  /**
   * Getter on template ID.
   * 
   * @return String the template ID
   */
  public String getTemplateId() {
    return this.templateId;
  }

  /**
   * Getter on the original JSON package containing the template.
   * 
   * @return String the JSON package containing the template.
   */
  public String getJsonPackageAsString() {
    return this.originalJsonPackage;
  }

  /**
   * Destroys the object by closing its context.
   * 
   * <p>
   * It is recommended to use 'destroy' so that the GraalVM is explicitely closed.
   * </p>
   * 
   * @throws DestroyException if closing the context failed
   */
  public synchronized void destroy() throws DestroyException {
    try {
      context.close(false); // no cancel if still executing
    } catch (Exception e) {
      throw new DestroyException("destroy context failed " + this.templateId, e);
    }
  }
}
