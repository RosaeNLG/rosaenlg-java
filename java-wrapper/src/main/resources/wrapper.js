/*-
 * #%L
 * RosaeNLG for Java
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
