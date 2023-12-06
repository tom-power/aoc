#!/bin/bash
source $PWD/sh/.env
day=$1
mainTemplate="src/main/kotlin/template"
mainYear="src/main/kotlin/aoc$year"
testTemplate="src/test/kotlin/template"
testYear="src/test/kotlin/aoc$year"
testResourcesYear="src/test/resources/aoc$year"

sedOption="-i "
if [[ "$OSTYPE" == "darwin"* ]]; then
  sedOption="-i '' -e"
fi

mkdir -p "$mainYear" &&
cp "$mainTemplate/Day00.kt" "$mainYear/Day$1.kt" &&
sed $sedOption "s/Year00/Year$year/g" "$mainYear/Day$1.kt" &&
sed $sedOption "s/00/$1/g" "$mainYear/Day$1.kt" &&
sed $sedOption "s/template/aoc$year/" "$mainYear/Day$1.kt" &&

mkdir -p "$testYear" &&
cp "$testTemplate/Day00Test.kt" "$testYear/Day$1Test.kt" &&
sed $sedOption "s/Year00/Year$year/g" "$testYear/Day$1Test.kt" &&
sed $sedOption "s/00/$1/g" "$testYear/Day$1Test.kt" &&
sed $sedOption "s/template/aoc$year/" "$testYear/Day$1Test.kt" &&

mkdir -p "$testResourcesYear"  &&
touch "$testResourcesYear/Day$1.txt"  &&
touch "$testResourcesYear/Day$1_example.txt"