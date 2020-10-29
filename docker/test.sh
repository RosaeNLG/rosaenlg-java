#!/bin/sh

echo "starting test..."

# https://stackoverflow.com/questions/2829613/how-do-you-tell-if-a-string-contains-another-string-in-posix-sh
# contains(string, substring)
#
# Returns 0 if the specified string contains the specified substring,
# otherwise returns 1.
contains() {
    string="$1"
    substring="$2"
    if test "${string#*$substring}" != "$string"
    then
        return 0    # $substring is in $string
    else
        return 1    # $substring is not in $string
    fi
}

echo "create a template"

curl -X PUT \
  http://localhost:8080/templates \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -o creation.json \
  -d '{
  "templateId": "chanson",
  "entryTemplate": "chanson.pug",
  "compileInfo": {
    "compileDebug": false,
    "language": "fr_FR"
  },   
  "templates": {
    "chanson.pug": "p\n  | il #[+verb(getAnonMS(), {verb: '\''chanter'\'', tense:'\''FUTUR'\''} )]\n  | \"#{chanson.nom}\"\n  | de #{chanson.auteur}\n"
  }
}
'

echo "end of curl call #1"

CAT_CREATION="$(cat creation.json)"
echo "TEST CREATION ON: $CAT_CREATION"
contains "$CAT_CREATION" "templateId" || exit 1
echo "TEST CREATION: OK!"

echo "render the template"

curl -X POST \
  http://localhost:8080/templates/chanson/render \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -o render.json \
  -d '{
  "language": "fr_FR",
  "chanson": {
    "auteur": "Édith Piaf",
    "nom": "Non, je ne regrette rien"
  }
}'

echo "end of curl call #2"

CAT_RENDER="$(cat render.json)"
echo "TEST RENDER ON: $CAT_RENDER"
contains "$CAT_RENDER" "Il chantera" || exit 1
echo "TEST RENDER: OK!"

exit 0
