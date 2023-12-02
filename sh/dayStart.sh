#!/bin/bash
source $PWD/sh/.env

mainTemplate="src/main/kotlin/template"
mainYear="src/main/kotlin/aoc$year"
mainResourcesYear="src/main/resources/aoc$year"
testTemplate="src/test/kotlin/template"
testYear="src/test/kotlin/aoc$year"

mkdir "$mainYear" &&
cp "$mainTemplate/Day00.kt" "$mainYear/Day$1.kt" &&
sed -i "s/00/$1/g" "$mainYear/Day$1.kt" &&
sed -i "s/template/aoc$year/" "$mainYear/Day$1.kt"
sed -i "s/Year/$year/" "$mainYear/Day$1.kt"

mkdir "$testYear" &&
cp "$testTemplate/Day00Test.kt" "$testYear/Day$1Test.kt" &&
sed -i "s/00/$1/g" "$testYear/Day$1Test.kt" &&
sed -i "s/template/aoc$year/" "$testYear/Day$1Test.kt"

mkdir "$mainResourcesYear"  &&
touch "$mainResourcesYear/Day$1.txt"  &&
touch "$mainResourcesYear/Day$1_example.txt"