package happybeans.controller.health

import happybeans.controller.member.AbstractRestDocsRestAssuredTest
import io.restassured.RestAssured
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document

@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthCheckControllerTest : AbstractRestDocsRestAssuredTest(){
    @Test
    fun `health returns OK`() {
        val response = RestAssured
            .given(spec).port(port).log().all()
            .filter(document("health-check"))
            .`when`().get("/api/health")
            .then().log().all()
            .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.body().asString()).isEqualTo("OK")
    }
}
