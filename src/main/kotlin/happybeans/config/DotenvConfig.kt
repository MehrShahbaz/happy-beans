package happybeans.config

import io.github.cdimascio.dotenv.Dotenv
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import kotlin.collections.forEach

@Configuration
class DotenvConfig {
    @PostConstruct
    fun loadDotenv() {
        val dotenv = Dotenv.configure().ignoreIfMissing().load()
        dotenv.entries().forEach { System.setProperty(it.key, it.value) }
    }
}
