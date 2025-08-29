### Spring REST Docs 

**Spring REST Docs** generates accurate API documentation directly from tests.
Instead of writing docs manually, you write tests, and Spring REST Docs produces snippets in AsciiDoc that later become full HTML documentation.
This ensures that your API documentation is always **up to date and consistent** with your code.

**Why use it?**
- Docs = truth from tests → no outdated API descriptions.
- Lightweight → no extra runtime dependencies.
- Flexible → you choose how docs are structured and styled.
- Clean code → no need for annotations inside controllers.
- Version control friendly → snippets and AsciiDoc live in the repo.

**Required Dependencies (Gradle Kotlin DSL)**
These dependencies allow Spring REST Docs to capture request/response information during tests and convert them into **snippets**.
```
    dependencies {
    // Core Spring REST Docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    // or for REST Assured
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    // Asciidoctor plugin support
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}
```

**Plugins (Gradle)**
The Asciidoctor plugin is used to transform your snippets and .adoc files into **HTML or PDF documentation**.
```kotlin
plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}
```

**Gradle Setup (snippets + build tasks)**
This configuration ensures:
1.	Tests produce snippets.
2.	Asciidoctor assembles snippets into final docs.
3.	Docs are automatically copied into static/docs and also packaged inside the final JAR.
```kotlin
// In build.gradle.kts

val snippetsDir by extra { "build/generated-snippets" }

tasks.test {
    // Make sure tests output snippets here
    outputs.dir(snippetsDir)
}

tasks.asciidoctor {
    // Use the snippets directory produced by tests
    inputs.dir(snippetsDir)
    configurations("asciidoctorExt")
    dependsOn(tasks.test)
    baseDirFollowsSourceFile()
}

//Copy built docs into static resources (useful for local preview and packaged app)
tasks.register<Copy>("copyDocs") {
    dependsOn(tasks.asciidoctor)
    // Copy only the final HTML (index.html) produced by Asciidoctor
    from("${tasks.asciidoctor.get().outputDir}/index.html")
    into("src/main/resources/static/docs")
}

//Package docs into the JAR under static/docs/
tasks.bootJar {
    dependsOn(tasks.asciidoctor)
    from("${tasks.asciidoctor.get().outputDir}/index.html") {
        into("static/docs")
    }
}
```

**Works with Testing Libraries**
Works with Testing Libraries
- MockMvc → for Spring MVC controllers.
- REST Assured → for full HTTP-level testing.
- WebTestClient → for reactive APIs.

```mermaid
A[Write Tests<br/>(MockMvc / RestAssured / WebTestClient)] --> B[Run Tests]
B --> C[Generate Snippets<br/>(HTTP request/response, fields, etc.)]
C --> D[Assemble AsciiDoc<br/>(index.adoc + includes)]
D --> E[Convert with Asciidoctor]
E --> F[Final Docs<br/>(HTML / PDF)]
```





