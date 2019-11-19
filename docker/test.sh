#!/bin/sh

# encoding issues
EXPECTED="<p>Il chantera"

# put host here
HOST=SOMETHING

curl -X PUT \
  http://$HOST/templates \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -d '{
  "templateId": "chanson",
  "entryTemplate": "chanson.pug",
  "compileInfo": {
    "activate": false,
    "compileDebug": false,
    "language": "fr_FR"
  },
  "templates": {
    "chanson.pug": "p\n  | il #[+verb(getAnonMS(), {verb: '\''chanter'\'', tense:'\''FUTUR'\''} )]\n  | \"#{chanson.nom}\"\n  | de #{chanson.auteur}\n"
  }
}
'

RES="$(
curl -X POST \
  http://$HOST/templates/chanson/render \
  -H "Accept-Charset: utf-8;q=0.7,*;q=0.7" \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -d '{
  "language": "fr_FR",
  "chanson": {
    "auteur": "Édith Piaf",
    "nom": "Non, je ne regrette rien"
  }
}'
)"

echo "res: $RES"
echo "expected: $EXPECTED"

if [[ $RES == *"$EXPECTED"* ]]; then
  echo "TEST: OK!"
  exit 0
else
  echo "TEST: FAILS (too bad)! :-("
  exit 1
fi
