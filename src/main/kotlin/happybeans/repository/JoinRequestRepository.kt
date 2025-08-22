package happybeans.repository

import happybeans.model.JoinRequest
import org.springframework.data.jpa.repository.JpaRepository

interface JoinRequestRepository : JpaRepository<JoinRequest, Long>
