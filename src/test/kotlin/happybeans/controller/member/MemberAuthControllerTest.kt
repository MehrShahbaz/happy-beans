package happybeans.controller.member

import happybeans.controller.BaseApiTest
import happybeans.dto.user.UserCreateRequestDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberAuthControllerTest : BaseApiTest() {
    @Test
    fun `sign-up User`() {
        val user =
            UserCreateRequestDto(
                "temp@temp.com",
                "test-456",
                "First",
                "Last"
            )
        val response =
            RestAssured
                .given().log().all()
                .body(user)
                .contentType(ContentType.JSON)
                .`when`().post("api/member/auth/sign-up")
                .then().log().all().extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(response.body().jsonPath().get<String>("token")).isNotEmpty
    }
}
