pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Bingo"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:model")
include(":core:network")
include(":core:database")
include(":data:cards:repository")
include(":data:cards:datasource")
include(":data:game:repository")
include(":data:game:datasource")
include(":domain:cards:usecase")
include(":domain:game:usecase")
include(":feature:cards")
include(":feature:game")
include(":ui")
