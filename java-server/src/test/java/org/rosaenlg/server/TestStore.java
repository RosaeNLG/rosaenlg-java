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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestStore {

  private static final Logger logger = LoggerFactory.getLogger(TestStore.class);

  private FilesHelper filesHelper = new FilesHelper(
      "test-templates-repo", 
      "test-templates-testing");

  @AfterEach
  void cleanUp() throws Exception {
    filesHelper.cleanTest();
  }

  @Test
  void testReload() throws Exception {

    filesHelper.copyToTest("basic_a.json");
    Store store = new Store("test-templates-testing");
    assertTrue(store.templateLoaded("basic_a"));

    assertFalse(store.templateLoaded("basic_b"));
    filesHelper.copyToTest("basic_b.json");
    assertFalse(store.templateLoaded("basic_b"));
    store.reloadExistingTemplate("basic_b");
    assertTrue(store.templateLoaded("basic_b"));

    filesHelper.cleanTest();
    filesHelper.copyToTest("basic_b.json");

    assertTrue(store.templateLoaded("basic_a"));
    assertTrue(store.templateLoaded("basic_b"));

    store.reloadExistingTemplates();

    assertFalse(store.templateLoaded("basic_a"));
    assertTrue(store.templateLoaded("basic_b"));

    assertThrows(Exception.class, () -> {
      store.reloadExistingTemplate("blablabla");
    });
  }

  @Test
  void getTemplateIds() throws Exception {
    filesHelper.copyToTest("basic_a.json");
    filesHelper.copyToTest("basic_b.json");

    Store store = new Store("test-templates-testing");

    List<String> ids = store.getTemplateIds();
    assertEquals(2, ids.size(), ids.toString());

    assertTrue(store.templateLoaded("basic_a"));
    assertTrue(store.templateLoaded("basic_b"));
  }

  @Test
  void render() throws Exception {
    filesHelper.copyToTest("basic_a.json");

    Store store = new Store("test-templates-testing");
    String opts = "{ \"language\": \"en_US\" }";
    String rendered = store.render("basic_a", opts).getRenderedText();
    logger.debug("rendered: ", rendered);
    assertEquals("<p>Aaa</p>", rendered);
  }

  @Test
  void reloadNoPath() throws Exception {
    Store store = new Store(null);
    assertThrows(Exception.class, () -> {
      store.reloadExistingTemplates();
    });
  }

  @Test
  void createInvalidPath() throws Exception {
    Exception thrown = assertThrows(Exception.class, () -> {
      new Store("+sfsdfè\\sdfdsf");
    });
    assertTrue(thrown instanceof StoreConstructorException);
  }

  @Test
  void deleteNoTemplatesPath() throws Exception {
    Store store = new Store(null);
    Exception thrown = assertThrows(Exception.class, () -> {
      store.deleteTemplateFileAndUnload("bla");
    });
    assertTrue(thrown instanceof DeleteTemplateException);
    assertTrue(thrown.getCause() instanceof UnloadTemplateException);
  }

  @Test
  void renderTemplateDoesNotExist() throws Exception {
    Store store = new Store(null);
    Exception thrown = assertThrows(Exception.class, () -> {
      store.render("bla", "{}");
    });
    assertTrue(thrown instanceof RenderException);
    assert(thrown.getMessage().contains("not found"));
  }

  @Test
  void renderFails() throws Exception {
    filesHelper.copyToTest("basic_a.json");

    Store store = new Store("test-templates-testing");
    String opts = "{}"; // no language

    Exception thrown = assertThrows(Exception.class, () -> {
      store.render("basic_a", opts);
    });
    assertTrue(thrown instanceof RenderException);
    assert(thrown.getMessage().contains("could not render"));
  }

  @Test
  void loadExistingNoTemplatesPath() throws Exception {
    Store store = new Store(null);
    Exception thrown = assertThrows(Exception.class, () -> {
      store.loadExistingTemplates();
    });
    assertTrue(thrown instanceof NoTemplatesPathException);
    assert(thrown.getMessage().contains("templates path"));
  }

  @Test
  void storeWrongExplicitPath() throws Exception {
    Exception thrown = assertThrows(Exception.class, () -> {
      new Store("sf^sfds°");
    });
    assertTrue(thrown instanceof StoreConstructorException);
  }

  @Test
  void storeWrongSystemPropertyPath() throws Exception {
    System.setProperty("rosaenlg.homedir", "sf^sfds°");
    Exception thrown = assertThrows(Exception.class, () -> {
      new Store();
    });
    assertTrue(thrown instanceof StoreConstructorException);
    System.clearProperty("rosaenlg.homedir");
  }

  @Test
  void storeGoodSystemPropertyPath() throws Exception {
    filesHelper.copyToTest("basic_a.json");
    System.setProperty("rosaenlg.homedir", "test-templates-testing");

    Store store = new Store();
    String opts = "{ \"language\": \"en_US\" }";
    String rendered = store.render("basic_a", opts).getRenderedText();
    logger.debug("rendered: ", rendered);
    assertEquals("<p>Aaa</p>", rendered);
    System.clearProperty("rosaenlg.homedir");
  }

}
