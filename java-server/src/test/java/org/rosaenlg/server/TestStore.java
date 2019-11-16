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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStore {

  private static final Logger logger = LoggerFactory.getLogger(TestStore.class);

  private FilesHelper filesHelper = new FilesHelper(
      "test-templates-repo", 
      "test-templates-testing");

  @AfterEach
  public void cleanUp() throws Exception {
    filesHelper.cleanTest();
  }

  @Test
  public void testReload() throws Exception {

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
  public void getTemplateIds() throws Exception {
    filesHelper.copyToTest("basic_a.json");
    filesHelper.copyToTest("basic_b.json");

    Store store = new Store("test-templates-testing");

    List<String> ids = store.getTemplateIds();
    assertTrue(ids.size() == 2, ids.toString());

    assertTrue(store.templateLoaded("basic_a"));
    assertTrue(store.templateLoaded("basic_b"));
  }

  @Test
  public void render() throws Exception {
    filesHelper.copyToTest("basic_a.json");

    Store store = new Store("test-templates-testing");
    String opts = "{ \"language\": \"en_US\" }";
    String rendered = store.render("basic_a", opts);
    logger.debug("rendered: ", rendered);
    assertEquals(rendered, "<p>Aaa</p>");
  }

  @Test
  public void reloadNoPath() throws Exception {
    Store store = new Store(null);
    assertThrows(Exception.class, () -> {
      store.reloadExistingTemplates();
    });
  }

}
