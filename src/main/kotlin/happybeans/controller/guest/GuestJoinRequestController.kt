package happybeans.controller.guest

import happybeans.dto.joinRequest.JoinRequestDto
import happybeans.dto.response.MessageResponse
import happybeans.service.JoinRequestService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/guest/join-request")
class GuestJoinRequestController(
    private val joinRequestService: JoinRequestService,
) {
    @PostMapping
    fun joinRequest(
        @RequestBody @Valid joinRequestDto: JoinRequestDto,
    ): ResponseEntity<MessageResponse> {
        joinRequestService.createJoinRequest(joinRequestDto)
        return ResponseEntity.ok(MessageResponse("Request sent successfully"))
    }
}
