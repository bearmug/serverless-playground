package bearmug.lambda

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import reactor.core.publisher.Mono

@Controller("/ping")
class PingController(
        private val pingService: PingService) {

    @Get("/kotlin")
    fun pingKotlin() = pingService.pong()

    @Get("/graal")
    fun pingGraal() = pingService.pong()

    @Get("/rkotlin")
    fun pingKotlinReactive() = Mono.just(pingService.pong())

    @Get("/rgraal")
    fun pingGraalReactive() = Mono.just(pingService.pong())
}