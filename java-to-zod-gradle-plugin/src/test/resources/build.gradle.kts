import cz.habarta.typescript.generator.*

plugins {
    id("java")
    id("cz.habarta.typescript-generator") version "3.2.1263"
    id("java-to-zod-gradle-plugin") version "0.7.0-SNAPSHOT"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("cz.habarta.typescript-generator:typescript-generator-core:3.2.1263")
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<sh.ivan.zod.GenerateZodSchemas> {
    outputFile = file("build/java-to-zod/generated-schemas.ts")
    jsonLibrary = JsonLibrary.jackson2
    classes = mutableListOf("sh.ivan.zod.resources.TestPersonClass", "sh.ivan.zod.resources.TestPersonRecord")
}

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations["annotationProcessor"]
}
