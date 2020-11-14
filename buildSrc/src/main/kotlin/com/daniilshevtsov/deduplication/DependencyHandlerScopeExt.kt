package com.daniilshevtsov.deduplication

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.appDependencies() {
    logging()

    dagger(version = "2.23.1")
}

fun DependencyHandlerScope.logging() {
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.2")
    implementation("org.slf4j:slf4j-simple:1.7.29")
}

fun DependencyHandlerScope.dagger(version: String) {
    implementation("com.google.dagger:dagger:$version")
    kapt("com.google.dagger:dagger-compiler:$version")
}

fun DependencyHandlerScope.unitTestDependencies() {

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