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
import com.fasterxml.jackson.annotation.JsonRawValue;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Template rendering response.
 *
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rendered {

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(Rendered.class);

  private String renderedText;
  
  @JsonRawValue
  private String outputData;

  private long ms;

  private RenderOptionsForSerialize renderOptions;

  
  /** Constructor.
   * @param renderedText the rendered text
   * @param outputData the output data
   * @param renderOptions the rendering options
   * @param ms time to render in ms
   */
  public Rendered(
      String renderedText,
      String outputData,
      RenderOptionsForSerialize renderOptions,
      long ms) {
    this.renderedText = renderedText;
    this.outputData = outputData;
    this.renderOptions = renderOptions;
    this.ms = ms;
  }

  
  /** Getter on rendering options.
   * @return RenderOptionsForSerialize the rendering options
   */
  public RenderOptionsForSerialize getRenderOptions() {
    return renderOptions;
  }
  
  /** Getter on rendered text.
   * @return String rendered text
   */
  public String getRenderedText() {
    return renderedText;
  }

  /** Getter on ms.
   * @return long ms
   */
  public long getMs() {
    return ms;
  }

}
