package org.rosaenlg.lib;

/*-
 * #%L
 * java-wrapper
 * %%
 * Copyright (C) 2019 - 2020 RosaeNLG.org, Ludan Stoecklé
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


/**
 * Exception thrown when auto test fails.
 * 
 * @author Ludan Stoecklé contact@rosaenlg.org
 */
public class AutotestException extends Exception { 
  /** Constructor when rendering failed.
   * 
   * @param templateId the ID of the template
   * @param err the original error
   */
  public AutotestException(String templateId, Throwable err) {
    super(templateId + " autotest failed on rendering input", err);
  }

  /** Constructor when rendered is different than excepted.
   * 
   * @param templateId the ID of the template
   * @param expected what was expected
   * @param rendered what was rendered
   */
  public AutotestException(String templateId, String expected, String rendered) {
    super(templateId + " autotest fail on " + expected + " was " + rendered);
  }
}
