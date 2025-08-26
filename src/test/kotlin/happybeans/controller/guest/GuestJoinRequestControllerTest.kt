package happybeans.controller.guest

import happybeans.dto.joinRequest.JoinRequestDto
import happybeans.repository.JoinRequestRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GuestJoinRequestControllerTest {
    @Autowired
    lateinit var joinRequestRepository: JoinRequestRepository

    @AfterEach
    fun cleanUp() {
        joinRequestRepository.deleteAll()
    }

    @Test
    fun joinRequest() {
        val request = createJoinRequestDto("test1@request.com")

        val response =
            RestAssured
                .given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/guest/join-request")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `joinRequest throws for same user`() {
        val request = createJoinRequestDto("test2@request.com")

        RestAssured
            .given().log().all()
            .body(request)
            .contentType(ContentType.JSON)
            .`when`().post("/api/guest/join-request")
            .then().log().all().extract()

        val response =
            RestAssured
                .given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/guest/join-request")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value())
    }

    private fun createJoinRequestDto(email: String): JoinRequestDto {
        return JoinRequestDto(
            email,
            "test",
            "test",
            "test",
        )
    }
}
