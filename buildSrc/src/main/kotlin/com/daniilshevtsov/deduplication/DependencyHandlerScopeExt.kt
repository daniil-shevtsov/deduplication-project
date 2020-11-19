package com.daniilshevtsov.deduplication

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.appDependencies() {
    logging()

    dagger(version = "2.23.1")

    database(
        sqliteVersion = "3.30.1",
        exposedVersion = "0.28.1"
    )

    unitTestDependencies(
        jupiterVersion = "5.4.2",
        kotestVersion = "4.3.1"
    )
}

fun DependencyHandlerScope.logging() {
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.2")
    implementation("org.slf4j:slf4j-simple:1.7.29")
}

fun DependencyHandlerScope.dagger(version: String) {
    implementation("com.google.dagger:dagger:$version")
    kapt("com.google.dagger:dagger-compiler:$version")
}

fun DependencyHandlerScope.database(
    sqliteVersion: String,
    exposedVersion: String
) {
    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
}

fun DependencyHandlerScope.unitTestDependencies(
    jupiterVersion: String,
    kotestVersion: String
) {
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
    testImplementation("io.mockk:mockk:1.9.3")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
}

//TODO: Do something about these weird hacks
private fun DependencyHandlerScope.implementation(dependency: String) {
    add("implementation", dependency)
}

private fun DependencyHandlerScope.kapt(dependency: String) {
    add("kapt", dependency)
}

private fun DependencyHandlerScope.testImplementation(dependency: String) {
    add("testImplementation", dependency)
}

private fun DependencyHandlerScope.testRuntimeOnly(dependency: String) {
    "testRuntimeOnly"(dependency)
}