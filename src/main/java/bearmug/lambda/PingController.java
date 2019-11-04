package bearmug.lambda;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

@Controller("/ping")
public class PingController {

	@Inject
	PingService pingService;

    @Get("/java")
    public Mono<String> pingJava() {
        return Mono.just(pingService.pong());
    }

    @Get("/graal")
    public Mono<String> pingGraal() {
        return Mono.just(pingService.pong());
    }
}