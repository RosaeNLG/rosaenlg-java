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

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestApplicationPersistence extends AbstractTest {

  private static final String TEST_FOLDER = "test-templates-testing";

  static {
    System.setProperty("rosaenlg.homedir", TEST_FOLDER);
  }

  //@SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(TestApplicationPersistence.class);
  private ApplicationTestHelper ath;

  @Override
  @BeforeAll
  public void setUp() {
    logger.debug("rosaenlg.homedir system property: {}", System.getProperty("rosaenlg.homedir"));
    super.setUp();
    // mvc does not exist before
    ath = new ApplicationTestHelper(this.mvc);
  }

  @Test
  public void fileIsSaved() throws Exception {
    ath.createOneFrom("basic_a", "basic_fileIsSaved");
    assertTrue(new File(TEST_FOLDER + "/basic_fileIsSaved.json").exists());
    ath.deleteOne("basic_fileIsSaved");
  }

  @Test
  public void createDelete() throws Exception {
    ath.createOneFrom("basic_a", "createDelete");

    // we can render
    String opts = "{ \"language\": \"en_US\" }";
    ath.render("createDelete", opts, new String[] { "Aaa" }, null);

    // it is on the disk
    assertTrue(new File(TEST_FOLDER + "/createDelete.json").exists());

    ath.deleteOne("createDelete");

    // has been deleted
    assertFalse(new File(TEST_FOLDER + "/createDelete.json").exists());
  }

  @Test
  public void reload() throws Exception {
    ath.createOneFrom("basic_a", "reload");

    String opts = "{ \"language\": \"en_US\" }";

    // we can render
    ath.render("reload", opts, new String[] { "Aaa" }, null);

    // we modify the file
    File file = new File(TEST_FOLDER + "/reload.json");
    String originalTemplate = FileUtils.readFileToString(file, "utf-8");
    logger.debug("original template: {}", originalTemplate);
    String modifiedTemplate = originalTemplate.replace("aaa", "bbb").replace("Aaa", "Bbb");
    FileUtils.writeStringToFile(file, modifiedTemplate, "utf-8");

    // still can render, did not change
    ath.render("reload", opts, new String[] { "Aaa" }, null);

    // reload
    ath.reload("reload");

    // can render, but changed
    ath.render("reload", opts, new String[] { "Bbb" }, null);

    // put the original again
    FileUtils.writeStringToFile(file, originalTemplate, "utf-8");

    // reload all
    ath.reload();
    
    // back to original
    ath.render("reload", opts, new String[] { "Aaa" }, null);

    ath.deleteOne("reload");
    assertFalse(new File(TEST_FOLDER + "/reload.json").exists());
  }

  @Test
  public void deleteNotExists() throws Exception {
    assertThrows(Exception.class, () -> {
      ath.deleteOne("toto");
    });
  }


  @AfterEach 
  @BeforeEach
  protected void checkEmptyList() throws Exception {
    ath.checkTemplateList(0);
    File[] list = new File(TEST_FOLDER).listFiles();
    assertEquals(list.length, 0, list.toString());
  }

}
