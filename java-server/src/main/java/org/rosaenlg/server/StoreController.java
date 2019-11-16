package org.rosaenlg.server;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;

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

import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

// import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

// import org.apache.commons.io.FileUtils;

import org.json.JSONObject;
import org.rosaenlg.lib.JsonPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StoreController {

  private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

  private final AtomicLong renderCounter = new AtomicLong();
  private Store store;

  
  /** Constructor.
   * 
   * @param store a Store that makes the job
   */
  @Autowired
  public StoreController(Store store) {
    logger.debug("called StoreController autowired contructor");
    this.store = store;
  }

  
  /** Gets the list of the loaded templates: their IDs.
   * 
   * @return TemplatesList the IDs of the loaded templates
   */
  @Operation(
      summary = "Get the IDs of the loaded templates.",
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "the IDs of the loaded templates",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TemplatesList.class)
          )
        )
      }
  )
  @GetMapping(value = "/templates")
  public TemplatesList listTemplates() {
    logger.debug("getTemplateIds() {}", store.getTemplateIds().size());
    return new TemplatesList(store.getTemplateIds());
  }

  
  /** Creates a template from a JSON containing a packaged template.
   * 
   * <p>
   * Use the template ID in the result to use the template for rendering deleting etc.
   * Template ID is the same than the one in the JSON package.
   * If the template already exists it is removed and replaced with the new one.
   * </p>
   * 
   * @param body the JSON package containing the template
   * @return CreateTemplateStatus: ID of the created template and status (created or updated).
   * @throws Exception if a problem happens like invalid template
   */
  @Operation(
      summary = "Creates a new template.",
      description = "Creates a template from a JSON containing a packaged template. "
          + "The template is validated, loaded, autotested (if configured so), " 
          + "and saved on disk if persistent storage is set.",
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "template was properly loaded and is available for rendering",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CreateTemplateStatus.class)
          )
        ),
        @ApiResponse(
          responseCode = "404",
          description = "template was not created: invalid template for instance"
        )
      }
  )
  @RequestMapping(value = "/templates", method = { RequestMethod.PUT, RequestMethod.POST })
  public CreateTemplateStatus createTemplate(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "the packaged template",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = JsonPackage.class)
        ),
        required = true
      )
      @org.springframework.web.bind.annotation.RequestBody
      String body) throws Exception {
    logger.debug("createTemplate()");
    CreateTemplateStatus status = this.store.saveTemplateOnDiskAndLoad(body);
    return status;
  }
  
  
  /** Deletes a templates.
   * 
   * <p>
   * Unloaded, and deleted on the disk if permanent storage is set.
   * </p>
   * 
   * @param templateId the ID of the template
   * @throws Exception if the template could not be unloaded or deleted
   */
  @Operation(
      summary = "Deletes an existing template.",
      responses = {
        @ApiResponse(
          responseCode = "200", 
          description = "template was deleted and unloaded"
        ),
        @ApiResponse(
          responseCode = "404",
          description = "template could not be unloaded or deleted"
        )
      }
  )
  @DeleteMapping(value = "/templates/{templateId}")
  public void deleteTemplate(
      @PathVariable(value = "templateId") String templateId) throws Exception {
    logger.debug("deleteTemplate() on {}", templateId);
    this.store.deleteTemplateFileAndUnload(templateId);
  }
  
  /** Get the original JSON package of a template.
   * 
   * @param templateId the ID of the template
   * @return String the JSON package
   * @throws Exception if the template does not exist
   */
  @Operation(
      summary = "Gets the original JSON package of an existing template.",
      responses = {
        @ApiResponse(
          responseCode = "200", 
          description = "the JSON package of a template",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = JsonPackage.class)
          )
        ),
        @ApiResponse(
          responseCode = "404",
          description = "template does not exist"
        )
      }
  )
  @GetMapping(value = "/templates/{templateId}/template")
  public String getTemplate(
      @PathVariable(value = "templateId") String templateId) throws Exception {
    logger.debug("getTemplate() on {}", templateId);
    return this.store.getFullTemplate(templateId);
  }
  
  /** 
   * Renders a template with data. The template must have been created previously.
   * 
   * @param templateId the ID of the template
   * @param body the JSON request containing the data and the parameters
   * @return Rendered the rendered text
   * @throws Exception template does not exist or issue during rendering
   */
  @Operation(
      summary = "Renders a template using data.",
      description = "Renders a previously loaded template using data passed in the request.",
      parameters = {
        @Parameter(
          description = "the data to render the template",
          required = true
        )
      },
      responses = {
        @ApiResponse(
          responseCode = "200", 
          description = "the rendered content",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Rendered.class)
          )
        ),
        @ApiResponse(
          responseCode = "404",
          description = "template does not exist or issue during rendering"
        )
      }
  )
  @PostMapping(value = "/templates/{templateId}/render")
  public Rendered render(
      @PathVariable(value = "templateId") String templateId, 
      @org.springframework.web.bind.annotation.RequestBody String body) throws Exception {
    
    logger.info(
        "render() on {} with {}...", 
        templateId, 
        body.substring(0, Math.min(20, body.length()))
    );

    // FileUtils.write(new File("test-templates-testing/jsonData_received.json"), body, "UTF-8");

    String renderedText = this.store.render(templateId, body);
    logger.info(
        "rendered text: {}", 
        renderedText.substring(0, Math.min(20, renderedText.length())
    ));

    /*
    FileUtils.write(
        new File("test-templates-testing/rendered_before_sent.txt"), 
        renderedText, 
        "UTF-8");
    */

    RenderOptionsForSerialize renderOptions = new RenderOptionsForSerialize(new JSONObject(body));

    Rendered rendered = new Rendered(
        templateId, 
        renderedText, 
        renderCounter.incrementAndGet(), 
        renderOptions);
    return rendered;
  }

  
  /** Reloads a specific template from the disk (if a permanent storage is set).
   * 
   * @param templateId the ID of the template to reload
   * @throws Exception when a problem happens
   */
  @Operation(
      summary = "Reloads a specific template from the disk.",
      description = "Only works if a permanent storage is set.",
      responses = {
        @ApiResponse(
          responseCode = "200", 
          description = "the template was properly reloaded"
        ),
        @ApiResponse(
          responseCode = "404",
          description = "when a problem happens"
        )
      }
  )
  @GetMapping(value = "/templates/{templateId}/reload")
  public void reloadTemplate(
      @PathVariable(value = "templateId") String templateId) throws Exception {
    logger.debug("reloadTemplate() on {}", templateId);
    this.store.reloadExistingTemplate(templateId);
  }

  
  /** Reloads all templates from the disk (if a permanent storage is set).
   * 
   * @throws Exception when a problem happens
   */
  @Operation(
      summary = "Reloads all templates from the disk.",
      description = "Only works if a permanent storage is set.",
      responses = {
        @ApiResponse(
          responseCode = "200", 
          description = "the templates were properly reloaded"
        ),
        @ApiResponse(
          responseCode = "404",
          description = "when a problem happens")
        }
  )
  @GetMapping(value = "/templates/reload")
  public void reloadTemplates() throws Exception {
    logger.debug("reloadTemplates()");
    this.store.reloadExistingTemplates();
  }

}
