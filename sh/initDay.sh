srcMain=src/main/kotlin/aoc22
srcTest=src/test/kotlin/aoc22
srcResources=src/main/resources

cp "$srcMain/Day00.kt" "$srcMain/Day$1.kt" &&
sed -i "s/00/$1/" "$srcMain/Day$1.kt" &&

cp "$srcTest/Day00Test.kt" "$srcTest/Day$1Test.kt" &&
sed -i "s/00/$1/" "$srcTest/Day$1Test.kt" &&

touch "$srcResources/Day$1.txt"  &&
touch "$srcResources/Day$1_example.txt"