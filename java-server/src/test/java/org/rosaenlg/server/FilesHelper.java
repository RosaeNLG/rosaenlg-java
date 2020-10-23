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

import java.io.File;
import org.apache.commons.io.FileUtils;

public class FilesHelper {
  private String repoPath;
  private String testPath;

  protected FilesHelper(String repoPath, String testPath) {
    this.repoPath = repoPath;
    this.testPath = testPath;
  }

  protected void copyToTest(String filename) throws Exception {
    FileUtils.copyFile(
        new File(repoPath + File.separator + filename), 
        new File(testPath + File.separator + filename)
    );
  }

  protected void deleteTest(String filename) throws Exception {
    new File(testPath + File.separator + filename).delete();
  }

  protected void cleanTest() throws Exception {
    FileUtils.cleanDirectory(new File(testPath));
  }

}
