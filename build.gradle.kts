plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.allopen") version "1.6.21"
    id("io.quarkus")
    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.21"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-hibernate-reactive-panache")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-reactive-pg-client")
    implementation("org.jetbrains.kotlin:kotlin-noarg")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

noArg {
    annotation("de.novatec.configuration.NoArgConstructor")
}

group = "de.novatec"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}
