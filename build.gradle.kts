import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") plugins {
    application
    alias(libs.plugins.kotest.multiplatform)
    id(libs.plugins.kotlin.jvm.pluginId)
    alias(libs.plugins.dokka)
    id(libs.plugins.detekt.pluginId)
    alias(libs.plugins.kover)
    alias(libs.plugins.kotlinx.serialization)
}

application {
    mainClass by "io.github.qohat.MainKt"
}

allprojects {
    extra.set("dokka.outputDirectory", rootDir.resolve("docs"))
    setupDetekt()
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "${JavaVersion.VERSION_11}"
            freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
        }
    }

    test {
        useJUnitPlatform()
        extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
            includes = listOf("io.github.qohat.*")
        }
    }
}

dependencies {
    implementation(libs.bundles.arrow)
    implementation(libs.bundles.ktor.server)
    implementation(libs.ktor.serialization)
    implementation(libs.logback.classic)
    implementation(libs.hikari)
    implementation(libs.postgresql)
    implementation(libs.kotlin.kafka)

    testImplementation(libs.bundles.ktor.client)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.kafka)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.bundles.kotest)
}