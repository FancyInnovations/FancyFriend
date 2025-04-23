plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta11"
}

group = "com.fancyinnovations.fancyfriend"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.fancyinnovations.com/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.12")
    implementation("com.google.code.gson:gson:2.13.0")

    implementation("de.oliver.FancyAnalytics:java-sdk:0.0.2")
    implementation("de.oliver.FancyAnalytics:logger:0.0.6")

    implementation("org.jetbrains:annotations:26.0.2")
}

tasks {
    shadowJar {
        archiveClassifier.set("")

        manifest {
            manifest {
                attributes["Main-Class"] = "com.fancyinnovations.fancyfriend.FancyFriend"
            }
        }
    }
}