package happybeans.controller

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseApiTest {
    @BeforeAll
    fun setup() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = 80
    }
}
