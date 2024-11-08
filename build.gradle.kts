plugins {
    application
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "uk.ac.nott.cs.comp2013"
version = "1.0.0"

repositories {
    mavenCentral()
}

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.vintage:junit-vintage-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.pholser:junit-quickcheck-core:1.0")
    testImplementation("com.pholser:junit-quickcheck-generators:1.0")
    testImplementation("org.mockito:mockito-core:5.14.+")
    mockitoAgent("org.mockito:mockito-core:5.14.+") { isTransitive = false }

    implementation("com.opencsv:opencsv:5.8")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

application {
    mainClass = "uk.ac.nott.cs.comp2013.mentorapp.MentorApp"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}