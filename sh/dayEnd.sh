#!/bin/bash

source $PWD/sh/paths.sh

mv "$srcMain/Day$1.kt" "$srcMainAoc/" &&
mv "$srcTest/Day$1Test.kt" "$srcTestAoc/" &&
mv "$srcResources/Day$1.txt" "$srcResourcesAoc/" &&
mv "$srcResources/Day$1_example.txt" "$srcResourcesAoc/"