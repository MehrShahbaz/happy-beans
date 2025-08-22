package happybeans.service

import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.enums.JoinRequestStatus
import happybeans.model.JoinRequest
import happybeans.repository.JoinRequestRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JoinRequestService(
    private val joinRequestRepository: JoinRequestRepository,
    private val emailDispatchService: EmailDispatchService,
    private val handleRestaurantOwnerCreateService: HandleRestaurantOwnerCreateService,
) {
    fun getAllJoinRequests(): List<JoinRequest> {
        return joinRequestRepository.findAll()
    }

    fun handleAcceptInvite(inviteId: Long) {
        val joinRequest = getValidJoinRequest(inviteId)
        handleRestaurantOwnerCreateService.handleCreateRestaurantOwner(
            RestaurantOwnerRequestDto(
                joinRequest.email,
                joinRequest.firstName,
                joinRequest.lastName,
            ),
        )
        joinRequest.status = JoinRequestStatus.INVITATION_SENT
    }

    fun handleRejectInvite(inviteId: Long) {
        val joinRequest = getValidJoinRequest(inviteId)
        emailDispatchService.sendJoinRequestRejectEmail(joinRequest.email)
        joinRequest.status = JoinRequestStatus.REJECTED
    }

    private fun getValidJoinRequest(inviteId: Long): JoinRequest {
        return joinRequestRepository.findById(inviteId).orElseThrow {
            EntityNotFoundException("Request with id $inviteId not found")
        }
    }
}
