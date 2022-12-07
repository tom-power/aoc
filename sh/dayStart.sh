#!/bin/bash

source $PWD/sh/paths.sh

cp "$srcMainAoc/Day00.kt" "$srcMain/Day$1.kt" &&
sed -i "s/00/$1/g" "$srcMain/Day$1.kt" &&

cp "$srcTestAoc/Day00Test.kt" "$srcTest/Day$1Test.kt" &&
sed -i "s/00/$1/g" "$srcTest/Day$1Test.kt" &&

touch "$srcResources/Day$1.txt"  &&
touch "$srcResources/Day$1_example.txt"