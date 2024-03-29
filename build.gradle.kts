import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("com.github.shiguruikai:combinatoricskt:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")

    testImplementation(kotlin("test"))
    testImplementation("com.varabyte.kotter:kotter:1.0.0")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "21"
        }
    }
    test {
        useJUnitPlatform {
            excludeTags("visualisationDemo", "visualisation", "slow")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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