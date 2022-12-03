cp src/main/kotlin/Day00.kt "src/main/kotlin/Day$1.kt" &&
touch "src/main/resources/Day$1.txt"  &&
touch "src/main/resources/Day$1_test.txt" &&
cp src/test/kotlin/Day00Test.kt "src/test/kotlin/Day$1Test.kt" &&

sed -i "s/00/$1/" "src/main/kotlin/Day$1.kt" &&
sed -i "s/00/$1/" "src/test/kotlin/Day$1Test.kt"