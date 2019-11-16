package bearmug.lambda;

import lombok.extern.slf4j.Slf4j;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class PingServiceImpl implements PingService {

    @Override
    public String pong() {
        log.trace("Received a ping.");
        return "{ \"pong\" : true }";
    }
}
