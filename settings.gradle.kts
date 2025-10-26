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
include(":app")
include(":core:model")
include(":data:cards:repository")
include(":data:cards:datasource")
include(":domain:game:usecase")
include(":feature:game")
include(":data:game:datasource")
include(":data:game:repository")
include(":domain:cards:usecase")
include(":feature:cards")
