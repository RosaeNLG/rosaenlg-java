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

// used when debugging only
function JSONStringify(object) {
  var cache = [];        
  var str = JSON.stringify(object,
      // custom replacer fxn - gets around "TypeError: Converting circular structure to JSON" 
      function(key, value) {
          if (typeof value === 'object' && value !== null) {
              if (cache.indexOf(value) !== -1) {
                  // Circular reference found, discard key
                  return;
              }
              // Store value in our collection
              cache.push(value);
          }
          return value;
      }, 4);
  cache = null; // enable garbage collection
  return str;
};

function compileFile(path, language, jsonStaticFs, jsonOptions, exceptionMarker) { 
  try {
    const opts = JSON.parse(jsonOptions);
    opts.staticFs = JSON.parse(jsonStaticFs);
    const fct = getRosaenlg(language).compileFile(cleanPath(path), opts);
    return (renderOptsWithLib) => {
      renderOptsWithLib.outputData = {}; // must be created here
      const renderedText = fct(renderOptsWithLib);
      const outputData = renderOptsWithLib.outputData;

      return JSON.stringify({
        renderedText: renderedText,
        outputData: outputData,
        renderOptions: {
          randomSeed: renderOptsWithLib.util.randomSeed
          // we don't have a simple access to input render options; will be completed upflow
        }
      });
    };
  } catch (e) {
    return exceptionMarker + e.toString();
  }
}
