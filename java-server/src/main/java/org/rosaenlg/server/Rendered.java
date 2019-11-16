package org.rosaenlg.server;

/*-
 * #%L
 * org.rosaenlg:java-server
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
