package bearmug.lambda

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import reactor.core.publisher.Mono

@Controller("/ping")
class PingController(
        private val pingService: PingService) {

    @Get("/jvm-block")
    fun pingJava() = pingService.pong()

    @Get("/graal-block")
    fun pingGraal() = pingService.pong()

    @Get("/jvm-reactive")
    fun pingJavaReactive() = Mono.just(pingService.pong())

    @Get("/graal-reactive")
    fun pingGraalReactive() = Mono.just(pingService.pong())
}