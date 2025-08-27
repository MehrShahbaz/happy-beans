package happybeans.controller.admin

import happybeans.dto.response.MessageResponse
import happybeans.model.JoinRequest
import happybeans.service.JoinRequestService
import mu.KotlinLogging
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
    private val logger = KotlinLogging.logger {}

    @GetMapping()
    fun getAllRequests(): ResponseEntity<List<JoinRequest>> {
        logger.info("GET Getting all JoinRequests for Admin")
        return ResponseEntity.ok().body(joinRequestService.getAllJoinRequests())
    }

    @PostMapping("/accept/{joinRequestId}")
    fun acceptInvite(
        @PathVariable joinRequestId: Long,
    ): ResponseEntity<MessageResponse> {
        logger.info("POST Accepting JoinRequest for $joinRequestId")
        joinRequestService.handleAcceptInvite(joinRequestId)
        return ResponseEntity.ok(MessageResponse("Owner Accepted"))
    }

    @PostMapping("/reject/{joinRequestId}")
    fun rejectInvite(
        @PathVariable joinRequestId: Long,
    ): ResponseEntity<MessageResponse> {
        logger.info("POST Rejecting JoinRequest for $joinRequestId")
        joinRequestService.handleRejectInvite(joinRequestId)
        return ResponseEntity.ok(MessageResponse("Owner Rejected"))
    }
}
