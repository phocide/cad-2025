/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.12/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Add Spring Context dependency
    implementation(libs.spring.context)
    
    // Add Jakarta Annotations API for @PostConstruct
    implementation(libs.jakarta.annotation.api)
    
    // Spring Data JPA and ORM
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation("org.springframework:spring-orm:6.1.5")

    // Hibernate (JPA provider)
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

    // H2 Database
    runtimeOnly("com.h2database:h2:2.2.224")

    // HikariCP for DataSource
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Jakarta Persistence API
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Логирование
    implementation("org.slf4j:slf4j-api:2.0.13")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.6")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    // Define the main class for the application.
    mainClass = "ru.bsuedu.cad.lab.App"
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<JavaExec>().configureEach {
    standardInput = System.`in`
    standardOutput = System.out
    jvmArgs = listOf(
        "-Dfile.encoding=UTF-8", 
        "-Dconsole.encoding=UTF-8",
        "-Dsun.stdout.encoding=UTF-8",
        "-Dsun.stderr.encoding=UTF-8",
        "-Dspring.output.ansi.enabled=ALWAYS"
    )
    systemProperty("file.encoding", "UTF-8")
    systemProperty("console.encoding", "UTF-8")
}
