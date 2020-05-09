package org.rosaenlg.lib;

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestRosaeContext {

  // @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(TestRosaeContext.class);

  @Test
  public void createRender() throws Exception {
    
    String jsonPackage = FileUtils.readFileToString(
        new File("test-templates-repo/test_inc_no_comp.json"), 
        "utf-8");
    RosaeContext rc = new RosaeContext(jsonPackage);

    assertEquals(rc.getTemplateId(), "test_inc_no_comp");

    String opts = "{ \"language\": \"en_US\" }";
    for (int i = 0; i < 10; i++) {
      String rendered = rc.render(opts);
      logger.debug("rendered: {}", rendered);
      assertEquals(rendered, "<p>Bla included</p>");
    }
  }


  @Test
  public void createRender2Different() throws Exception {
    String[] letters = {"a", "b"};
    for (int i = 0; i < letters.length; i++) {
      String letter = letters[i];

      String jsonPackage = FileUtils.readFileToString(
          new File("test-templates-repo/basic_" + letter + ".json"),
          "utf-8");
      RosaeContext rc = new RosaeContext(jsonPackage);

      assertEquals(rc.getTemplateId(), "basic_" + letter);

      String opts = "{ \"language\": \"en_US\" }";
      for (int j = 0; j < 10; j++) {
        String rendered = rc.render(opts);
        assertTrue(rendered.contains(letter));
      }

    }
  }

  @Test
  public void createRenderLots() throws Exception {

    String jsonPackage = FileUtils.readFileToString(
        new File("test-templates-repo/basic_a.json"),
        "utf-8");

    for (int i = 0; i < 5; i++) {
      // hack the template
      String newJsonPackage = jsonPackage
          .replace("basic_a", "basic_a" + i)
          .replace("aaa", "aaa" + i)
          .replace("Aaa", "Aaa" + i);

      RosaeContext rc = new RosaeContext(newJsonPackage);

      assertEquals(rc.getTemplateId(), "basic_a" + i);

      String opts = "{ \"language\": \"en_US\" }";
      for (int j = 0; j < 10; j++) {
        String rendered = rc.render(opts);
        assertTrue(rendered.contains("Aaa" + i));
      }

    }
  }

  @Test
  public void failAutotest() throws Exception {
    String jsonPackage = FileUtils.readFileToString(
        new File("test-templates-repo/basic_a.json"),
        "utf-8");

    // hack the template
    String newJsonPackage = jsonPackage.replace("Aaa", "Xxx");

    assertThrows(Exception.class, () -> {
      new RosaeContext(newJsonPackage);
    });

  }

  @Test
  public void noAutotestAtAll() throws Exception {
    String jsonPackage = FileUtils.readFileToString(
        new File("test-templates-repo/basic_a.json"), 
        "utf-8");
    JSONObject parsed = new JSONObject(jsonPackage);
    assertTrue(parsed.has("autotest"));
    parsed.remove("autotest");
    RosaeContext rc = new RosaeContext(parsed.toString());

    String opts = "{ \"language\": \"en_US\" }";
    String rendered = rc.render(opts);
    assertTrue(rendered.contains("Aaa"));
  }

  @Test
  public void autotestHereNotActivated() throws Exception {
    String jsonPackage = FileUtils.readFileToString(
        new File("test-templates-repo/basic_a.json"), 
        "utf-8");
    JSONObject parsed = new JSONObject(jsonPackage);
    assertTrue(parsed.has("autotest"));
    assertTrue(parsed.getJSONObject("autotest").getBoolean("activate"));
    parsed.getJSONObject("autotest").remove("activate");
    parsed.getJSONObject("autotest").put("activate", false);

    RosaeContext rc = new RosaeContext(parsed.toString());

    String opts = "{ \"language\": \"en_US\" }";
    String rendered = rc.render(opts);
    assertTrue(rendered.contains("Aaa"));
  }

  @Test
  public void noPackagingWithInclude() throws Exception {
    CompileOptions opts = new CompileOptions();
    opts.setLanguage("en_US");

    RosaeContext rc = new RosaeContext(
        "test.pug",
        new File("test-templates-repo/includes"),
        opts);

    String rendered = rc.render(opts.toJson());
    assertEquals(rendered, "<p>Bla included</p>");
  }

  @Test
  public void compileClientWithEmbed() throws Exception {
    
    String jsonPackage = FileUtils.readFileToString(
        new File("test-templates-repo/verb_fr.json"), 
        "utf-8");
    RosaeContext rc = new RosaeContext(jsonPackage);

    assertTrue(rc.getJsonPackageAsString().contains("tense:'FUTUR'"));

    String compiledClient = rc.getCompiledClient();

    // resources are well embedded
    assertTrue(compiledClient.contains("chanteront"));

    rc.destroy();
    
    // logger.info(compiledClient);
  }


  @Test
  public void compileClientWithEmbedSpecific() throws Exception {

    CompileOptions opts = new CompileOptions();
    opts.setLanguage("fr_FR");
    opts.setName("testName");
    opts.setWords(Arrays.asList(new String[]{"travail", "maison"}));
    opts.setVerbs(Arrays.asList(new String[]{"promener"}));

    RosaeContext rc = new RosaeContext(
        "verb_fr.pug",
        new File("test-templates-repo"),
        opts);

    String compiledClient = rc.getCompiledClient();

    // logger.info(compiledClient);

    // name is ok
    assertTrue(compiledClient.contains("testName"), compiledClient);

    // resources are well embedded
    String[] expected = new String[]{"chanteront", "\"travail\":{\"gender\":\"M\"", "maison", "promènes"};
    for (int i = 0; i < expected.length; i++) {
      assertTrue(
          compiledClient.contains(expected[i]), 
          "expected: " + expected[i]);
    }
    
  }


  @Test
  public void compileClientWithEmbedSpecificUsingJsonOpts() throws Exception {

    CompileOptions opts = new CompileOptions(
        new JSONObject(
          "{\"language\": \"fr_FR\", "
          + "\"words\": [\"travail\", \"maison\"], "
          + "\"verbs\": [\"promener\"]}"
        ));
    logger.info(opts.toJson());
    RosaeContext rc = new RosaeContext(
        "verb_fr.pug",
        new File("test-templates-repo"),
        opts);

    String compiledClient = rc.getCompiledClient();

    // logger.info(compiledClient);

    // resources are well embedded
    String[] expected = new String[]{"chanteront", "\"travail\":{\"gender\":\"M\"", "maison", "promènes"};
    for (int i = 0; i < expected.length; i++) {
      assertTrue(
          compiledClient.contains(expected[i]), 
          "expected: " + expected[i]);
    }
    
  }
  
  @Test
  public void renderError() throws Exception {

    String template = "p\n" + "  | il #[+verb(getAnonMS(), {verb: 'chanter', tense:'FUTUR'} )]\n"
        + "  | \"#{chanson.nom}\"\n" + "  | de #{chanson.auteur}\n";

    CompileOptions compileOptions = new CompileOptions();
    compileOptions.setLanguage("fr_FR");

    RosaeContext rc = new RosaeContext(template, compileOptions);

    JSONObject opts = new JSONObject();
    opts.put("language", "fr_FR");

    Exception thrown = assertThrows(Exception.class,
        () -> {
          rc.render(opts.toString());
        });
    assertTrue(thrown.getMessage().contains("Cannot read property 'nom' of undefined"));
  }

  @Test
  public void testCompileError() throws Exception {
    try {
      CompileOptions opts = new CompileOptions();
      opts.setLanguage("en_US");

      new RosaeContext(
          "comp_errors.pug",
          new File("test-templates-repo"),
          opts);

      fail("Exception did not throw!");
    } catch (Exception e) {
      // System.out.println(e.getMessage());
      assertTrue(e.getMessage().contains("Unexpected token"));
      assertTrue(e.getMessage().contains("  > 5|   if true +!= false"));
      assertTrue(e.getMessage().contains("------------------^"));
    }
  }

  private static String getJsonPhone1() {
    JSONObject jo = new JSONObject();
    jo.put("name", "OnePlus 5T");
    JSONArray colors = new JSONArray();
    colors.put("Black").put("Red").put("White");
    jo.put("colors", colors);
    jo.put("displaySize", 6);
    jo.put("screenRatio", 80.43);
    jo.put("battery", 3300);

    JSONObject joWrapper = new JSONObject();
    joWrapper.put("language", "en_US");
    joWrapper.put("phone", jo);

    return joWrapper.toString();
  }

  private static String getJsonPhone2() {
    JSONObject jo = new JSONObject();
    jo.put("name", "OnePlus 5");
    JSONArray colors = new JSONArray();
    colors.put("Gold").put("Gray");
    jo.put("colors", colors);
    jo.put("displaySize", 5.5);
    jo.put("screenRatio", 72.93);
    jo.put("battery", 3300);

    JSONObject joWrapper = new JSONObject();
    joWrapper.put("language", "en_US");
    joWrapper.put("phone", jo);

    return joWrapper.toString();
  }

  private static String getJsonPhone3() {
    JSONObject jo = new JSONObject();
    jo.put("name", "OnePlus 3T");
    JSONArray colors = new JSONArray();
    colors.put("Black").put("Gold").put("Gray");
    jo.put("colors", colors);
    jo.put("displaySize", 5.5);
    jo.put("screenRatio", 73.14);
    jo.put("battery", 3400);

    JSONObject joWrapper = new JSONObject();
    joWrapper.put("language", "en_US");
    joWrapper.put("phone", jo);

    return joWrapper.toString();
  }

  @Test
  public void testRenderWithDynamicData() throws Exception {
    CompileOptions opts = new CompileOptions();
    opts.setLanguage("en_US");
    RosaeContext rc = new RosaeContext(
        "tutorial_en_US_nodata.pug",
        new File("test-templates-repo"),
        opts);

    String rendered1 = rc.render(getJsonPhone1());
    // System.out.println(rendered1);
    assertTrue(rendered1.contains("OnePlus 5T"));
    assertTrue(rendered1.contains("80.43"));

    String rendered2 = rc.render(getJsonPhone2());
    // System.out.println(rendered2);
    assertTrue(rendered2.contains("OnePlus 5"));
    assertTrue(rendered2.contains("5.5"));

    String rendered3 = rc.render(getJsonPhone3());
    // System.out.println(rendered3);
    assertTrue(rendered3.contains("OnePlus 3T"));
    assertTrue(rendered3.contains("73.14"));
    assertTrue(rendered3.contains("Black"));
  }

  @Test
  public void testNoLanguage() throws Exception {

    CompileOptions opts = new CompileOptions();

    Exception thrown = assertThrows(Exception.class,
        () -> {
          new RosaeContext(
            "verb_fr.pug",
            new File("test-templates-repo"),
            opts);
        });
    // logger.info(thrown.toString());
    assertTrue(thrown.getMessage().contains("language"), thrown.toString());
  }

  @Test
  public void testDocExemple() throws Exception {

    CompileOptions compileOpts = new CompileOptions();
    compileOpts.setLanguage("fr_FR");

    final RosaeContext rc = new RosaeContext(
        "chanson.pug",
        new File("test-templates-repo"),
        compileOpts);

    JSONObject renderOpts = new JSONObject();
    renderOpts.put("language", "fr_FR");
    JSONObject chanson = new JSONObject();
    chanson.put("nom", "Non, je ne regrette rien");
    chanson.put("auteur", "Édith Piaf");
    renderOpts.put("chanson", chanson);

    String rendered = rc.render(renderOpts.toString());

    assertTrue(
        rendered.contains("Il chantera \"Non, je ne regrette rien\" d'Édith Piaf"), 
        rendered);
  }

}
