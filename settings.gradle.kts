rootProject.name = "Bff-Natural"

pluginManagement {
  repositories {
    maven {
      url = uri("https://maven.aliyun.com/repository/gradle-plugin")
    }
    maven {
      url = uri("https://plugins.gradle.org/m2")
    }
    mavenLocal()
    mavenCentral()
  }
}
