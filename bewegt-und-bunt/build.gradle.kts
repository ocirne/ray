plugins {
    kotlin("jvm") version "1.5.10"
}

group = "io.github.ocirne.ray"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.squareup:gifencoder:0.10.1")
}
