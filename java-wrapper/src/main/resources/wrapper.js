/*-
 * #%L
 * RosaeNLG for Java
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
// helper, private
function cleanPath(path) {
  // path must follow / convention (and not \) to be in the staticFs
  return path.replace(/\\/g, '/');
}

function getRosaenlg(language) {
  switch (language) {
    case 'fr_FR': return rosaenlg_fr_FR;
    case 'en_US': return rosaenlg_en_US;
    case 'de_DE': return rosaenlg_de_DE;
    case 'it_IT': return rosaenlg_it_IT;
    case 'es_ES': return rosaenlg_es_ES;
    default: return rosaenlg_OTHER;
  }
}

function compileFileClient(path, language, jsonStaticFs, jsonOptions, exceptionMarker) {
  try {
    const opts = JSON.parse(jsonOptions);
    opts.staticFs = JSON.parse(jsonStaticFs);
    return getRosaenlg(language).compileFileClient(cleanPath(path), opts);
  } catch (e) {
    return exceptionMarker + e.toString();
  }
}

function compileFile(path, language, jsonStaticFs, jsonOptions, exceptionMarker) { 
  try {
    const opts = JSON.parse(jsonOptions);
    opts.staticFs = JSON.parse(jsonStaticFs);
    return getRosaenlg(language).compileFile(cleanPath(path), opts);
  } catch (e) {
    return exceptionMarker + e.toString();
  }
}
