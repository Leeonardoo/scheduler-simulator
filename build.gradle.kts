import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "io.github.leeonardoo"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation("com.arkivanov.decompose:decompose:2.0.0-beta-01")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.0.0-beta-01")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
            }
        }
    }
}

allprojects {
    tasks.withType(KotlinCompile::class).configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs.toMutableList().also {
                it.addAll(
                    listOf(
                        "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                        "-Xopt-in=com.arkivanov.decompose.ExperimentalDecomposeApi",
                        "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",

                    )
                )
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.leeonardoo.so.scheduler.ui.AppKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "scheduler"
            packageVersion = "1.0.0"

            jvmArgs(
                "-Dapple.awt.application.appearance=system"
            )
        }
    }
}
