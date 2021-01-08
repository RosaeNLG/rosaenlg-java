package org.rosaenlg.user;

/*-
 * #%L
 * rosaenlg-java-wrapper-user
 * %%
 * Copyright (C) 2020 - 2021 RosaeNLG.org, Ludan Stoecklé
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

import java.io.File;
import org.rosaenlg.lib.RosaeContext;
import org.rosaenlg.lib.CompileInfo;
import org.json.JSONObject;

public class Main {

  public static void main(String[] args) throws Exception {
    CompileInfo compileOpts = new CompileInfo();
    compileOpts.setLanguage("fr_FR");

    final RosaeContext rc = new RosaeContext("main.pug", "templates", compileOpts);

    JSONObject renderOpts = new JSONObject();
    renderOpts.put("language", "fr_FR");
    JSONObject chanson = new JSONObject();
    chanson.put("nom", "Non, je ne regrette rien");
    chanson.put("auteur", "Édith Piaf");
    renderOpts.put("chanson", chanson);

    String rendered = rc.render(renderOpts.toString()).getRenderedText();

    System.out.println(rendered);
    
  }
}
