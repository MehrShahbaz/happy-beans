package happybeans.controller.admin

import happybeans.dto.response.MessageResponse
import happybeans.model.JoinRequest
import happybeans.service.JoinRequestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/join-request")
class AdminJoinRequestController(
    private val joinRequestService: JoinRequestService,
) {
    @GetMapping()
    fun getAllRequests(): ResponseEntity<List<JoinRequest>> {
        return ResponseEntity.ok().body(joinRequestService.getAllJoinRequests())
    }

    @PostMapping("/accept/{joinRequestId}")
    fun acceptInvite(
        @PathVariable joinRequestId: Long,
    ): ResponseEntity<MessageResponse> {
        joinRequestService.handleAcceptInvite(joinRequestId)
        return ResponseEntity.ok(MessageResponse("Owner Accepted"))
    }

    @PostMapping("/reject/{joinRequestId}")
    fun rejectInvite(
        @PathVariable joinRequestId: Long,
    ): ResponseEntity<MessageResponse> {
        joinRequestService.handleRejectInvite(joinRequestId)
        return ResponseEntity.ok(MessageResponse("Owner Rejected"))
    }
}
