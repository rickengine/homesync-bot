import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.9.22"
    // Plugins do Spring Boot que adicionamos:
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("plugin.spring") version "1.9.22"
}

group = "org.example" // Pode mudar pro seu pacote depois se quiser
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // 🚀 O Motor do Spring Boot (sem servidor web)
    implementation("org.springframework.boot:spring-boot-starter")

    // Suas integrações (mantidas intactas)
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.21.0")
    implementation("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation(kotlin("stdlib-jdk8"))

    // Testes
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
