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
    String rendered = store.render("basic_a", opts).getText();
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

}
