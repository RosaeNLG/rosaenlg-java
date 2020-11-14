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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Template creation or update response.
 *
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class CreateTemplateStatus {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(CreateTemplateStatus.class);

  private String templateId;
  private CreateStatus status;

  
  /** Constructor.
   * 
   * @param templateId the ID of the templace
   * @param status the creation status
   */
  public CreateTemplateStatus(String templateId, CreateStatus status) {
    this.templateId = templateId;
    this.status = status;
  }

  
  /** Getter on templateId.
   * @return String the ID of the template
   */
  public String getTemplateId() {
    return templateId;
  }

  
  /** Getter on status.
   * @return CreateStatus the creation status
   */
  public CreateStatus getStatus() {
    return status;
  }

}
