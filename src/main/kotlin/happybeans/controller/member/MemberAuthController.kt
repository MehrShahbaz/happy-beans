package happybeans.controller.member

import happybeans.dto.response.TokenResponse
import happybeans.dto.user.UserCreateRequestDto
import happybeans.service.MemberAuthService
import jakarta.validation.Valid
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
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody @Valid userCreateRequestDto: UserCreateRequestDto,
    ): ResponseEntity<TokenResponse> {
        val userCreateResponse = memberAuthService.signUp(userCreateRequestDto)
        return ResponseEntity.created(userCreateResponse.uri)
            .body(TokenResponse(userCreateResponse.token))
    }
}
