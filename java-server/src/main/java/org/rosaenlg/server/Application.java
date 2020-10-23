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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
    title = "RosaeNLG API", 
    version = "1.4.1", 
    description = "API over the Natural Language Generation library RosaeNLG."
      + "Server is written in Java.", 
    license = @License(name = "MIT", url = "http://www.opensource.org/licenses/mit-license.php"), 
    contact = @Contact(url = "https://rosaenlg.org", name = "Ludan Stoecklé", email = "ludan.stoeckle@rosaenlg.org")),
    externalDocs = @ExternalDocumentation(description = "RosaeNLG reference documentation", url = "https://rosaenlg.org"))
public class Application extends SpringBootServletInitializer {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(Application.class);
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
