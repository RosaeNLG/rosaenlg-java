package org.rosaenlg.user;

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
