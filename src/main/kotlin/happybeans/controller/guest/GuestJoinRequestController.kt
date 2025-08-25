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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/guest/join-request")
class GuestJoinRequestController(
    private val joinRequestService: JoinRequestService,
) {
    @PostMapping
    fun joinRequest(
        @RequestBody @Valid joinRequestDto: JoinRequestDto,
    ): ResponseEntity<MessageResponse> {
        val request = joinRequestService.createJoinRequest(joinRequestDto)

        val location: URI =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/join-request")
                .buildAndExpand(request.id)
                .toUri()

        return ResponseEntity.ok(MessageResponse("Request sent successfully"))
    }
}
