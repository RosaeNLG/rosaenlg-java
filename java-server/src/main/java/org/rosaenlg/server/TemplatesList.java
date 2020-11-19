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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A list of projects (= templates).
 *
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class TemplatesList {

  private static final Logger logger = LoggerFactory.getLogger(TemplatesList.class);

  private final List<String> ids = new ArrayList<>();
  
  /** Constructor.
   * @param ids list of string of template IDs
   */
  public TemplatesList(List<String> ids) {
    logger.debug("constructor with {}", ids);
    this.ids.addAll(ids);
  }

  
  /** Getter on the list of IDs.
   * @return the list of Is.
   */
  public List<String> getIds() {
    return ids;
  }
  
}
