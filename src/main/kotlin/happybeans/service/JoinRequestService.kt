package happybeans.service

import happybeans.dto.joinRequest.JoinRequestDto
import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.enums.JoinRequestStatus
import happybeans.model.JoinRequest
import happybeans.repository.JoinRequestRepository
import happybeans.utils.exception.DuplicateEntityException
import happybeans.utils.exception.EntityNotFoundException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JoinRequestService(
    private val joinRequestRepository: JoinRequestRepository,
    private val emailDispatchService: EmailDispatchService,
    private val handleRestaurantOwnerCreateService: HandleRestaurantOwnerCreateService,
) {
    private val logger = KotlinLogging.logger {}

    fun getAllJoinRequests(): List<JoinRequest> {
        return joinRequestRepository.findAll()
    }

    fun createJoinRequest(joinRequestDto: JoinRequestDto): JoinRequest {
        if (joinRequestRepository.existsByEmail(joinRequestDto.email)) {
            logger.error { "Request already exists for email ${joinRequestDto.email}" }
            throw DuplicateEntityException("Request already exists")
        }

        return joinRequestRepository.save(
            JoinRequest(
                joinRequestDto.email,
                joinRequestDto.firstName,
                joinRequestDto.lastName,
                joinRequestDto.message,
            ),
        )
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
            logger.error { "Request does not exist for inviteId $inviteId" }
            EntityNotFoundException("Request with id $inviteId not found")
        }
    }
}
