#!/usr/bin/env bash
# Compile everything and run every example as a test.
set -e
cd "$(dirname "$0")"
rm -rf out && mkdir -p out
javac -encoding UTF-8 -d out $(find src -name '*.java')
status=0
for t in $(cd src/test/java && find . -name '*Tests.java' | sort | sed 's|^\./||; s|\.java$||; s|/|.|g'); do
  java -cp out "$t" || status=1
done
exit $status
