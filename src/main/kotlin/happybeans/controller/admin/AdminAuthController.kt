package happybeans.controller.admin

import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.response.TokenResponse
import happybeans.service.AdminAuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/auth")
class AdminAuthController(
    private val adminAuthService: AdminAuthService,
) {
    @PostMapping("/login")
    fun signIn(
        @RequestBody @Valid loginRequest: LoginRequestDto,
    ): ResponseEntity<TokenResponse> {
        val token = adminAuthService.login(loginRequest)
        return ResponseEntity.ok().body(TokenResponse(token))
    }
}
