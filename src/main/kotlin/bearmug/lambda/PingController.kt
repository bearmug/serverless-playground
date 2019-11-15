package bearmug.lambda

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import reactor.core.publisher.Mono

@Controller("/ping")
class PingController(
        private val pingService: PingService) {

    @Get("/java")
    fun pingJava() = Mono.just(pingService.pong())

    @Get("/graal")
    fun pingGraal() = Mono.just(pingService.pong())
}