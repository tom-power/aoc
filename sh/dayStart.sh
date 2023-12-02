#!/bin/bash
source $PWD/sh/.env

common="src/main/kotlin/common"
main="src/main/kotlin/aoc$year"
resources="src/main/resources/aoc$year"
test="src/test/kotlin/aoc$year"

cp "$common/Day00.kt" "$main/Day$1.kt" &&
sed -i "s/00/$1/g" "$main/Day$1.kt" &&

cp "$test/Day00Test.kt" "$test/Day$1Test.kt" &&
sed -i "s/00/$1/g" "$test/Day$1Test.kt" &&

touch "$resources/Day$1.txt"  &&
touch "$resources/Day$1_example.txt"