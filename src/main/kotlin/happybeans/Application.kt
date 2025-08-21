package happybeans

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HappyBeansApplication

fun loadDotenv() {
    val dotenv = Dotenv.configure().ignoreIfMissing().load()
    dotenv.entries().forEach { System.setProperty(it.key, it.value) }
}

fun main(args: Array<String>) {
    loadDotenv()
    runApplication<HappyBeansApplication>(*args)
}
