plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()

}
dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("com.github.shiguruikai:combinatoricskt:1.6.0")

    testImplementation(kotlin("test"))
}

tasks {
    wrapper {
        gradleVersion = "7.6"
    }
    test {
       useJUnitPlatform()
    }
}

tasks.withType<Jar>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}