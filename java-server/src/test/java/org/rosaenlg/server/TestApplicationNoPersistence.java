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

import static org.hamcrest.Matchers.is;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class TestApplicationNoPersistence extends AbstractTest {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(TestApplicationNoPersistence.class);
  private ApplicationTestHelper ath;

  @Override
  @BeforeAll
  public void setUp() {
    super.setUp();
    // mvc does not exist before
    ath = new ApplicationTestHelper(this.mvc);
  }

  /*
  @Test
  public void testCreateWithPost() throws Exception {
    String template = ath.getTemplate("basic_a");

    // just testing that also works with POST method
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/templates")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
        .characterEncoding("UTF-8").content(template);

    this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.templateId", is("basic_a")));

    ath.checkTemplateList(1);

    String opts = "{ \"language\": \"en_US\" }";
    ath.render("basic_a", opts, new String[] { "Aaa" });

    ath.deleteOne("basic_a");
  }

  @Test
  public void createAnDelete() throws Exception {
    ath.checkTemplateList(0);
    ath.createOne("basic_a");
    ath.checkTemplateList(1);
    ath.deleteOne("basic_a");
    ath.checkTemplateList(0);

    ath.confirmNoRender("basic_a", "{ \"language\": \"en_US\" }");
    ath.confirmNoRender("basic_b", "{ \"language\": \"en_US\" }");

  }

  @Test
  public void checkIdemPotent() throws Exception {
    ath.checkTemplateList(0);
    ath.createOne("basic_a");
    ath.createOne("basic_a");
    ath.createOne("basic_a");
    ath.checkTemplateList(1);
    ath.deleteOne("basic_a");
    ath.checkTemplateList(0);
    ath.confirmNoRender("basic_a", "{ \"language\": \"en_US\" }");
  }

  @Test
  public void createFromScratch() throws Exception {
    ath.checkTemplateList(0);
    ath.createOne("basic_a");
    ath.checkTemplateList(1);

    String opts = "{ \"language\": \"en_US\" }";
    ath.render("basic_a", opts, new String[] { "Aaa" });

    ath.createOne("basic_b");
    ath.checkTemplateList(2);

    ath.deleteOne("basic_a");
    ath.checkTemplateList(1);

    ath.deleteOne("basic_b");

  }

  */
  @Test
  public void testRenderWithParam() throws Exception {
    ath.checkTemplateList(0);
    ath.createOne("chanson");
    ath.checkTemplateList(1);

    JSONObject opts = new JSONObject();
    opts.put("language", "fr_FR");
    JSONObject chanson = new JSONObject();
    chanson.put("nom", "Non, je ne regrette rien");
    chanson.put("auteur", "Édith Piaf");
    opts.put("chanson", chanson);

    ath.render(
        "chanson", 
        opts.toString(), 
        new String[] { "Il chantera \"Non, je ne regrette rien\" d'Édith Piaf" });

    ath.deleteOne("chanson");
  }

  @Test
  public void testUpdate() throws Exception {
    ath.checkTemplateList(0);

    String templateOriginal = ath.getTemplate("basic_a");

    ath.createOne("basic_a", templateOriginal);
    ath.checkTemplateList(1);
    String opts = "{ \"language\": \"en_US\" }";
    ath.render("basic_a", opts, new String[] { "Aaa" });

    String templateModified = templateOriginal.replaceAll("aaa", "ccc").replaceAll("Aaa", "Ccc");
    ath.createOne("basic_a", templateModified);
    ath.checkTemplateList(1);
    ath.render("basic_a", opts, new String[] { "Ccc" });

    ath.createOne("basic_a", templateOriginal);
    ath.checkTemplateList(1);
    ath.render("basic_a", opts, new String[] { "Aaa" });

    ath.deleteOne("basic_a");

  }

  @Test
  public void getTemplate() throws Exception {
    ath.createOne("basic_a");

    MvcResult mvcResult = mvc
        .perform(
            MockMvcRequestBuilders.get("/templates/{templateId}/template", "basic_a")
              .accept(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

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
  public void testReloadException() throws Exception {
    ath.createOne("basic_a");
    assertThrows(Exception.class, () -> {
      ath.reload("basic_a");
    });
    assertThrows(Exception.class, () -> {
      ath.reload();
    });
    ath.deleteOne("basic_a");
  }

  @Test
  public void deleteNotExists() throws Exception {
    assertThrows(Exception.class, () -> {
      ath.deleteOne("toto");
    });
  }

  @Test
  public void createTemplateWithNoId() throws Exception {
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

  @AfterEach
  public void checkEmptyList() throws Exception {
    ath.checkTemplateList(0);
  }

}
