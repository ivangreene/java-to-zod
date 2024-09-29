import cz.habarta.typescript.generator.*

plugins {
    id("java")
    id("cz.habarta.typescript-generator") version "3.2.1263"
    id("java-to-zod-gradle-plugin") version "0.7.0-SNAPSHOT"
    id("com.github.node-gradle.node") version "3.0.1"
}

node {
    version = "16.13.0"  // Or the version you want to use
    download = true  // This ensures a local copy of Node.js is used
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
    val tempOutputDir = layout.buildDirectory.dir("temp/generated-schemas")
    outputFile = tempOutputDir.map { it.file("schemas.ts") }.get().asFile
    pluginParameters.jsonLibrary = JsonLibrary.jackson2
    pluginParameters.classes = mutableListOf("sh.ivan.zod.resources.TestPersonClass", "sh.ivan.zod.resources.TestPersonRecord")
}

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations["annotationProcessor"]
}

tasks.register("runJestTests", Exec::class.java) {
    group = "verification"
    description = "Runs Jest tests to validate the generated Zod schemas."

    // Set working directory for Jest (e.g., the directory where the schema was generated)
    workingDir = projectDir

    // Define the command to run Jest
    commandLine = listOf("npm", "run", "test")

    // Ensure that Jest tests are run after the schemas are generated
    dependsOn("generateZodSchemas")

    // Finalizer to clean the temp files after Jest has run
    finalizedBy("cleanTemp")
}

tasks.register("cleanTemp", Delete::class.java) {
    delete(layout.buildDirectory.dir("temp"))
}

