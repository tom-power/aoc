#!/bin/bash
source $PWD/sh/.env
day=$1
mainTemplate="src/main/kotlin/template"
mainYear="src/main/kotlin/aoc$year"
testTemplate="src/test/kotlin/template"
testYear="src/test/kotlin/aoc$year"
testResourcesYear="src/test/resources/aoc$year"

mkdir -p "$mainYear" &&
cp "$mainTemplate/Day00.kt" "$mainYear/Day$day.kt" &&
sed -i "s/Year00/Year$year/g" "$mainYear/Day$day.kt" &&
sed -i "s/00/$day/g" "$mainYear/Day$day.kt" &&
sed -i "s/template/aoc$year/" "$mainYear/Day$day.kt" &&

mkdir -p "$testYear" &&
cp "$testTemplate/Day00Test.kt" "$testYear/Day${day}Test.kt" &&
sed -i "s/Year00/Year$year/g" "$testYear/Day${day}Test.kt" &&
sed -i "s/00/$day/g" "$testYear/Day${day}Test.kt" &&
sed -i "s/template/aoc$year/" "$testYear/Day${day}Test.kt" &&

mkdir -p "$testResourcesYear"  &&
touch "$testResourcesYear/Day$day.txt" &&
touch "$testResourcesYear/Day${day}_example.txt" &&

mob start  --include-uncommitted-changes --branch "day$day"