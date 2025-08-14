package happybeans.infrastructure

import happybeans.dto.auth.AuthTokenPayload
import happybeans.utils.exception.UnauthorisedUserException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey
import kotlin.jvm.java
import kotlin.text.toByteArray

@Component
class JwtProvider(
    @Value("\${security.jwt.token.secret-key}")
    private val secret: String,
    @Value("\${security.jwt.token.expire-length}")
    private val validityInMilliseconds: Long,
) {
    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun createToken(authTokenPayload: AuthTokenPayload): String {
        val now = Date()
        val expirationDate = Date(now.time + validityInMilliseconds)
        return Jwts.builder()
            .claim("email", authTokenPayload.email)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun getPayload(token: String): AuthTokenPayload {
        val claims =
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload

        val email = claims.get("email", String::class.java)
        return AuthTokenPayload(email)
    }

    fun validateToken(token: String) {
        try {
            val claims =
                Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
            claims.payload.expiration.before(Date())
        } catch (_: JwtException) {
            throw UnauthorisedUserException("Token not valid")
        } catch (_: IllegalArgumentException) {
            throw kotlin.IllegalArgumentException("Invalid JWT token")
        }
    }
}
