import org.gradle.api.JavaVersion
import org.gradle.jvm.toolchain.JavaLanguageVersion

object AppConfig {
    const val minSdk = 21
    const val compileSdk = 31
    const val targetSdk = 31

    private const val version = 11
    val javaLanguageVersion = JavaLanguageVersion.of(version)
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}
