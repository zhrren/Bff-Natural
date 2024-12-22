import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "app"
version = "1.0.0"

val vertxVersion = "4.5.11"
val watchForChange = "src/**/*"
val doOnChange = "$projectDir/gradlew classes"

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "17"

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions.jvmTarget = "17"

plugins {
  kotlin("jvm") version "1.9.25"
  application
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("io.ebean") version "12.8.3"
  id("org.jetbrains.kotlin.kapt") version "1.9.25"
  id("org.jlleitschuh.gradle.ktlint-idea") version "11.3.1"
  id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

application {
  mainClass.set("app.Application")
  applicationDefaultJvmArgs = "--add-opens java.base/java.lang=ALL-UNNAMED".split(" ")
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

kotlin {
  jvmToolchain(17)
}

ktlint {
  disabledRules.set(setOf("no-wildcard-imports"))
}

ebean {
  debugLevel = 1
}

repositories {
  maven {
    url = uri("https://maven.aliyun.com/repository/public/")
  }
  maven {
    credentials {
      username = "62745b297e8dbc28d840b2cb"
      password = "n_PLOsGjq-a="
    }
    url = uri("https://packages.aliyun.com/maven/repository/2224919-release-0iLhgF/")
  }
  maven {
    credentials {
      username = "62745b297e8dbc28d840b2cb"
      password = "n_PLOsGjq-a="
    }
    url = uri("https://packages.aliyun.com/maven/repository/2224919-snapshot-ZZUjrQ/")
  }
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.25")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-lang-kotlin:$vertxVersion")
  implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")
  implementation("io.vertx:vertx-core:$vertxVersion")
  implementation("io.vertx:vertx-web:$vertxVersion")
  implementation("io.vertx:vertx-web-client:$vertxVersion")
  implementation("io.vertx:vertx-pg-client:$vertxVersion")
  implementation("io.vertx:vertx-sql-client-templates:$vertxVersion")
  implementation("io.vertx:vertx-redis-client:$vertxVersion")
  implementation("io.vertx:vertx-mqtt:$vertxVersion")

  implementation("org.codehaus.janino:janino:3.1.6")
  implementation("com.ongres.scram:client:2.1")
  implementation("org.postgresql:postgresql:42.7.2")
  implementation("io.ebean:ebean:13.10.1")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")
  implementation("org.reflections:reflections:0.9.12")

  implementation("io.github.microutils:kotlin-logging:3.0.2")
  implementation("org.codehaus.janino:janino:3.1.8")
  implementation("org.slf4j:slf4j-api:2.0.3")
  implementation("ch.qos.logback:logback-classic:1.4.12")

  implementation("com.google.inject:guice:5.1.0")
  implementation("app:vertx-cutil:1.0.24012101")

  implementation("cn.hutool:hutool-core:5.8.25")
  implementation("cn.hutool:hutool-crypto:5.8.25")
  implementation("cn.hutool:hutool-extra:5.8.25")
  implementation("cn.hutool:hutool-all:5.8.25")
  implementation("cglib:cglib:3.3.0")

  testImplementation("io.vertx:vertx-junit5:$vertxVersion")
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
  testImplementation("io.reactiverse:reactiverse-junit5-web-client:0.3.0")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
  testImplementation("io.ebean:ebean-test:13.10.1")

  implementation("com.taosdata.jdbc:taos-jdbcdriver:3.2.7")
  implementation("com.zaxxer:HikariCP:5.1.0")

  implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
}
