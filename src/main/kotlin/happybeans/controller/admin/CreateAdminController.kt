package happybeans.controller.admin

import happybeans.dto.response.MessageResponse
import happybeans.dto.user.UserCreateRequestDto
import happybeans.service.AdminUserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/admin/create-admin")
class CreateAdminController(
    private val adminUserService: AdminUserService,
) {
    @PostMapping
    fun createAdmin(
        @Valid @RequestBody userCreateRequestDto: UserCreateRequestDto,
    ): ResponseEntity<MessageResponse> {
        val user = adminUserService.createAdmin(userCreateRequestDto)
        val location =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("create-admin")
                .buildAndExpand(user.id)
                .toUri()

        return ResponseEntity.created(location).body(MessageResponse("Admin created!"))
    }
}
