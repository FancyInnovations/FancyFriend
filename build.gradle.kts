plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta11"
}

group = "com.fancyinnovations.fancyfriend"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.fancyinnovations.com/releases")
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:5.5.1")
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("org.json:json:20250107")

    implementation("de.oliver.FancyAnalytics:java-sdk:0.0.2")
    implementation("de.oliver.FancyAnalytics:logger:0.0.6")

    implementation("org.jetbrains:annotations:26.0.2")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("FancyFriend.jar")

        manifest {
            manifest {
                attributes["Main-Class"] = "com.fancyinnovations.fancyfriend.FancyFriend"
            }
        }
    }
}