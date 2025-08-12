package happybeans

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HappyBeansApplication

fun main(args: Array<String>) {
    runApplication<HappyBeansApplication>(*args)
}
