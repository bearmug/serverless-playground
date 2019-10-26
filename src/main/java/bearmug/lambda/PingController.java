package bearmug.lambda;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;

@Controller("/ping")
public class PingController {

	@Inject
	PingService pingService;

    @Get("/java")
    public String pingJava() {
        return pingService.pong();
    }

    @Get("/graal")
    public String pingGraal() {
        return pingService.pong();
    }
}