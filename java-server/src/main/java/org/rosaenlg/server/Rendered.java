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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rendered {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(Rendered.class);

  private String templateId;
  private String renderedText;

  private RenderOptionsForSerialize renderOptions;

  private long counter;
  private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

  
  /** Constructor.
   * @param templateId the template ID
   * @param renderedText the rendered text
   * @param counter the value of the counter
   * @param renderOptions the rendering options
   */
  public Rendered(
      String templateId, 
      String renderedText, 
      long counter, 
      RenderOptionsForSerialize renderOptions) {
    this.templateId = templateId;
    this.renderedText = renderedText;
    this.counter = counter;
    this.renderOptions = renderOptions;
  }

  
  /** Getter on rendering options.
   * @return RenderOptionsForSerialize the rendering options
   */
  public RenderOptionsForSerialize getRenderOptions() {
    return renderOptions;
  }

  
  /** Getter on template ID.
   * @return String the template ID
   */
  public String getTemplateId() {
    return templateId;
  }

  
  /** Getter on rendered text.
   * @return String rendered text
   */
  public String getRenderedText() {
    return renderedText;
  }

  
  /** Getter on countner.
   * @return long counter current value
   */
  public long getCounter() {
    return counter;
  }

  
  /** Getter on timestamp.
   * @return Timestamp
   */
  public Timestamp getTimestamp() {
    return timestamp;
  }

}
