import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    testImplementation("com.varabyte.kotter:kotter:1.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
}

tasks {
    wrapper {
        gradleVersion = "7.6"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    test {
        useJUnitPlatform {
            excludeTags("visualisationDemo", "visualisation", "slow")
        }
    }
}

tasks.withType<Jar>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register("visualisationDemo", Test::class) {
    useJUnitPlatform {
        includeTags("visualisationDemo")
        setForkEvery(1)
        maxParallelForks = 10
        minHeapSize = "128m"
        maxHeapSize = "4G"
    }
}

tasks.withType(KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}