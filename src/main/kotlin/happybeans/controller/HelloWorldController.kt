package happybeans.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {
    @GetMapping("/api/hello-world")
    fun helloWorld(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, World!")
    }
}
