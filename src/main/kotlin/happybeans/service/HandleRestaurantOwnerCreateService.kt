package happybeans.service

import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.model.User
import org.springframework.stereotype.Service

@Service
class HandleRestaurantOwnerCreateService(
    private val createRestaurantOwnerService: CreateRestaurantOwnerService,
    private val emailDispatchService: EmailDispatchService,
) {
    fun handleCreateRestaurantOwner(request: RestaurantOwnerRequestDto): User {
        val user = createRestaurantOwnerService.createRestaurantOwner(request)
        emailDispatchService.sendRestaurantOwnerWelcomeEmail(
            user.email,
            user.password,
        )
        return user
    }
}
