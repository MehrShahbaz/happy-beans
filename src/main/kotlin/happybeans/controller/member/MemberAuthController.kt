package happybeans.controller.member

import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.response.TokenResponse
import happybeans.dto.user.UserCreateRequestDto
import happybeans.service.MemberAuthService
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member/auth")
class MemberAuthController(
    private val memberAuthService: MemberAuthService,
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody @Valid userCreateRequestDto: UserCreateRequestDto,
    ): ResponseEntity<TokenResponse> {
        logger.info("POST Sign up for user ${userCreateRequestDto.email}")
        val userCreateResponse = memberAuthService.signUp(userCreateRequestDto)
        return ResponseEntity.created(userCreateResponse.uri)
            .body(TokenResponse(userCreateResponse.token))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid loginRequestDto: LoginRequestDto,
    ): ResponseEntity<TokenResponse> {
        logger.info("POST Login for user ${loginRequestDto.email}")
        val token = memberAuthService.login(loginRequestDto)
        return ResponseEntity.ok().body(TokenResponse(token))
    }
}
