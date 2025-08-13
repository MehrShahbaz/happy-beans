package happybeans.infrastructure

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.jvm.java

@Component
class ApplicationLogger {
    val log: Logger = LoggerFactory.getLogger(ApplicationLogger::class.java)

    fun logError(message: String?) {
        log.error(message)
    }

    fun logWarning(message: String?) {
        log.warn(message)
    }
}
