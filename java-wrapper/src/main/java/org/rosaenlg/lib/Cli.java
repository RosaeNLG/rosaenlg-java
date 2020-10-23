package org.rosaenlg.lib;

/*-
 * #%L
 * RosaeNLG for Java
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
