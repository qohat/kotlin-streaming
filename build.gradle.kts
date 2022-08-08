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

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}