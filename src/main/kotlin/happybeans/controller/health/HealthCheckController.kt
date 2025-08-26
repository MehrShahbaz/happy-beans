package happybeans.controller.health

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/health")
class HealthCheckController {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun healthCheck(): String {
        logger.info("HEALTHY")
        return "OK"
    }
}
