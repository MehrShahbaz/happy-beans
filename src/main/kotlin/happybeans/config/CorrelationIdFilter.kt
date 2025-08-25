package happybeans.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class CorrelationIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        // Check for an incoming correlation ID header. If not present, generate one.
        val correlationId = request.getHeader("X-Correlation-Id") ?: UUID.randomUUID().toString()

        // Store the ID in the Mapped Diagnostic Context (MDC) for Logback to use
        MDC.put("correlationId", correlationId)

        // Return the ID in the response header for client-side tracking
        response.setHeader("X-Correlation-Id", correlationId)

        try {
            // Continue the filter chain
            filterChain.doFilter(request, response)
        } finally {
            // Always clean up the MDC after the request is complete
            MDC.remove("correlationId")
        }
    }
}
