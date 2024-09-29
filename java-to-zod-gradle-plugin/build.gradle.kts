plugins {
    id("java")
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "sh.ivan"
version = "0.7.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")
    implementation("sh.ivan:java-to-zod-core:0.7.0-SNAPSHOT")


    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
// https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:33.3.1-jre")


    implementation("cz.habarta.typescript-generator:typescript-generator-core:3.2.1263")
    implementation(gradleApi())

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("java-to-zod-gradle-plugin") {
            id = "java-to-zod-gradle-plugin"
            implementationClass = "sh.ivan.zod.JavaToZodPlugin"
        }
    }
}

publishing {

    repositories {
        mavenLocal()  // Publish to the local Maven repository
    }
}