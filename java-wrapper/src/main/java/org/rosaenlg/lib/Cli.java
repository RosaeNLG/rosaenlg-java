package org.rosaenlg.lib;

/*-
 * #%L
 * RosaeNLG for Java
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

import org.json.JSONObject;

public class Cli {

  /**
   * Self test, no params.
   * <p>
   * Renders a simple included template in French. Should print <i>Il chantera
   * "Non, je ne regrette rien" d'Édith Piaf</i>
   * </p>
   * 
   * @param args not used as of today
   * @throws Exception if a problem occurs
   */
  public static void main(String[] args) throws Exception {

    String template = "p\n" + "  | il #[+verb(getAnonMS(), {verb: 'chanter', tense:'FUTUR'} )]\n"
        + "  | \"#{chanson.nom}\"\n" + "  | de #{chanson.auteur}\n";

    CompileOptions compileOptions = new CompileOptions();
    compileOptions.setLanguage("fr_FR");

    final RosaeContext rosaeContext = new RosaeContext(template, compileOptions);

    JSONObject opts = new JSONObject();
    opts.put("language", "fr_FR");
    JSONObject chanson = new JSONObject();
    chanson.put("nom", "Non, je ne regrette rien");
    chanson.put("auteur", "Édith Piaf");
    opts.put("chanson", chanson);

    String rendered = rosaeContext.render(opts.toString());
    System.out.println(rendered);
  }
}
