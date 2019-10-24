package bearmug.lambda;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/ping")
public class PingController {

	private Logger log = LoggerFactory.getLogger(PingController.class);

    @Get("/java")
    public String pingJava() {
        return ping();
    }

    @Get("/graal")
    public String pingGraal() {
        return ping();
    }

    private String ping() {
        log.trace("Received a ping.");
        return "{\"pong\":true}";
    }
}