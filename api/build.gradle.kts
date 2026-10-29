plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.apollo)
}

android {
    namespace = "nl.guidoroos.ticketwave.api"
    compileSdk = 37

    defaultConfig {
        minSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        getByName("main").java.srcDir("src/main/generated")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.apollo.runtime)
    implementation(libs.apollo.normalized.cache)
    implementation(libs.datastore.preferences)
    implementation(libs.okhttp)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

apollo {
    service("service") {
        packageName.set("nl.guidoroos.ticketwave.api.graphql")
        schemaFile.set(file("src/main/graphql/schema.graphql"))
    }
}

tasks.register<Copy>("copyApolloGeneratedSources") {
    dependsOn("generateServiceApolloSources")
    from(layout.buildDirectory.dir("generated/apollosources/service"))
    into(layout.projectDirectory.dir("src/main/generated"))
}

tasks.named("preBuild") {
    dependsOn("copyApolloGeneratedSources")
}
