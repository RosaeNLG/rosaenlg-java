package org.rosaenlg.server;

import io.swagger.v3.oas.annotations.ExternalDocumentation;

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

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Application.
 *
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
    title = "RosaeNLG API", 
    version = "1.18.1",
    description = "API over the Natural Language Generation library RosaeNLG. "
      + "Server is written in Java.", 
    license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html"), 
    contact = @Contact(url = "https://rosaenlg.org", name = "Ludan Stoecklé", email = "contact@rosaenlg.org")),
    externalDocs = @ExternalDocumentation(description = "RosaeNLG reference documentation", url = "https://rosaenlg.org"))
public class Application extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(Application.class);
  }



  /** Main. Is it called?
   * 
   * @param args will be given to SpringApplication.run
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
