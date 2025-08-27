package happybeans.controller.restaurantOwner

import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.response.TokenResponse
import happybeans.service.RestaurantOwnerAuthService
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/restaurant-owner/")
class RestaurantOwnerAuthController(
    private val restaurantOwnerAuthService: RestaurantOwnerAuthService,
) {
    private val logger = KotlinLogging.logger {}
    
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody loginRequestDto: LoginRequestDto,
    ): ResponseEntity<TokenResponse> {
        logger.info("POST Restaurant Owner login for ${loginRequestDto.email}")
        val token = restaurantOwnerAuthService.login(loginRequestDto)
        return ResponseEntity.ok().body(TokenResponse(token))
    }
}
