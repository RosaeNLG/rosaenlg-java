package org.rosaenlg.server;

/*-
 * #%L
 * org.rosaenlg:java-server
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class TestApplicationNoPersistence extends AbstractTest {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(TestApplicationNoPersistence.class);
  private ApplicationTestHelper ath;

  static {
    System.clearProperty("rosaenlg.homedir");
  }

  @Override
  @BeforeAll
  void setUp() {
    super.setUp();
    // mvc does not exist before
    ath = new ApplicationTestHelper(this.mvc);
  }

  @Test
  void testRenderWithParam() throws Exception {
    ath.checkTemplateList(0);
    ath.createOne("chanson");
    ath.checkTemplateList(1);

    JSONObject opts = new JSONObject();
    opts.put("language", "fr_FR");
    JSONObject chanson = new JSONObject();
    chanson.put("nom", "Non, je ne regrette rien");
    chanson.put("auteur", "Édith Piaf");
    opts.put("chanson", chanson);

    ath.render("chanson", opts.toString(), new String[] { "Il chantera \"Non, je ne regrette rien\" d'Édith Piaf" },
        null);

    ath.deleteOne("chanson");
  }

  @Test
  void testRenderTemplateDoesNotExist() throws Exception {
    ath.checkTemplateList(0);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/templates/{templateId}/render", "blablabla")
        .characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE).content("");

    this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());

  }

  @Test
  void testUpdate() throws Exception {
    ath.checkTemplateList(0);

    String templateOriginal = ath.getTemplate("basic_a");

    ath.createOne("basic_a", templateOriginal);
    ath.checkTemplateList(1);
    String opts = "{ \"language\": \"en_US\" }";
    ath.render("basic_a", opts, new String[] { "Aaa" }, null);

    String templateModified = templateOriginal.replaceAll("aaa", "ccc").replaceAll("Aaa", "Ccc");
    ath.createOne("basic_a", templateModified);
    ath.checkTemplateList(1);
    ath.render("basic_a", opts, new String[] { "Ccc" }, null);

    ath.createOne("basic_a", templateOriginal);
    ath.checkTemplateList(1);
    ath.render("basic_a", opts, new String[] { "Aaa" }, null);

    ath.deleteOne("basic_a");

  }

  @Test
  void getTemplate() throws Exception {
    ath.createOne("basic_a");

    MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/templates/{templateId}/template", "basic_a")
        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    int status = mvcResult.getResponse().getStatus();
    assertEquals(200, status);

    String content = mvcResult.getResponse().getContentAsString();

    logger.debug("get content: {}", content);

    assertTrue(content.contains("aaa"));
    assertTrue(content.contains("en_US"));
    assertTrue(content.contains("Aaa"));

    ath.deleteOne("basic_a");
  }

  @Test
  void getTemplateNotExists() throws Exception {

    Exception thrown = assertThrows(Exception.class, () -> {
      this.mvc.perform(MockMvcRequestBuilders.get("/templates/{templateId}/template", "blablabla")
          .accept(MediaType.APPLICATION_JSON_VALUE));
    });

    assertTrue(((NestedServletException) thrown).getRootCause() instanceof FullTemplateException);
  }

  @Test
  void testReloadException() throws Exception {
    ath.createOne("basic_a");

    try {
      ath.reload("basic_a");
    } catch (Exception e) {
      assertTrue(e instanceof NestedServletException);
      assertTrue(((NestedServletException) e).getRootCause() instanceof NoTemplatesPathException);
    }

    try {
      ath.reload();
    } catch (Exception e) {
      assertTrue(e instanceof NestedServletException);
      assertTrue(((NestedServletException) e).getRootCause() instanceof NoTemplatesPathException);
    }

    ath.deleteOne("basic_a");
  }

  @Test
  void deleteNotExists() throws Exception {
    assertThrows(Exception.class, () -> {
      ath.deleteOne("toto");
    });
  }

  @Test
  void createTemplateWithNoId() throws Exception {
    String original = ath.getTemplate("basic_a");
    JSONObject parsed = new JSONObject(original);
    parsed.remove("templateId");

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/templates")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("UTF-8").content(parsed.toString());

    assertThrows(Exception.class, () -> {
      this.mvc.perform(builder);
    });
  }

  @Test
  void testRenderWithOutputData() throws Exception {
    ath.checkTemplateList(0);
    ath.createOne("outputdata");
    ath.checkTemplateList(1);

    JSONObject opts = new JSONObject();
    opts.put("language", "fr_FR");
    JSONObject input = new JSONObject();
    input.put("field", 1);
    opts.put("input", input);

    ath.render("outputdata", opts.toString(), new String[] { "Bla bla" },
        "{\"val\":2,\"obj\":{\"aaa\":\"bbb\"},\"foo\":\"bar\"}");

    ath.deleteOne("outputdata");
  }


  @Test
  void testRenderDebug() throws Exception {
    ath.checkTemplateList(0);
    ath.createOne("chanson");
    ath.checkTemplateList(1);

    JSONObject opts = new JSONObject();
    opts.put("language", "fr_FR");
    opts.put("renderDebug", true);
    JSONObject chanson = new JSONObject();
    chanson.put("nom", "Non, je ne regrette rien");
    chanson.put("auteur", "Édith Piaf");
    opts.put("chanson", chanson);

    ath.render("chanson", opts.toString(), new String[] { "rosaenlg-debug", "Édith Piaf" },
        null);

    ath.deleteOne("chanson");
  }

  @AfterEach
  void checkEmptyList() throws Exception {
    ath.checkTemplateList(0);
  }

}
