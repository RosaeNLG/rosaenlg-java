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
    ath.render("createDelete", opts, new String[] { "Aaa" });

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
    ath.render("reload", opts, new String[] { "Aaa" });

    // we modify the file
    File file = new File(TEST_FOLDER + "/reload.json");
    String originalTemplate = FileUtils.readFileToString(file, "utf-8");
    logger.debug("original template: {}", originalTemplate);
    String modifiedTemplate = originalTemplate.replace("aaa", "bbb").replace("Aaa", "Bbb");
    FileUtils.writeStringToFile(file, modifiedTemplate, "utf-8");

    // still can render, did not change
    ath.render("reload", opts, new String[] { "Aaa" });

    // reload
    ath.reload("reload");

    // can render, but changed
    ath.render("reload", opts, new String[] { "Bbb" });

    // put the original again
    FileUtils.writeStringToFile(file, originalTemplate, "utf-8");

    // reload all
    ath.reload();
    
    // back to original
    ath.render("reload", opts, new String[] { "Aaa" });

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
    assertEquals(new File(TEST_FOLDER).listFiles().length, 0);
  }

}
