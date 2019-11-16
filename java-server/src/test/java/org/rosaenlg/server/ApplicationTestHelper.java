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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


public class ApplicationTestHelper {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationTestHelper.class);

  private static final String REPO_FOLDER = "test-templates-repo";

  protected MockMvc mvc;

  protected ApplicationTestHelper(MockMvc mvc) {
    this.mvc = mvc;
  }

  protected String getTemplate(String templateId) throws IOException {
    return FileUtils.readFileToString(
        new File(REPO_FOLDER + "/" + templateId + ".json"), 
        "utf-8");
  }


  protected void checkTemplateList(int qty) throws Exception {
    MvcResult mvcResult = mvc.perform(
        MockMvcRequestBuilders.get("/templates")
          .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

    int status = mvcResult.getResponse().getStatus();
    assertEquals(200, status);

    String content = mvcResult.getResponse().getContentAsString();

    JSONObject tomJsonObject = new JSONObject(content);
    JSONArray ids = tomJsonObject.getJSONArray("ids");

    assertTrue(ids.length() == qty, ids.toString());
  }


  protected void createOneFrom(String templateId, String newTemplateId) throws Exception {
    String template = FileUtils.readFileToString(
        new File(REPO_FOLDER + "/" + templateId + ".json"), 
        "utf-8");
    JSONObject parsed = new JSONObject(template);
    parsed.remove("templateId");
    parsed.put("templateId", newTemplateId);
    this.createOne(newTemplateId, parsed.toString());
  }


  protected void createOne(String templateId) throws Exception {
    String template = FileUtils.readFileToString(
        new File(REPO_FOLDER + "/" + templateId + ".json"), 
        "utf-8");
    this.createOne(templateId, template);
  }

  protected void createOne(String templateId, String template) throws Exception {

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/templates")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("UTF-8").content(template);

    this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.templateId", is(templateId)));
  }

  protected void deleteOne(String templateId) throws Exception {

    MockHttpServletRequestBuilder builder = 
        MockMvcRequestBuilders.delete("/templates/{templateId}", templateId)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .characterEncoding("UTF-8");

    this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());
  }

  protected void render(String templateId, String jsonData, String[] expected) throws Exception {

    logger.debug("in render test {} with {}", templateId, jsonData);

    // FileUtils.write(new File("test-templates-testing/jsonData.json"), jsonData, "UTF-8");
    

    MockHttpServletRequestBuilder builder = 
        MockMvcRequestBuilders
          .post("/templates/{templateId}/render", templateId)
          .characterEncoding("UTF-8")
          .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonData);

    MvcResult result = this.mvc
        .perform(builder)
        .andExpect(
          MockMvcResultMatchers.status().isOk()
        ).andReturn();
    
    logger.debug(result.getResponse().getCharacterEncoding());

    /*
    FileUtils.write(
        new File("test-templates-testing/received.txt"), 
        result.getResponse().getContentAsString(), 
        "UTF-8");
    */

    String content = result.getResponse().getContentAsString();

    // FileUtils.write(new File("test-templates-testing/content.txt"), content, "UTF-8");


    logger.debug("rendered content: {}", content);
    JSONObject jsonContent = new JSONObject(content);
    String renderedText = jsonContent.getString("renderedText");
    for (int i = 0; i < expected.length; i++) {
      assertTrue(renderedText.contains(expected[i]), expected[i] + " / " + renderedText);
    }
  }

  protected void confirmNoRender(String templateId, String jsonData) {  
    MockHttpServletRequestBuilder builder = 
        MockMvcRequestBuilders.post("/templates/{templateId}/render", templateId)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .characterEncoding("UTF-8").content(jsonData);
    assertThrows(Exception.class, () -> {
      this.mvc.perform(builder).andReturn();
    });
  }

  protected void reload(String templateId) throws Exception {
    MockHttpServletRequestBuilder builder = 
        MockMvcRequestBuilders.get("/templates/{templateId}/reload", templateId)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .characterEncoding("UTF-8");

    this.mvc.perform(builder);
  }

  protected void reload() throws Exception {
    MockHttpServletRequestBuilder builder = 
        MockMvcRequestBuilders.get("/templates/reload")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .characterEncoding("UTF-8");

    this.mvc.perform(builder);
  }

}
