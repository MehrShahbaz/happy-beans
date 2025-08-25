package happybeans.controller

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation

@ExtendWith(RestDocumentationExtension::class)
abstract class AbstractRestDocsRestAssuredTest {
    @LocalServerPort
    protected var port: Int = 0

    protected lateinit var spec: RequestSpecification

    @BeforeEach
    fun setUpBase(restDocumentation: RestDocumentationContextProvider) {
        spec =
            RequestSpecBuilder()
                .addFilter(
                    RestAssuredRestDocumentation.documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(Preprocessors.prettyPrint())
                        .withResponseDefaults(Preprocessors.prettyPrint()),
                )
                .build()
    }

    protected fun given(): RequestSpecification = RestAssured.given(spec).port(port)
}
