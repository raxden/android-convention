package extension

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementationBundle(dependencyNotation: Provider<ExternalModuleDependencyBundle>) {
    dependencyNotation.get().forEach { dependency ->
        if (dependency.name.contains("bom")) {
            add("implementation", platform(dependency))
        } else {
            add("implementation", dependency)
        }
    }
}
